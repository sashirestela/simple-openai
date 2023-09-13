package io.github.sashirestela.openai.domain.completion;

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
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;

@SuppressWarnings("unchecked")
public class CompletionDomainTest {

  static HttpClient httpClient;
  static SimpleOpenAI openAI;
  static CompletionRequest completionRequest;

  @BeforeAll
  static void setup() {
    httpClient = mock(HttpClient.class);
    openAI = SimpleOpenAI.builder()
        .apiKey("apiKey")
        .httpClient(httpClient)
        .build();
    completionRequest = CompletionRequest.builder()
        .model("text-davinci-003")
        .prompt("Write a technical article about ChatGPT, no more than 100 words.")
        .suffix(null)
        .temperature(0.0)
        .maxTokens(300)
        .topP(1.0)
        .n(1)
        .logprobs(0)
        .echo(false)
        .stop(null)
        .presencePenalty(0.0)
        .frequencyPenalty(0.0)
        .bestOf(1)
        .logitBias(null)
        .user("test")
        .build();
  }

  @Test
  void testCompletionsCreateStream() throws IOException {
    HttpResponse<Stream<String>> httpResponse = mock(HttpResponse.class);
    var listResponse = Files.readAllLines(Paths.get("src/test/resources/completions_create_stream.txt"));
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofLines().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn(listResponse.stream());

    var completionResponse = openAI.completions().createStream(completionRequest).join();
    completionResponse.filter(chatResp -> chatResp.firstText() != null)
        .map(chatResp -> chatResp.firstText())
        .forEach(System.out::print);
    assertNotNull(completionResponse);
  }

  @Test
  void testCompletionsCreate() throws IOException {
    HttpResponse<String> httpResponse = mock(HttpResponse.class);
    var jsonResponse = Files.readAllLines(Paths.get("src/test/resources/completions_create.json")).get(0);
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn(jsonResponse);

    var completionResponse = openAI.completions().create(completionRequest).join();
    System.out.println(completionResponse);
    assertNotNull(completionResponse);
  }
}