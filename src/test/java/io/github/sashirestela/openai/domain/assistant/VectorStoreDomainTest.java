package io.github.sashirestela.openai.domain.assistant;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.DomainTestingHelper.MockForType;
import io.github.sashirestela.openai.domain.assistant.ExpiresAfter.Anchor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

public class VectorStoreDomainTest {

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
    void testCreateVectorStore() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.OBJECT, List.of(
                                "src/test/resources/vector_store__create.json",
                                "src/test/resources/vector_store__create.json")));
        var vectorStoreRequest = VectorStoreRequest.builder()
                .name("Demo Vector Store")
                .fileId("fileId")
                .expiresAfter(ExpiresAfter.builder()
                        .anchor(Anchor.LAST_ACTIVE_AT)
                        .days(1)
                        .build())
                .metadata(Map.of("env", "test"))
                .build();
        var vectorStore = openAI.vectorStores().createAndPoll(vectorStoreRequest);
        System.out.println(vectorStore);
        assertNotNull(vectorStore);
    }

    @Test
    void testModifyVectorStore() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/vector_store__modify.json");
        var vectorStoreModifyRequest = VectorStoreModifyRequest.builder()
                .name("Demonstration Vector Store")
                .metadata(Map.of("env", "testing"))
                .metadata(Map.of("user", "mary"))
                .build();
        var vectorStore = openAI.vectorStores().modify("vectorStoreId", vectorStoreModifyRequest).join();
        System.out.println(vectorStore);
        assertNotNull(vectorStore);
    }

    @Test
    void testRetrieveVectorStore() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/vector_store__getone.json");
        var vectorStore = openAI.vectorStores().getOne("vectorStoreId").join();
        System.out.println(vectorStore);
        assertNotNull(vectorStore);
    }

    @Test
    void testListVectorStores() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/vector_store__getlist.json");
        var vectorStores = openAI.vectorStores().getList().join();
        vectorStores.forEach(System.out::println);
        assertNotNull(vectorStores);
    }

    @Test
    void deleteVectorStore() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/vector_store__delete.json");
        var deletedVectorStore = openAI.vectorStores().delete("vectorStoreId").join();
        System.out.println(deletedVectorStore);
        assertNotNull(deletedVectorStore);
    }

}
