package io.github.sashirestela.openai.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.cleverclient.Event;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartTextAnnotation;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.common.function.Functional;
import io.github.sashirestela.openai.domain.assistant.AssistantRequest;
import io.github.sashirestela.openai.domain.assistant.AssistantTool;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageDelta;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRole;
import io.github.sashirestela.openai.domain.assistant.ThreadRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRun;
import io.github.sashirestela.openai.domain.assistant.ThreadRun.RunStatus;
import io.github.sashirestela.openai.domain.assistant.ThreadRunRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunSubmitOutputRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunSubmitOutputRequest.ToolOutput;
import io.github.sashirestela.openai.domain.assistant.ToolResourceFull;
import io.github.sashirestela.openai.domain.assistant.ToolResourceFull.FileSearch;
import io.github.sashirestela.openai.domain.assistant.VectorStoreRequest;
import io.github.sashirestela.openai.domain.assistant.events.EventName;
import io.github.sashirestela.openai.domain.file.FileRequest;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ConversationV2Demo {

    private SimpleOpenAI openAI;
    private String fileId;
    private String vectorStoreId;
    private FunctionExecutor functionExecutor;
    private String assistantId;
    private String threadId;

    public ConversationV2Demo() {
        openAI = SimpleOpenAI.builder().apiKey(System.getenv("OPENAI_API_KEY")).build();
    }

    public void prepareConversation() {
        List<FunctionDef> functionList = new ArrayList<>();
        functionList.add(FunctionDef.builder()
                .name("getCurrentTemperature")
                .description("Get the current temperature for a specific location")
                .functionalClass(CurrentTemperature.class)
                .build());
        functionList.add(FunctionDef.builder()
                .name("getRainProbability")
                .description("Get the probability of rain for a specific location")
                .functionalClass(RainProbability.class)
                .build());
        functionExecutor = new FunctionExecutor(functionList);

        var file = openAI.files()
                .create(FileRequest.builder()
                        .file(Paths.get("src/demo/resources/mistral-ai.txt"))
                        .purpose(PurposeType.ASSISTANTS)
                        .build())
                .join();
        fileId = file.getId();
        System.out.println("File was created with id: " + fileId);

        var vectorStore = openAI.vectorStores()
                .createAndPoll(VectorStoreRequest.builder()
                        .fileId(fileId)
                        .build());
        vectorStoreId = vectorStore.getId();
        System.out.println("Vector Store was created with id: " + vectorStoreId);

        var assistant = openAI.assistants()
                .create(AssistantRequest.builder()
                        .name("World Assistant")
                        .model("gpt-4o")
                        .instructions("You are a skilled tutor on geo-politic topics.")
                        .tools(functionExecutor.getToolFunctions())
                        .tool(AssistantTool.fileSearch())
                        .toolResources(ToolResourceFull.builder()
                                .fileSearch(FileSearch.builder()
                                        .vectorStoreId(vectorStoreId)
                                        .build())
                                .build())
                        .temperature(0.2)
                        .build())
                .join();
        assistantId = assistant.getId();
        System.out.println("Assistant was created with id: " + assistantId);

        var thread = openAI.threads().create(ThreadRequest.builder().build()).join();
        threadId = thread.getId();
        System.out.println("Thread was created with id: " + threadId);
        System.out.println();
    }

    public void runConversation() {
        var myMessage = System.console().readLine("\nWelcome! Write any message: ");
        while (!myMessage.equalsIgnoreCase("exit")) {
            openAI.threadMessages()
                    .create(threadId, ThreadMessageRequest.builder()
                            .role(ThreadMessageRole.USER)
                            .content(myMessage)
                            .build())
                    .join();
            var runStream = openAI.threadRuns()
                    .createStream(threadId, ThreadRunRequest.builder()
                            .assistantId(assistantId)
                            .parallelToolCalls(Boolean.FALSE)
                            .build())
                    .join();
            handleRunEvents(runStream);
            myMessage = System.console().readLine("\nWrite any message (or write 'exit' to finish): ");
        }
    }

    private void handleRunEvents(Stream<Event> runStream) {
        runStream.forEach(event -> {
            switch (event.getName()) {
                case EventName.THREAD_RUN_CREATED:
                case EventName.THREAD_RUN_COMPLETED:
                case EventName.THREAD_RUN_REQUIRES_ACTION:
                    var run = (ThreadRun) event.getData();
                    System.out.println("=====>> Thread Run: id=" + run.getId() + ", status=" + run.getStatus());
                    if (run.getStatus().equals(RunStatus.REQUIRES_ACTION)) {
                        var toolCalls = run.getRequiredAction().getSubmitToolOutputs().getToolCalls();
                        var toolOutputs = functionExecutor.executeAll(toolCalls,
                                (toolCallId, result) -> ToolOutput.builder()
                                        .toolCallId(toolCallId)
                                        .output(result)
                                        .build());
                        var runSubmitToolStream = openAI.threadRuns()
                                .submitToolOutputStream(threadId, run.getId(), ThreadRunSubmitOutputRequest.builder()
                                        .toolOutputs(toolOutputs)
                                        .stream(true)
                                        .build())
                                .join();
                        handleRunEvents(runSubmitToolStream);
                    }
                    break;
                case EventName.THREAD_MESSAGE_DELTA:
                    var msgDelta = (ThreadMessageDelta) event.getData();
                    var content = msgDelta.getDelta().getContent().get(0);
                    if (content instanceof ContentPartTextAnnotation) {
                        var textContent = (ContentPartTextAnnotation) content;
                        System.out.print(textContent.getText().getValue());
                    }
                    break;
                case EventName.THREAD_MESSAGE_COMPLETED:
                    System.out.println();
                    break;
                default:
                    break;
            }
        });
    }

    public void cleanConversation() {
        var deletedFile = openAI.files().delete(fileId).join();
        var deletedVectorStore = openAI.vectorStores().delete(vectorStoreId).join();
        var deletedAssistant = openAI.assistants().delete(assistantId).join();
        var deletedThread = openAI.threads().delete(threadId).join();

        System.out.println("File was deleted: " + deletedFile.getDeleted());
        System.out.println("Vector Store was deleted: " + deletedVectorStore.getDeleted());
        System.out.println("Assistant was deleted: " + deletedAssistant.getDeleted());
        System.out.println("Thread was deleted: " + deletedThread.getDeleted());
    }

    public static void main(String[] args) {
        var demo = new ConversationV2Demo();
        demo.prepareConversation();
        demo.runConversation();
        demo.cleanConversation();
    }

    public static class CurrentTemperature implements Functional {

        @JsonPropertyDescription("The city and state, e.g., San Francisco, CA")
        @JsonProperty(required = true)
        public String location;

        @JsonPropertyDescription("The temperature unit to use. Infer this from the user's location.")
        @JsonProperty(required = true)
        public String unit;

        @Override
        public Object execute() {
            double centigrades = Math.random() * (40.0 - 10.0) + 10.0;
            double fahrenheit = centigrades * 9.0 / 5.0 + 32.0;
            String shortUnit = unit.substring(0, 1).toUpperCase();
            return shortUnit.equals("C") ? centigrades : (shortUnit.equals("F") ? fahrenheit : 0.0);
        }

    }

    public static class RainProbability implements Functional {

        @JsonPropertyDescription("The city and state, e.g., San Francisco, CA")
        @JsonProperty(required = true)
        public String location;

        @Override
        public Object execute() {
            return Math.random() * 100;
        }

    }

}
