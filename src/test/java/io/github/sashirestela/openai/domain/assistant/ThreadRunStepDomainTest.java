package io.github.sashirestela.openai.domain.assistant;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.DomainTestingHelper.MockForType;
import io.github.sashirestela.openai.domain.assistant.StepDetail.ToolCallsStep;
import io.github.sashirestela.openai.domain.assistant.StepDetail.ToolCallsStep.StepToolCall.FileSearchToolCall;
import io.github.sashirestela.openai.domain.assistant.events.EventName;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ThreadRunStepDomainTest {

    static HttpClient httpClient;
    static SimpleOpenAI openAI;

    @BeforeAll
    static void setup() {
        httpClient = mock(HttpClient.class);
        openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
    }

    @Test
    void testListThreadRunSteps() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runssteps_getlist.json");
        var threadRunSteps = openAI.threadRunSteps().getList("threadId", "threadRunId").join();
        threadRunSteps.forEach(System.out::println);
        assertNotNull(threadRunSteps);
    }

    @Test
    void testRetrieveThreadRunStep() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads_runssteps_getone.json");
        var threadRunStep = openAI.threadRunSteps().getOne("threadId", "threadRunId", "threadRunStepId").join();
        System.out.println(threadRunStep);
        assertNotNull(threadRunStep);
    }

    @Test
    void testListThreadRunStepsWithUnknownTool() throws IOException {
        DomainTestingHelper.get()
                .mockForObject(httpClient, "src/test/resources/threads_runssteps_getunknown_tool.json");
        var threadRunSteps = openAI.threadRunSteps().getList("threadId", "threadRunId").join();
        threadRunSteps.forEach(System.out::println);
        assertNotNull(threadRunSteps);
    }

    @Test
    void testListThreadRunStepDelta() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.STREAM, List.of("src/test/resources/threads_runssteps_delta.txt")));
        var threadRunRequest = ThreadRunRequest.builder()
                .assistantId("assistantId")
                .additionalMessage(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content("question")
                        .build())
                .build();
        var response = openAI.threadRuns().createStream("threadId", threadRunRequest).join();
        response.forEach(e -> {
            switch (e.getName()) {
                case EventName.THREAD_RUN_STEP_DELTA:
                    var runStepDeltaDetail = ((ThreadRunStepDelta) e.getData()).getDelta();
                    var fileSearchToolCall = (FileSearchToolCall) ((ToolCallsStep) runStepDeltaDetail.getStepDetails())
                            .getToolCalls()
                            .get(0);
                    var rankingOptions = fileSearchToolCall.getFileSearch().getRankingOptions();
                    assertNotNull(rankingOptions);
                    System.out.println(rankingOptions);
                    break;
                default:
                    break;
            }
        });
    }

}
