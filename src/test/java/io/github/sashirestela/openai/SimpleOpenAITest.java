package io.github.sashirestela.openai;

import static io.github.sashirestela.openai.SimpleOpenAI.OPENAI_BASE_URL;
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

import io.github.sashirestela.cleverclient.CleverClient;
import io.github.sashirestela.cleverclient.http.HttpProcessor;
import io.github.sashirestela.cleverclient.util.ReflectUtil;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

class SimpleOpenAITest {

    HttpClient httpClient = mock(HttpClient.class);
    CleverClient cleverClient = mock(CleverClient.class);

    @Nested
    class StandAloneTests {

        @Test
        void shouldSetPropertiesToDefaultValuesWhenBuilderIsCalledWithoutThoseProperties() {
            var openAI = SimpleOpenAI.builder()
                    .apiKey("apiKey")
                    .build();
            assertEquals(HttpClient.Version.HTTP_2, openAI.getHttpClient().version());
            assertNotNull(openAI.getBaseUrl());
            assertNotNull(openAI.getCleverClient());
        }

        @Test
        void shouldSetPropertiesWhenBuilderIsCalledWithThoseProperties() {
            var otherUrl = "https://openai.com/api";
            var openAI = SimpleOpenAI.builder()
                    .apiKey("apiKey")
                    .baseUrl(otherUrl)
                    .httpClient(httpClient)
                    .build();
            assertEquals("apiKey", openAI.getApiKey());
            assertEquals(otherUrl, openAI.getBaseUrl());
            assertEquals(httpClient, openAI.getHttpClient());
        }

        @Test
        void shouldSetBaseUrlWhenBuilderIsCalledWithBaseUrlOnly() {
            var someUrl = "https://exmaple.org/api";
            var openAI = SimpleOpenAI.builder()
                    .baseUrl(someUrl)
                    .build();
            assertEquals(someUrl, openAI.getBaseUrl());
        }

        @Test
        void shouldSetBaseUrlWhenBuilderIsCalledWithUrlBaseOnly() {
            var someUrl = "https://exmaple.org/api";
            var openAI = SimpleOpenAI.builder()
                    .urlBase(someUrl)
                    .build();
            assertEquals(someUrl, openAI.getBaseUrl());
        }

        @Test
        void shouldSetBaseUrlWhenBuilderIsCalledWithBothBaseUrlAndUrlBase() {
            var someUrl = "https://exmaple.org/api";
            var otherUrl = "https://exmaple.org/other-api";
            var openAI = SimpleOpenAI.builder()
                    .baseUrl(someUrl)
                    .urlBase(otherUrl)
                    .build();
            assertEquals(someUrl, openAI.getBaseUrl());
        }

        @Test
        void shouldSetDefaultBaseUrlWhenBuilderIsCalledWithoutBaseUrlOrUrlBase() {
            var openAI = SimpleOpenAI.builder()
                    .build();
            assertEquals(OPENAI_BASE_URL, openAI.getBaseUrl());
        }

        @Test
        void shouldNotAddOrganizationToHeadersWhenBuilderIsCalledWithoutOrganizationId() {
            var openAI = SimpleOpenAI.builder()
                    .apiKey("apiKey")
                    .build();
            assertFalse(openAI.getCleverClient().getHeaders().contains(openAI.getOrganizationId()));
        }

        @Test
        void shouldAddOrganizationToHeadersWhenBuilderIsCalledWithOrganizationId() {
            var openAI = SimpleOpenAI.builder()
                    .apiKey("apiKey")
                    .organizationId("orgId")
                    .build();
            assertTrue(openAI.getCleverClient().getHeaders().contains(openAI.getOrganizationId()));
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
            final var NO_OF_REQUESTS = 2;
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

            var actualRequest = requestCaptor.getAllValues().get(NO_OF_REQUESTS - 1);
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
                    .apiKey("apiKey")
                    .httpClient(httpClient)
                    .build();
            openAI.setCleverClient(cleverClient);
        }

