package io.github.sashirestela.openai.domain.assistant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartTextAnnotation;
import io.github.sashirestela.openai.common.content.FileAnnotation.FileCitationAnnotation;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.common.function.Functional;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.DomainTestingHelper.MockForType;
import io.github.sashirestela.openai.domain.assistant.ThreadRun.RunStatus;
import io.github.sashirestela.openai.domain.assistant.ThreadRunSubmitOutputRequest.ToolOutput;
import io.github.sashirestela.openai.domain.assistant.events.EventName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ThreadRunDomainTest {

    static HttpClient httpClient;
    static SimpleOpenAI openAI;
    static FunctionExecutor functionExecutor;

    @BeforeAll
    static void setup() {
        httpClient = mock(HttpClient.class);
        openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
        List<FunctionDef> functionList = new ArrayList<>();
        functionList.add(FunctionDef.builder()
                .name("CurrentTemperature")
                .description("Get the current temperature for a specific location")
                .functionalClass(CurrentTemperature.class)
                .strict(Boolean.TRUE)
                .build());
        functionList.add(FunctionDef.builder()
                .name("RainProbability")
                .description("Get the probability of rain for a specific location")
                .functionalClass(RainProbability.class)
                .strict(Boolean.TRUE)
                .build());
        functionExecutor = new FunctionExecutor();
        functionExecutor.enrollFunctions(functionList);
    }

    @Test
    void testCreateThreadRun() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.OBJECT, List.of(
                                "src/test/resources/threads_runs_create_1.json",
                                "src/test/resources/threads_runs_create_1.json",
                                "src/test/resources/threads_runs_create_2.json")));
        var question = "Tell me something brief about Lima, Peru.";
        System.out.println("Question: " + question);
        var threadRunRequest = ThreadRunRequest.builder()
                .assistantId("assistantId")
                .additionalMessage(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content(question)
                        .build())
                .build();
        var threadRun = openAI.threadRuns().createAndPoll("threadId", threadRunRequest);
        var threadMessages = openAI.threadMessages().getList("threadId").join();
        var answer = ((ContentPartTextAnnotation) threadMessages.first().getContent().get(0)).getText().getValue();
        System.out.println("Answer: " + answer);
        System.out.println(threadRun);
        assertNotNull(threadRun);
    }

    @Test
    void testCreateThreadRunStream() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.STREAM, List.of("src/test/resources/threads_runs_create_stream_1.txt")));
        var question = "Tell me something pretty brief about its people.";
        System.out.println("Question: " + question);
        var threadRunRequest = ThreadRunRequest.builder()
                .assistantId("assistantId")
                .additionalMessage(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content(question)
                        .build())
                .parallelToolCalls(Boolean.TRUE)
                .build();
        var response = openAI.threadRuns().createStream("threadId", threadRunRequest).join();
        System.out.print("Answer: ");
        response.forEach(e -> {
            switch (e.getName()) {
                case EventName.THREAD_MESSAGE_DELTA:
                    var messageDeltaFirstContent = ((ThreadMessageDelta) e.getData()).getDelta().getContent().get(0);
                    if (messageDeltaFirstContent instanceof ContentPartTextAnnotation) {
                        System.out.print(((ContentPartTextAnnotation) messageDeltaFirstContent).getText().getValue());
                    }
                    break;
                case EventName.THREAD_MESSAGE_COMPLETED:
                    System.out.println();
                    break;
                case EventName.THREAD_RUN_COMPLETED:
                    var threadRun = (ThreadRun) e.getData();
                    System.out.println(threadRun);
                    assertNotNull(threadRun);
                    break;
                default:
                    break;
            }
        });
    }

    @Test
    void testSubmitToolOutputToThreadRun() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.OBJECT, List.of(
                                "src/test/resources/threads_runs_submittool_1.json",
                                "src/test/resources/threads_runs_submittool_1.json",
                                "src/test/resources/threads_runs_submittool_2.json",
                                "src/test/resources/threads_runs_submittool_2.json",
                                "src/test/resources/threads_runs_submittool_3.json")));
        var question = "What is the temperature there currently?";
        System.out.println("Question: " + question);
        var threadRunRequest = ThreadRunRequest.builder()
                .assistantId("assistantId")
                .additionalMessage(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content(question)
                        .build())
                .build();
        var threadRun = openAI.threadRuns().createAndPoll("threadId", threadRunRequest);
        if (threadRun.getStatus().equals(RunStatus.REQUIRES_ACTION)) {
            var toolCalls = threadRun.getRequiredAction().getSubmitToolOutputs().getToolCalls();
            var toolOutputs = functionExecutor.executeAll(toolCalls,
                    (toolCallId, result) -> ToolOutput.builder().toolCallId(toolCallId).output(result).build());
            var threadRunSubmit = openAI.threadRuns()
                    .submitToolOutputAndPoll("threadId", threadRun.getId(), ThreadRunSubmitOutputRequest.builder()
                            .toolOutputs(toolOutputs)
                            .build());
            var threadMessages = openAI.threadMessages().getList("threadId").join();
            var answer = ((ContentPartTextAnnotation) threadMessages.first().getContent().get(0)).getText().getValue();
            System.out.println("Answer: " + answer);
            System.out.println(threadRunSubmit);
            assertNotNull(threadRun);
        }
    }

    @Test
    void testSubmitToolOutputToThreadRunStream() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.OBJECT, List.of(
                                "src/test/resources/threads_runs_submittool_stream_1.json",
                                "src/test/resources/threads_runs_submittool_stream_1.json",
                                "src/test/resources/threads_runs_submittool_stream_1.json",
                                "src/test/resources/threads_runs_submittool_stream_1.json"),
                        MockForType.STREAM, List.of(
                                "src/test/resources/threads_runs_submittool_stream_2.txt")));
        var question = "What is the rain probability there currently?";
        System.out.println("Question: " + question);
        var threadRunRequest = ThreadRunRequest.builder()
                .assistantId("assistantId")
                .additionalMessage(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content(question)
                        .build())
                .build();
        var threadRun = openAI.threadRuns().createAndPoll("threadId", threadRunRequest);
        if (threadRun.getStatus().equals(RunStatus.REQUIRES_ACTION)) {
            var toolCalls = threadRun.getRequiredAction().getSubmitToolOutputs().getToolCalls();
            var toolOutputs = functionExecutor.executeAll(toolCalls,
                    (toolCallId, result) -> ToolOutput.builder().toolCallId(toolCallId).output(result).build());
            var response = openAI.threadRuns()
                    .submitToolOutputStream("threadId", threadRun.getId(),
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
                    case EventName.THREAD_MESSAGE_COMPLETED:
                        System.out.println();
                        break;
                    case EventName.THREAD_RUN_COMPLETED:
                        var submittedThreadRun = (ThreadRun) e.getData();
                        System.out.println(submittedThreadRun);
                        assertNotNull(submittedThreadRun);
                        break;
                    default:
                        break;
                }
            });
        }
    }

    @Test
    void testCreateThreadAndThreadRun() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.OBJECT, List.of(
                                "src/test/resources/threads_runs_createthreadandrun_1.json",
                                "src/test/resources/threads_runs_createthreadandrun_1.json",
                                "src/test/resources/threads_runs_createthreadandrun_2.json")));
        var question = "What's the main focus of Mistral and what models are available?.";
        System.out.println("Question: " + question);
        var threadCreateAndRunRequest = ThreadCreateAndRunRequest.builder()
                .assistantId("assistantId")
                .thread(ThreadRequest.builder()
                        .message(
                                ThreadMessageRequest.builder()
                                        .role(ThreadMessageRole.USER)
                                        .content(question)
                                        .build())
                        .build())
                .build();
        var threadRun = openAI.threadRuns().createThreadAndRunAndPoll(threadCreateAndRunRequest);
        var threadMessages = openAI.threadMessages().getList(threadRun.getThreadId()).join();
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
        System.out.println(threadRun);
        assertNotNull(threadRun);
    }

    @Test
    void testCreateThreadAndThreadRunStream() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.STREAM,
                        List.of("src/test/resources/threads_runs_createthreadandrun_stream_1.txt")));
        var question = "Tell me something brief about the Everest mountain.";
        System.out.println("Question: " + question);
        var threadCreateAndRunRequest = ThreadCreateAndRunRequest.builder()
                .assistantId("assistantId")
                .thread(ThreadRequest.builder()
                        .message(
                                ThreadMessageRequest.builder()
                                        .role(ThreadMessageRole.USER)
                                        .content(question)
                                        .build())
                        .build())
                .parallelToolCalls(Boolean.TRUE)
                .build();
        var response = openAI.threadRuns().createThreadAndRunStream(threadCreateAndRunRequest).join();
        System.out.print("Answer: ");
        response.forEach(e -> {
            switch (e.getName()) {
                case EventName.THREAD_MESSAGE_DELTA:
                    var messageDeltaFirstContent = ((ThreadMessageDelta) e.getData()).getDelta().getContent().get(0);
                    if (messageDeltaFirstContent instanceof ContentPartTextAnnotation) {
                        System.out.print(((ContentPartTextAnnotation) messageDeltaFirstContent).getText().getValue());
                    }
                    break;
                case EventName.THREAD_MESSAGE_COMPLETED:
                    System.out.println();
                    break;
                case EventName.THREAD_RUN_COMPLETED:
                    var threadRun = (ThreadRun) e.getData();
                    System.out.println(threadRun);
                    assertNotNull(threadRun);
                    break;
                default:
                    break;
            }
        });
    }

    @Test
    void testCancelThreadRun() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.OBJECT, List.of(
                                "src/test/resources/threads_runs_cancel_1.json",
                                "src/test/resources/threads_runs_cancel_1.json",
                                "src/test/resources/threads_runs_cancel_2.json")));
        var question = "Tell me about the origins of the soccer game";
        System.out.println("Question: " + question);
        var threadRunRequest = ThreadRunRequest.builder()
                .assistantId("assistantId")
                .additionalMessage(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content(question)
                        .build())
                .build();
        var threadRun = openAI.threadRuns().create("threadId", threadRunRequest).join();
        var cancelledThreadRun = openAI.threadRuns().cancel("threadId", threadRun.getId()).join();
        if (cancelledThreadRun.getStatus().equals(RunStatus.CANCELLING)) {
            System.out.println("The answer was cancelled.");
        }
        System.out.println(cancelledThreadRun);
        assertNotNull(cancelledThreadRun);
    }

    @Test
    void testModifyThreadRun() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runs_modify.json");
        var threadRunModifyRequest = ThreadRunModifyRequest.builder()
                .metadata(Map.of("env", "test", "user", "tom"))
                .build();
        var threadRun = openAI.threadRuns().modify("threadId", "threadRunId", threadRunModifyRequest).join();
        System.out.println(threadRun);
        assertNotNull(threadRun);
    }

    @Test
    void testRetrieveThreadRun() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runs_getone.json");
        var threadRun = openAI.threadRuns().getOne("threadId", "threadRunId").join();
        System.out.println(threadRun);
        assertNotNull(threadRun);
    }

    @Test
    void testListThreadRuns() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runs_getlist.json");
        var threadRuns = openAI.threadRuns().getList("threadId").join();
        threadRuns.forEach(System.out::println);
        assertNotNull(threadRuns);
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
