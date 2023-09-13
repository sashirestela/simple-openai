package io.github.sashirestela.openai.domain.embedding;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;

@SuppressWarnings("unchecked")
public class EmbeddingDomainTest {

  @Test
  void testEmbeddingsCreate() throws IOException {
    var httpClient = mock(HttpClient.class);
    var openAI = SimpleOpenAI.builder()
        .apiKey("apiKey")
        .httpClient(httpClient)
        .build();

    HttpResponse<String> httpResponse = mock(HttpResponse.class);
    var jsonResponse = Files.readAllLines(Paths.get("src/test/resources/embeddings_create.json")).get(0);
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn(jsonResponse);

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
