package io.github.sashirestela.openai.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.completion.CompletionRequest;

public class StreamFilterTest {

  static SimpleOpenAI openAI;
  static HttpClient httpClient = mock(HttpClient.class);

  @BeforeAll
  @SuppressWarnings("unchecked")
  static void setup() {
    openAI = SimpleOpenAI.builder()
        .apiKey("apiKey")
        .httpClient(httpClient)
        .build();
    when(httpClient.sendAsync(any(), any()))
        .thenReturn(CompletableFuture.completedFuture(mock(HttpResponse.class)));
  }

  @Test
  void shouldSetStreamToTrueWhenCallingCreateStreamMethodOfChatService() {
    var chatRequest = ChatRequest.builder()
        .model("test_model")
        .messages(List.of())
        .build();
    openAI.chatCompletions().createStream(chatRequest);
    assertTrue(chatRequest.getStream());
  }

  @Test
  void shouldSetStreamToFalseWhenCallingCreateMethodOfChatService() {
    var chatRequest = ChatRequest.builder()
        .model("test_model")
        .messages(List.of())
        .build();
    openAI.chatCompletions().create(chatRequest);
    assertFalse(chatRequest.getStream());
  }

  @Test
  void shouldSetStreamToTrueWhenCallingCreateStreamMethodOfCompletionService() {
    var completionRequest = CompletionRequest.builder()
        .model("test_model")
        .prompt("prompt text")
        .build();
    openAI.completions().createStream(completionRequest);
    assertTrue(completionRequest.getStream());
  }

  @Test
  void shouldSetStreamToFalseWhenCallingCreateMethodOfCompletionService() {
    var completionRequest = CompletionRequest.builder()
        .model("test_model")
        .prompt("prompt text")
        .build();
    openAI.completions().create(completionRequest);
    assertFalse(completionRequest.getStream());
  }
}