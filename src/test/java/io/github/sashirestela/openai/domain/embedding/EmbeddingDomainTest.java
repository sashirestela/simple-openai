package io.github.sashirestela.openai.domain.embedding;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class EmbeddingDomainTest {

    @Test
    void testEmbeddingsCreate() throws IOException {
        var httpClient = mock(HttpClient.class);
        var openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/embeddings_create_float.json");
        var embeddingRequest = EmbeddingRequest.builder()
                .model("text-embedding-ada-002")
                .input(Arrays.asList("shiny sun", "blue sky"))
                .user("test")
                .build();
        var embeddingResponse = openAI.embeddings().create(embeddingRequest).join();
        System.out.println(embeddingResponse);
        assertNotNull(embeddingResponse);
    }

    @Test
    void testEmbeddingsCreateBase64() throws IOException {
        var httpClient = mock(HttpClient.class);
        var openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/embeddings_create_base64.json");
        var embeddingRequest = EmbeddingRequest.builder()
                .model("text-embedding-ada-002")
                .input(Arrays.asList("shiny sun", "blue sky"))
                .user("test")
                .build();
        var embeddingResponse = openAI.embeddings().createBase64(embeddingRequest).join();
        System.out.println(embeddingResponse);
        assertNotNull(embeddingResponse);
    }

}
