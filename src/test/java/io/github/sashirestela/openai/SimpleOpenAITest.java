package io.github.sashirestela.openai;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.http.HttpInvocationHandler;
import io.github.sashirestela.openai.http.HttpProcessor;
import io.github.sashirestela.openai.support.ReflectUtil;

public class SimpleOpenAITest {

  HttpClient httpClient = mock(HttpClient.class);
  HttpProcessor httpProcessor = mock(HttpProcessor.class);

  @Nested
  class StandAloneTests {

    @Test
    void shouldSetPropertiesToDefaultValuesWhenBuilderIsCalledWithoutThoseProperties() {
      SimpleOpenAI openAI = SimpleOpenAI.builder()
          .apiKey("apiLKey")
          .build();
      assertEquals(SimpleOpenAI.OPENAI_URL_BASE, openAI.getUrlBase());
      assertEquals(HttpClient.Version.HTTP_2, openAI.getHttpClient().version());
      assertNotNull(openAI.getHttpProcessor());
    }

    @Test
    void shouldSetPropertiesWhenBuilderIsCalledWithThoseProperties() {
      String otherUrl = "https://openai.com/api";
      SimpleOpenAI openAI = SimpleOpenAI.builder()
          .apiKey("apiLKey")
          .urlBase(otherUrl)
          .httpClient(httpClient)
          .build();
      assertEquals(otherUrl, openAI.getUrlBase());
      assertEquals(httpClient, openAI.getHttpClient());
    }

    @Test
    void shouldNotAddOrganizationToHeadersWhenBuilderIsCalledWithoutOrganizationId() {
      SimpleOpenAI openAI = SimpleOpenAI.builder()
          .apiKey("apiLKey")
          .build();
      assertFalse(openAI.getHttpProcessor().getHeaders().contains(openAI.getOrganizationId()));
    }

    @Test
    void shouldAddOrganizationToHeadersWhenBuilderIsCalledWithOrganizationId() {
      SimpleOpenAI openAI = SimpleOpenAI.builder()
          .apiKey("apiLKey")
          .organizationId("orgId")
          .build();
      assertTrue(openAI.getHttpProcessor().getHeaders().contains(openAI.getOrganizationId()));
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldNotDuplicateContentTypeHeaderWhenCallingSimpleOpenAI() {
      var chatService = SimpleOpenAI.builder()
          .apiKey("apiKey")
          .httpClient(httpClient)
          .build()
          .chatCompletions();

      // When
      final int NO_OF_REQUESTS = 2;
      when(httpClient.sendAsync(any(), any()))
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
      assertEquals(1, actualRequest.headers().allValues("Content-Type").size(),
          "Contains Content-Type header exactly once");
    }
  }

  @Nested
  class InitializedTests {
    final int NUMBER_CALLINGS = 3;
    final int NUMBER_INVOCATIONS = 1;

    SimpleOpenAI openAI;

    @BeforeEach
    void init() {
      openAI = SimpleOpenAI.builder()
          .apiKey("apiLKey")
          .httpClient(httpClient)
          .build();
      openAI.setHttpProcessor(httpProcessor);
    }

    @Test
    void shouldInstanceAudioServiceOnlyOnceWhenItIsCalledSeveralTimes() {
      when(httpProcessor.create(any(), any()))
          .thenReturn(ReflectUtil.get().createProxy(
              OpenAI.Audios.class,
              new HttpInvocationHandler(null, null)));
      repeat(NUMBER_CALLINGS, () -> openAI.audios());
      verify(httpProcessor, times(NUMBER_INVOCATIONS)).create(any(), any());
    }

    @Test
    void shouldInstanceChatCompletionServiceOnlyOnceWhenItIsCalledSeveralTimes() {
      when(httpProcessor.create(any(), any()))
          .thenReturn(ReflectUtil.get().createProxy(
              OpenAI.ChatCompletions.class,
              new HttpInvocationHandler(null, null)));
      repeat(NUMBER_CALLINGS, () -> openAI.chatCompletions());
      verify(httpProcessor, times(NUMBER_INVOCATIONS)).create(any(), any());
    }

    @Test
    void shouldInstanceCompletionServiceOnlyOnceWhenItIsCalledSeveralTimes() {
      when(httpProcessor.create(any(), any()))
          .thenReturn(ReflectUtil.get().createProxy(
              OpenAI.Completions.class,
              new HttpInvocationHandler(null, null)));
      repeat(NUMBER_CALLINGS, () -> openAI.completions());
      verify(httpProcessor, times(NUMBER_INVOCATIONS)).create(any(), any());
    }

    @Test
    void shouldInstanceEmbeddingServiceOnlyOnceWhenItIsCalledSeveralTimes() {
      when(httpProcessor.create(any(), any()))
          .thenReturn(ReflectUtil.get().createProxy(
              OpenAI.Embeddings.class,
              new HttpInvocationHandler(null, null)));
      repeat(NUMBER_CALLINGS, () -> openAI.embeddings());
      verify(httpProcessor, times(NUMBER_INVOCATIONS)).create(any(), any());
    }

    @Test
    void shouldInstanceFilesServiceOnlyOnceWhenItIsCalledSeveralTimes() {
      when(httpProcessor.create(any(), any()))
          .thenReturn(ReflectUtil.get().createProxy(
              OpenAI.Files.class,
              new HttpInvocationHandler(null, null)));
      repeat(NUMBER_CALLINGS, () -> openAI.files());
      verify(httpProcessor, times(NUMBER_INVOCATIONS)).create(any(), any());
    }

    @Test
    void shouldInstanceFineTunningServiceOnlyOnceWhenItIsCalledSeveralTimes() {
      when(httpProcessor.create(any(), any()))
          .thenReturn(ReflectUtil.get().createProxy(
              OpenAI.FineTunings.class,
              new HttpInvocationHandler(null, null)));
      repeat(NUMBER_CALLINGS, () -> openAI.fineTunings());
      verify(httpProcessor, times(NUMBER_INVOCATIONS)).create(any(), any());
    }

    @Test
    void shouldInstanceImageServiceOnlyOnceWhenItIsCalledSeveralTimes() {
      when(httpProcessor.create(any(), any()))
          .thenReturn(ReflectUtil.get().createProxy(
              OpenAI.Images.class,
              new HttpInvocationHandler(null, null)));
      repeat(NUMBER_CALLINGS, () -> openAI.images());
      verify(httpProcessor, times(NUMBER_INVOCATIONS)).create(any(), any());
    }

    @Test
    void shouldInstanceModelsServiceOnlyOnceWhenItIsCalledSeveralTimes() {
      when(httpProcessor.create(any(), any()))
          .thenReturn(ReflectUtil.get().createProxy(
              OpenAI.Models.class,
              new HttpInvocationHandler(null, null)));
      repeat(NUMBER_CALLINGS, () -> openAI.models());
      verify(httpProcessor, times(NUMBER_INVOCATIONS)).create(any(), any());
    }

    @Test
    void shouldInstanceModerationServiceOnlyOnceWhenItIsCalledSeveralTimes() {
      when(httpProcessor.create(any(), any()))
          .thenReturn(ReflectUtil.get().createProxy(
              OpenAI.Moderations.class,
              new HttpInvocationHandler(null, null)));
      repeat(NUMBER_CALLINGS, () -> openAI.moderations());
      verify(httpProcessor, times(NUMBER_INVOCATIONS)).create(any(), any());
    }
  }

  private static void repeat(int times, Runnable action) {
    for (int i = 0; i < times; i++)
      action.run();
  }
}