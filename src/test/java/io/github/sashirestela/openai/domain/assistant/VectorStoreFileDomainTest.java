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

public class VectorStoreFileDomainTest {

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
    void testCreateVectorStoreFile() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.OBJECT, List.of(
                                "src/test/resources/vector_store_file__create.json",
                                "src/test/resources/vector_store_file__create.json")));
        var vectorStoreFile = openAI.vectorStoreFiles().createAndPoll("vectorStoreId", "fileId");
        System.out.println(vectorStoreFile);
        assertNotNull(vectorStoreFile);
    }

    @Test
    void testRetrieveVectorStoreFile() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/vector_store_file__getone.json");
        var vectorStoreFile = openAI.vectorStoreFiles().getOne("vectorStoreId", "fileId").join();
        System.out.println(vectorStoreFile);
        assertNotNull(vectorStoreFile);
    }

    @Test
    void testListVectorStoreFiles() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/vector_store_file__getlist.json");
        var vectorStoreFiles = openAI.vectorStoreFiles().getList("vectorStoreId").join();
        vectorStoreFiles.forEach(System.out::println);
        assertNotNull(vectorStoreFiles);
    }

    @Test
    void deletedVectorStoreFile() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/vector_store_file__delete.json");
        var deletedVectorStoreFile = openAI.vectorStoreFiles().delete("vectorStoreId", "fileId").join();
        System.out.println(deletedVectorStoreFile);
        assertNotNull(deletedVectorStoreFile);
    }

}
