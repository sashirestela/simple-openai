package io.github.sashirestela.openai.domain.embedding;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;

public class EmbeddingDomainTest {

  @Test
  void testEmbeddingsCreate() throws IOException {
    var httpClient = mock(HttpClient.class);
    var openAI = SimpleOpenAI.builder()
        .apiKey("apiKey")
        .httpClient(httpClient)
        .build();
    DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/embeddings_create.json");
    var embeddingRequest = EmbeddingRequest.builder()
        .model("text-embedding-ada-002")
        .input(Arrays.asList("it is rainnig cats and dogs"))
        .user("test")
        .build();
    var embeddingResponse = openAI.embeddings().create(embeddingRequest).join();
    System.out.println(embeddingResponse);
    assertNotNull(embeddingResponse);

  }
}
