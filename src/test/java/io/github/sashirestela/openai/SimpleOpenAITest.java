package io.github.sashirestela.openai;

import static io.github.sashirestela.openai.SimpleOpenAI.AUTHORIZATION_HEADER;
import static io.github.sashirestela.openai.SimpleOpenAI.BEARER_AUTHORIZATION;
import static io.github.sashirestela.openai.SimpleOpenAI.ORGANIZATION_HEADER;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import io.github.sashirestela.cleverclient.CleverClient;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

class SimpleOpenAITest {

    HttpClient httpClient = mock(HttpClient.class);
    CleverClient cleverClient = mock(CleverClient.class);

    @Test
    void shouldPrepareBaseOpenSimpleAIArgsCorrectly() {
        var args = SimpleOpenAI.prepareBaseSimpleOpenAIArgs(
            "the-api-key",
            "orgId",
            "https://example.org",
            HttpClient.newHttpClient());

        assertEquals("https://example.org", args.getBaseUrl());
        assertEquals(2, args.getHeaders().size());
        assertEquals(BEARER_AUTHORIZATION + "the-api-key", args.getHeaders().get(AUTHORIZATION_HEADER));
        assertEquals("orgId", args.getHeaders().get(ORGANIZATION_HEADER));
        assertNotNull(args.getHttpClient());

        // No request interceptor for SimpleOpenAI
        assertNull(args.getRequestInterceptor());
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

    @Test
    void shouldInstanceServiceOnlyOnceWhenItIsCalledSeverlaTimes() {
        final int NUMBER_CALLINGS = 3;
        final int NUMBER_INVOCATIONS = 1;

        SimpleOpenAI openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
        openAI.setCleverClient(cleverClient);

        TestData[] data = {
                new TestData(OpenAI.Audios.class, openAI::audios),
                new TestData(OpenAI.ChatCompletions.class, openAI::chatCompletions),
                new TestData(OpenAI.Completions.class, openAI::completions),
                new TestData(OpenAI.Embeddings.class, openAI::embeddings),
                new TestData(OpenAI.Files.class, openAI::files),
                new TestData(OpenAI.FineTunings.class, openAI::fineTunings),
                new TestData(OpenAI.Images.class, openAI::images),
                new TestData(OpenAI.Models.class, openAI::models),
                new TestData(OpenAI.Moderations.class, openAI::moderations),
                new TestData(OpenAI.Assistants.class, openAI::assistants),
                new TestData(OpenAI.Threads.class, openAI::threads)
        };
        for (TestData testData : data) {
            when(cleverClient.create(any()))
                    .thenReturn(createProxy(testData.serviceClass));
            repeat(NUMBER_CALLINGS, testData.calling);
            verify(cleverClient, times(NUMBER_INVOCATIONS)).create(testData.serviceClass);
        }
    }

    private static void repeat(int times, Runnable action) {
        for (var i = 0; i < times; i++)
            action.run();
    }

    static class TestData {
        private Class<?> serviceClass;
        private Runnable calling;

        public TestData(Class<?> serviceClass, Runnable calling) {
            this.serviceClass = serviceClass;
            this.calling = calling;
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T createProxy(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[] { interfaceClass },
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        throw new UnsupportedOperationException("Unimplemented method 'invoke'");
                    }
                });
    }
}