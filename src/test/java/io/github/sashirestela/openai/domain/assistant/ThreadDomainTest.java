package io.github.sashirestela.openai.domain.assistant;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.assistant.Attachment.AttachmentTool;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class ThreadDomainTest {

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
    void testCreateThread() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads__create.json");
        var threadRequest = ThreadRequest.builder()
                .message(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.ASSISTANT)
                        .content("Hi, how can I help ypu?")
                        .build())
                .message(ThreadMessageRequest.builder()
                        .role(ThreadMessageRole.USER)
                        .content("Please, tell me what an LLM is?")
                        .attachment(Attachment.builder()
                                .fileId("fileId")
                                .tool(AttachmentTool.FILE_SEARCH)
                                .build())
                        .metadata(Map.of("item", "first"))
                        .build())
                .metadata(Map.of("env", "test"))
                .build();
        var thread = openAI.threads().create(threadRequest).join();
        System.out.println(thread);
        assertNotNull(thread);
    }

    @Test
    void testCreateMinimalThread() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads__create.json");
        var thread = openAI.threads().create().join();
        System.out.println(thread);
        assertNotNull(thread);
    }

    @Test
    void testModifyThread() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads__modify.json");
        var threadModifyRequest = ThreadModifyRequest.builder()
                .metadata(Map.of("env", "testing"))
                .metadata(Map.of("user", "thomas"))
                .build();
        var thread = openAI.threads().modify("threadId", threadModifyRequest).join();
        System.out.println(thread);
        assertNotNull(thread);
    }

    @Test
    void testRetrieveThread() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads__getone.json");
        var thread = openAI.threads().getOne("threadId").join();
        System.out.println(thread);
        assertNotNull(thread);
    }

    @Test
    void testDeleteThread() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/threads__delete.json");
        var deletedThread = openAI.threads().delete("threadId").join();
        System.out.println(deletedThread);
        assertNotNull(deletedThread);
    }

}