        @Test
        void shouldInstanceAudioServiceOnlyOnceWhenItIsCalledSeveralTimes() {
            when(cleverClient.create(any()))
                    .thenReturn(ReflectUtil.createProxy(
                            OpenAI.Audios.class,
                            HttpProcessor.builder().build()));
            repeat(NUMBER_CALLINGS, () -> openAI.audios());
            verify(cleverClient, times(NUMBER_INVOCATIONS)).create(any());
        }

        @Test
        void shouldInstanceChatCompletionServiceOnlyOnceWhenItIsCalledSeveralTimes() {
            when(cleverClient.create(any()))
                    .thenReturn(ReflectUtil.createProxy(
                            OpenAI.ChatCompletions.class,
                            HttpProcessor.builder().build()));
            repeat(NUMBER_CALLINGS, () -> openAI.chatCompletions());
            verify(cleverClient, times(NUMBER_INVOCATIONS)).create(any());
        }

        @Test
        void shouldInstanceCompletionServiceOnlyOnceWhenItIsCalledSeveralTimes() {
            when(cleverClient.create(any()))
                    .thenReturn(ReflectUtil.createProxy(
                            OpenAI.Completions.class,
                            HttpProcessor.builder().build()));
            repeat(NUMBER_CALLINGS, () -> openAI.completions());
            verify(cleverClient, times(NUMBER_INVOCATIONS)).create(any());
        }

        @Test
        void shouldInstanceEmbeddingServiceOnlyOnceWhenItIsCalledSeveralTimes() {
            when(cleverClient.create(any()))
                    .thenReturn(ReflectUtil.createProxy(
                            OpenAI.Embeddings.class,
                            HttpProcessor.builder().build()));
            repeat(NUMBER_CALLINGS, () -> openAI.embeddings());
            verify(cleverClient, times(NUMBER_INVOCATIONS)).create(any());
        }

        @Test
        void shouldInstanceFilesServiceOnlyOnceWhenItIsCalledSeveralTimes() {
            when(cleverClient.create(any()))
                    .thenReturn(ReflectUtil.createProxy(
                            OpenAI.Files.class,
                            HttpProcessor.builder().build()));
            repeat(NUMBER_CALLINGS, () -> openAI.files());
            verify(cleverClient, times(NUMBER_INVOCATIONS)).create(any());
        }

        @Test
        void shouldInstanceFineTunningServiceOnlyOnceWhenItIsCalledSeveralTimes() {
            when(cleverClient.create(any()))
                    .thenReturn(ReflectUtil.createProxy(
                            OpenAI.FineTunings.class,
                            HttpProcessor.builder().build()));
            repeat(NUMBER_CALLINGS, () -> openAI.fineTunings());
            verify(cleverClient, times(NUMBER_INVOCATIONS)).create(any());
        }

        @Test
        void shouldInstanceImageServiceOnlyOnceWhenItIsCalledSeveralTimes() {
            when(cleverClient.create(any()))
                    .thenReturn(ReflectUtil.createProxy(
                            OpenAI.Images.class,
                            HttpProcessor.builder().build()));
            repeat(NUMBER_CALLINGS, () -> openAI.images());
            verify(cleverClient, times(NUMBER_INVOCATIONS)).create(any());
        }

        @Test
        void shouldInstanceModelsServiceOnlyOnceWhenItIsCalledSeveralTimes() {
            when(cleverClient.create(any()))
                    .thenReturn(ReflectUtil.createProxy(
                            OpenAI.Models.class,
                            HttpProcessor.builder().build()));
            repeat(NUMBER_CALLINGS, () -> openAI.models());
            verify(cleverClient, times(NUMBER_INVOCATIONS)).create(any());
        }

        @Test
        void shouldInstanceModerationServiceOnlyOnceWhenItIsCalledSeveralTimes() {
            when(cleverClient.create(any()))
                    .thenReturn(ReflectUtil.createProxy(
                            OpenAI.Moderations.class,
                            HttpProcessor.builder().build()));
            repeat(NUMBER_CALLINGS, () -> openAI.moderations());
            verify(cleverClient, times(NUMBER_INVOCATIONS)).create(any());
        }
    }

    private static void repeat(int times, Runnable action) {
        for (var i = 0; i < times; i++)
            action.run();
    }
}