package io.github.sashirestela.openai;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.chat.ChatRequest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;

public class SimpleOpenAITest {

  @Mock
  HttpClient httpClient = mock(HttpClient.class);

  @Test
  void shouldCreateAnIstanceOfAudioServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.Audios audioService = simpleOpenAI.audios();
    assertNotNull(audioService);
  }

  @Test
  void shouldCreateAnInstanceOfChatServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.ChatCompletions chatService = simpleOpenAI.chatCompletions();
    assertNotNull(chatService);
  }

  @Test
  void shouldNotDuplicateContentTypeHeaderWhenCallingSimpleOpenAI() {
    var chatService = SimpleOpenAI.builder()
            .apiKey("apiKey")
            .httpClient(httpClient)
            .build()
            .chatCompletions();

    // When
    final int NO_OF_REQUESTS = 2;
    Mockito.when(httpClient.sendAsync(any(), any()))
            .thenReturn(completedFuture(mock(HttpResponse.class)));

    repeat(NO_OF_REQUESTS, () -> {
      var chatRequest = ChatRequest.builder()
              .model("model")
              .messages(List.of())
              .build();
      chatService.create(chatRequest);
    });

    // Then
    ArgumentCaptor<HttpRequest> requestCaptor = ArgumentCaptor.forClass(HttpRequest.class);
    verify(httpClient, times(NO_OF_REQUESTS))
            .sendAsync(requestCaptor.capture(), any());

    HttpRequest actualRequest = requestCaptor.getAllValues().get(NO_OF_REQUESTS - 1);
    assertEquals(1, actualRequest.headers().allValues("Content-Type").size(), "Contains Content-Type header exactly once");
  }

  @Test
  void shouldCreateAnInstanceOfCompletionServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.Completions completionService = simpleOpenAI.completions();
    assertNotNull(completionService);
  }

  @Test
  void shouldCreateAnInstanceOfEmbeddingServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.Embeddings embeddingService = simpleOpenAI.embeddings();
    assertNotNull(embeddingService);
  }

  @Test
  void shouldCreateAnInstanceOfFileServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.Files fileService = simpleOpenAI.files();
    assertNotNull(fileService);
  }

  @Test
  void shouldCreateAnInstanceOfImageServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.Images imageService = simpleOpenAI.images();
    assertNotNull(imageService);
  }

  @Test
  void shouldCreateAnInstanceOfFineTuningServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.FineTunings fineTuningService = simpleOpenAI.fineTunings();
    assertNotNull(fineTuningService);
  }

  @Test
  void shouldCreateAnInstanceOfModelServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").httpClient(httpClient).build();
    OpenAI.Models modelService = simpleOpenAI.models();
    assertNotNull(modelService);
  }

  @Test
  void shouldCreateAnInstanceOfModerationServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").httpClient(httpClient).build();
    OpenAI.Moderations moderationService = simpleOpenAI.moderations();
    assertNotNull(moderationService);
  }

  private static void repeat(int times, Runnable action) {
    for (int i = 0; i < times; i++)
      action.run();
  }
}
