package io.github.sashirestela.openai.domain.embedding;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.domain.DomainTestingHelper;

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

    @Test
    void shouldCreateCompletionRequestWhenInputIsRightClass() {
        Object[] testData = {
                "demo",
                List.of("first", "second"),
                List.of(1, 2, 3, 4),
                List.of(List.of(11, 22, 33))
        };
        for (Object data : testData) {
            var embeddingRequestBuilder = EmbeddingRequest.builder()
                    .model("model")
                    .input(data);
            assertDoesNotThrow(() -> embeddingRequestBuilder.build());
        }
    }

    @Test
    void shouldThrownExceptionWhenCreatingCompletionRequestWithInputWrongClass() {
        Object[] testData = {
                1001,
                List.of(17.65, 23.68),
                List.of(List.of("first", "second"))
        };
        for (Object data : testData) {
            var embeddingRequestBuilder = EmbeddingRequest.builder()
                    .model("model")
                    .input(data);
            var exception = assertThrows(SimpleUncheckedException.class, () -> embeddingRequestBuilder.build());
            var actualErrorMessage = exception.getMessage();
            var expectedErrorMessge = "The field input must be String or List<String> or List<Integer> or List<List<Integer>> classes.";
            assertEquals(expectedErrorMessge, actualErrorMessage);
        }
    }
}