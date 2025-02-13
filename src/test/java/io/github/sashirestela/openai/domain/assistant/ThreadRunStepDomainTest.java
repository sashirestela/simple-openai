package io.github.sashirestela.openai.domain.assistant;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;

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
    void retrieveThreadRunStep() throws IOException {
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

}
