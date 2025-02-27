package io.github.sashirestela.openai.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartTextAnnotation;
import io.github.sashirestela.openai.common.content.FileAnnotation.FileCitationAnnotation;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.common.function.Functional;
import io.github.sashirestela.openai.domain.assistant.AssistantRequest;
import io.github.sashirestela.openai.domain.assistant.AssistantTool;
import io.github.sashirestela.openai.domain.assistant.ThreadCreateAndRunRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageDelta;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadMessageRole;
import io.github.sashirestela.openai.domain.assistant.ThreadRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRun;
import io.github.sashirestela.openai.domain.assistant.ThreadRun.RunStatus;
import io.github.sashirestela.openai.domain.assistant.ThreadRunModifyRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunSubmitOutputRequest;
import io.github.sashirestela.openai.domain.assistant.ThreadRunSubmitOutputRequest.ToolOutput;
import io.github.sashirestela.openai.domain.assistant.ToolResourceFull;
import io.github.sashirestela.openai.domain.assistant.ToolResourceFull.FileSearch;
import io.github.sashirestela.openai.domain.assistant.VectorStoreRequest;
import io.github.sashirestela.openai.domain.assistant.events.EventName;
import io.github.sashirestela.openai.domain.file.FileRequest.PurposeType;
import io.github.sashirestela.openai.service.AssistantServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThreadRunV2Demo extends AbstractDemo {

    private FileDemo fileDemo;
    private String fileId;
    private String vectorStoreId;
    private FunctionExecutor functionExecutor;
    private String assistantId;
    private String threadId;
    private String newThreadId;
    private String threadRunId;

    protected String model;
    protected AssistantServices assistantProvider;

    protected ThreadRunV2Demo(String model) {
        this("standard", model, new FileDemo());
    }

    protected ThreadRunV2Demo(String provider, String model, FileDemo fileDemo) {
        super(provider);
        this.model = model;
        this.assistantProvider = this.openAI;
        this.fileDemo = fileDemo;
    }

    public void prepareDemo() {
        var file = fileDemo.createFile("src/demo/resources/mistral-ai.txt", PurposeType.ASSISTANTS);
        fileId = file.getId();

        var vectorStore = assistantProvider.vectorStores()
                .createAndPoll(VectorStoreRequest.builder().fileId(fileId).build());
        vectorStoreId = vectorStore.getId();

        List<FunctionDef> functionList = new ArrayList<>();
        functionList.add(FunctionDef.builder()
                .name("CurrentTemperature")
                .description("Get the current temperature for a specific location")
                .functionalClass(CurrentTemperature.class)
                .build());
        functionList.add(FunctionDef.builder()
                .name("RainProbability")
                .description("Get the probability of rain for a specific location")
                .functionalClass(RainProbability.class)
                .build());

        functionExecutor = new FunctionExecutor();
        functionExecutor.enrollFunctions(functionList);

        var assistant = assistantProvider.assistants()
                .create(AssistantRequest.builder()
                        .model(this.model)
                        .name("Demo Assistant")
                        .instructions("You are a very kind assistant. If you cannot find correct facts to answer the "
                                + "questions, you have to refer to the attached files or use the functions provided. "
                                + "Finally, if you receive math questions, you must write and run code to answer them.")
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

    public void createThreadRun() {
        var thread = assistantProvider.threads().create().join();
        threadId = thread.getId();

        var question = "Tell me something brief about Lima, Peru.";
        System.out.println("Question: " + question);
        var threadRunRequest = ThreadRunRequest.builder()
                .assistantId(assistantId)
                .additionalMessage(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content(question)
                        .build())
                .parallelToolCalls(Boolean.FALSE)
                .build();
        var threadRun = assistantProvider.threadRuns().createAndPoll(threadId, threadRunRequest);
        var threadMessages = assistantProvider.threadMessages().getList(threadId).join();
        var answer = ((ContentPartTextAnnotation) threadMessages.first().getContent().get(0)).getText().getValue();
        System.out.println("Answer: " + answer);
        threadRunId = threadRun.getId();
    }

    public void createThreadRunStream() {
        var question = "Tell me something pretty brief about its people.";
        System.out.println("Question: " + question);
        var threadRunRequest = ThreadRunRequest.builder()
                .assistantId(assistantId)
                .additionalMessage(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content(question)
                        .build())
                .build();
        var response = assistantProvider.threadRuns().createStream(threadId, threadRunRequest).join();
        System.out.print("Answer: ");
        response.forEach(e -> {
            switch (e.getName()) {
                case EventName.THREAD_MESSAGE_DELTA:
                    var messageDeltaFirstContent = ((ThreadMessageDelta) e.getData()).getDelta().getContent().get(0);
                    if (messageDeltaFirstContent instanceof ContentPartTextAnnotation) {
                        System.out.print(((ContentPartTextAnnotation) messageDeltaFirstContent).getText().getValue());
                    }
                    break;
                default:
                    break;
            }
        });
        System.out.println();
    }

    public void submitToolOutputToThreadRun() {
        var question = "What is the temperature there currently?";
        System.out.println("Question: " + question);
        var threadRunRequest = ThreadRunRequest.builder()
                .assistantId(assistantId)
                .additionalMessage(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content(question)
                        .build())
                .build();
        var threadRun = assistantProvider.threadRuns().createAndPoll(threadId, threadRunRequest);
        if (threadRun.getStatus().equals(RunStatus.REQUIRES_ACTION)) {
            var toolCalls = threadRun.getRequiredAction().getSubmitToolOutputs().getToolCalls();
            var toolOutputs = functionExecutor.executeAll(toolCalls,
                    (toolCallId, result) -> ToolOutput.builder().toolCallId(toolCallId).output(result).build());
            assistantProvider.threadRuns()
                    .submitToolOutputAndPoll(threadId, threadRun.getId(), ThreadRunSubmitOutputRequest.builder()
                            .toolOutputs(toolOutputs)
                            .build());
            var threadMessages = assistantProvider.threadMessages().getList(threadId).join();
            var answer = ((ContentPartTextAnnotation) threadMessages.first().getContent().get(0)).getText().getValue();
            System.out.println("Answer: " + answer);
        }
    }

    public void submitToolOutputToThreadRunStream() {
        var question = "What is the rain probability there currently?";
        System.out.println("Question: " + question);
        var threadRunRequest = ThreadRunRequest.builder()
                .assistantId(assistantId)
                .additionalMessage(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content(question)
                        .build())
                .build();
        var threadRun = assistantProvider.threadRuns().createAndPoll(threadId, threadRunRequest);
        if (threadRun.getStatus().equals(RunStatus.REQUIRES_ACTION)) {
            var toolCalls = threadRun.getRequiredAction().getSubmitToolOutputs().getToolCalls();
            var toolOutputs = functionExecutor.executeAll(toolCalls,
                    (toolCallId, result) -> ToolOutput.builder().toolCallId(toolCallId).output(result).build());
            var response = assistantProvider.threadRuns()
                    .submitToolOutputStream(threadId, threadRun.getId(),
                            ThreadRunSubmitOutputRequest.builder().toolOutputs(toolOutputs).build())
                    .join();
            System.out.print("Answer: ");
            response.forEach(e -> {
                switch (e.getName()) {
                    case EventName.THREAD_MESSAGE_DELTA:
                        var messageDeltaFirstContent = ((ThreadMessageDelta) e.getData()).getDelta()
                                .getContent()
                                .get(0);
                        if (messageDeltaFirstContent instanceof ContentPartTextAnnotation) {
                            System.out
                                    .print(((ContentPartTextAnnotation) messageDeltaFirstContent).getText().getValue());
                        }
                        break;
                    default:
                        break;
                }
            });
            System.out.println();
        }
    }

    public void createThreadAndThreadRun() {
        var question = "What's the main focus of Mistral and what models are available?.";
        System.out.println("Question: " + question);
        var threadCreateAndRunRequest = ThreadCreateAndRunRequest.builder()
                .assistantId(assistantId)
                .thread(ThreadRequest.builder()
                        .message(
                                ThreadMessageRequest.builder()
                                        .role(ThreadMessageRole.USER)
                                        .content(question)
                                        .build())
                        .build())
                .build();
        var threadRun = assistantProvider.threadRuns().createThreadAndRunAndPoll(threadCreateAndRunRequest);
        newThreadId = threadRun.getThreadId();
        var threadMessages = assistantProvider.threadMessages().getList(newThreadId).join();
        var textAnnotation = ((ContentPartTextAnnotation) threadMessages.first().getContent().get(0)).getText();
        var answer = textAnnotation.getValue();
        var refNumber = 1;
        for (var fileAnnotation : textAnnotation.getAnnotations()) {
            if (fileAnnotation instanceof FileCitationAnnotation) {
                var fileCitation = (FileCitationAnnotation) fileAnnotation;
                answer = answer.replaceFirst(fileCitation.getText(), " [" + refNumber++ + "]");
            }
        }
        System.out.println("Answer: " + answer);
        assistantProvider.threads().delete(newThreadId).join();
    }

    public void createThreadAndThreadRunStream() {
        var question = "Tell me something brief about the Everest mountain.";
        System.out.println("Question: " + question);
        var threadCreateAndRunRequest = ThreadCreateAndRunRequest.builder()
                .assistantId(assistantId)
                .thread(ThreadRequest.builder()
                        .message(
                                ThreadMessageRequest.builder()
                                        .role(ThreadMessageRole.USER)
                                        .content(question)
                                        .build())
                        .build())
                .build();
        var response = assistantProvider.threadRuns().createThreadAndRunStream(threadCreateAndRunRequest).join();
        System.out.print("Answer: ");
        response.forEach(e -> {
            switch (e.getName()) {
                case EventName.THREAD_RUN_CREATED:
                    var threadRun = (ThreadRun) e.getData();
                    newThreadId = threadRun.getThreadId();
                    break;
                case EventName.THREAD_MESSAGE_DELTA:
                    var messageDeltaFirstContent = ((ThreadMessageDelta) e.getData()).getDelta().getContent().get(0);
                    if (messageDeltaFirstContent instanceof ContentPartTextAnnotation) {
                        System.out.print(((ContentPartTextAnnotation) messageDeltaFirstContent).getText().getValue());
                    }
                    break;
                default:
                    break;
            }
        });
        System.out.println();
        assistantProvider.threads().delete(newThreadId).join();
    }

    public void cancelThreadRun() {
        var question = "Tell me about the origins of the soccer game";
        System.out.println("Question: " + question);
        var threadRunRequest = ThreadRunRequest.builder()
                .assistantId(assistantId)
                .additionalMessage(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content(question)
                        .build())
                .build();
        var threadRun = assistantProvider.threadRuns().create(threadId, threadRunRequest).join();
        var cancelledThreadRun = assistantProvider.threadRuns().cancel(threadId, threadRun.getId()).join();
        if (cancelledThreadRun.getStatus().equals(RunStatus.CANCELLING)) {
            System.out.println("The answer was cancelled.");
        }
    }

    public void modifyThreadRun() {
        var threadRunModifyRequest = ThreadRunModifyRequest.builder()
                .metadata(Map.of("env", "test", "user", "tom"))
                .build();
        var threadRun = assistantProvider.threadRuns().modify(threadId, threadRunId, threadRunModifyRequest).join();
        System.out.println(threadRun);
    }

    public void retrieveThreadRun() {
        var threadRun = assistantProvider.threadRuns().getOne(threadId, threadRunId).join();
        System.out.println(threadRun);
    }

    public void listThreadRuns() {
        var threadRuns = assistantProvider.threadRuns().getList(threadId).join();
        threadRuns.forEach(System.out::println);
    }

    public void deleteDemo() {
        var deletedThread = assistantProvider.threads().delete(threadId).join();
        System.out.println(deletedThread);

        var deletedAssistant = assistantProvider.assistants().delete(assistantId).join();
        System.out.println(deletedAssistant);

        var deletedFile = fileDemo.deleteFile(fileId);
        System.out.println(deletedFile);

        var deletedVectorStore = assistantProvider.vectorStores().delete(vectorStoreId).join();
        System.out.println(deletedVectorStore);
    }

    public static void main(String[] args) {
        var demo = new ThreadRunV2Demo("gpt-4o-mini");
        demo.prepareDemo();
        demo.addTitleAction("Demo ThreadRun v2 Create", demo::createThreadRun);
        demo.addTitleAction("Demo ThreadRun v2 Create Stream", demo::createThreadRunStream);
        demo.addTitleAction("Demo ThreadRun v2 Submit Tool Output", demo::submitToolOutputToThreadRun);
        demo.addTitleAction("Demo ThreadRun v2 Submit Tool Output Stream", demo::submitToolOutputToThreadRunStream);
        demo.addTitleAction("Demo ThreadRun v2 Create Thread and Run", demo::createThreadAndThreadRun);
        demo.addTitleAction("Demo ThreadRun v2 Create Thread and Run Stream", demo::createThreadAndThreadRunStream);
        demo.addTitleAction("Demo ThreadRun v2 Cancel", demo::cancelThreadRun);
        demo.addTitleAction("Demo ThreadRun v2 Modify", demo::modifyThreadRun);
        demo.addTitleAction("Demo ThreadRun v2 Retrieve", demo::retrieveThreadRun);
        demo.addTitleAction("Demo ThreadRun v2 List", demo::listThreadRuns);
        demo.addTitleAction("Demo ThreadRun v2 Delete", demo::deleteDemo);
        demo.run();
    }

}
