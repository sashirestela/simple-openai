package io.github.sashirestela.openai.domain.assistant;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.DomainTestingHelper.MockForType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class VectorStoreFileBatchDomainTest {

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
    void testCreateVectorStoreFileBatch() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.OBJECT, List.of(
                                "src/test/resources/vector_store_file_batch_create.json",
                                "src/test/resources/vector_store_file_batch_create.json")));
        var vectorStoreFileBatch = openAI.vectorStoreFileBatches()
                .createAndPoll("vectorStoreId", List.of("fileId1", "fileId2"));
        System.out.println(vectorStoreFileBatch);
        assertNotNull(vectorStoreFileBatch);
    }

    @Test
    void testRetrieveVectorStoreFileBatch() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/vector_store_file_batch_getone.json");
        var vectorStoreFileBatch = openAI.vectorStoreFileBatches()
                .getOne("vectorStoreId", "vectorStoreFileBatchId")
                .join();
        System.out.println(vectorStoreFileBatch);
        assertNotNull(vectorStoreFileBatch);
    }

    @Test
    void testListVectorStoreFilesInBatch() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/vector_store_file_batch_getfiles.json");
        var vectorStoreFiles = openAI.vectorStoreFileBatches()
                .getFiles("vectorStoreId", "vectorStoreFileBatchId")
                .join();
        vectorStoreFiles.forEach(System.out::println);
        assertNotNull(vectorStoreFiles);
    }

    @Test
    void testCancelVectorStoreFileBatch() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.OBJECT, List.of(
                                "src/test/resources/vector_store_file_batch_create.json",
                                "src/test/resources/vector_store_file_batch_create.json",
                                "src/test/resources/vector_store_file_batch_cancel.json")));
        var vectorStoreFileBatch = openAI.vectorStoreFileBatches()
                .create("vectorStoreId", List.of("fileId1", "fileId2"))
                .join();
        var newVectorStoreFileBatchId = vectorStoreFileBatch.getId();
        vectorStoreFileBatch = openAI.vectorStoreFileBatches()
                .cancel("vectorStoreId", newVectorStoreFileBatchId)
                .join();
        System.out.println(vectorStoreFileBatch);
        assertNotNull(vectorStoreFileBatch);
    }

}
