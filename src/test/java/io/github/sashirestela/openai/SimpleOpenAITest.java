package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.CleverClient;
import io.github.sashirestela.openai.SimpleOpenAI.RealtimeConfig;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.support.Constant;
import io.github.sashirestela.slimvalidator.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SimpleOpenAITest {

    HttpClient httpClient = mock(HttpClient.class);
    CleverClient cleverClient = mock(CleverClient.class);

    @Test
    void shouldPrepareBaseOpenSimpleAIArgsCorrectly() {
        var args = SimpleOpenAI.prepareBaseSimpleOpenAIArgs("the-api-key", "orgId", "prjId", "https://example.org",
                HttpClient.newHttpClient(), new ObjectMapper(), RealtimeConfig.of("the-model"));

        assertEquals("https://example.org", args.getBaseUrl());
        assertEquals(3, args.getHeaders().size());
        assertEquals(Constant.BEARER_AUTHORIZATION + "the-api-key",
                args.getHeaders().get(Constant.AUTHORIZATION_HEADER));
        assertEquals("orgId", args.getHeaders().get(Constant.OPENAI_ORG_HEADER));
        assertEquals("prjId", args.getHeaders().get(Constant.OPENAI_PRJ_HEADER));
        assertNotNull(args.getHttpClient());
        assertNotNull(args.getObjectMapper());
        assertNotNull(args.getBaseRealtimeConfig());
        assertNull(args.getRequestInterceptor());
    }

    @Test
    void shouldPrepareBaseOpenSimpleAIArgsCorrectlyWithOnlyApiKey() {
        var args = SimpleOpenAI.prepareBaseSimpleOpenAIArgs("the-api-key", null, null, null, null, null, null);

        assertEquals(Constant.OPENAI_BASE_URL, args.getBaseUrl());
        assertEquals(1, args.getHeaders().size());
        assertEquals(Constant.BEARER_AUTHORIZATION + "the-api-key",
                args.getHeaders().get(Constant.AUTHORIZATION_HEADER));
        assertNull(args.getHttpClient());
        assertNull(args.getObjectMapper());
        assertNull(args.getBaseRealtimeConfig());
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
                    .message(UserMessage.of("prompt"))
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
    void shouldInstanceServiceOnlyOnceWhenItIsCalledSeveralTimes() {
        final int NUMBER_CALLINGS = 3;
        final int NUMBER_INVOCATIONS = 1;

        var openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
        openAI.setCleverClient(cleverClient);

        TestData[] data = {
                new TestData(OpenAI.Audios.class, openAI::audios),
                new TestData(OpenAI.Batches.class, openAI::batches),
                new TestData(OpenAI.ChatCompletions.class, openAI::chatCompletions),
                new TestData(OpenAI.Completions.class, openAI::completions),
                new TestData(OpenAI.Embeddings.class, openAI::embeddings),
                new TestData(OpenAI.Files.class, openAI::files),
                new TestData(OpenAI.FineTunings.class, openAI::fineTunings),
                new TestData(OpenAI.Images.class, openAI::images),
                new TestData(OpenAI.Models.class, openAI::models),
                new TestData(OpenAI.Moderations.class, openAI::moderations),
                new TestData(OpenAI.Uploads.class, openAI::uploads),
                new TestData(OpenAIBeta2.Assistants.class, openAI::assistants),
                new TestData(OpenAIBeta2.Threads.class, openAI::threads),
                new TestData(OpenAIBeta2.ThreadMessages.class, openAI::threadMessages),
                new TestData(OpenAIBeta2.ThreadRuns.class, openAI::threadRuns),
                new TestData(OpenAIBeta2.ThreadRunSteps.class, openAI::threadRunSteps),
                new TestData(OpenAIBeta2.VectorStores.class, openAI::vectorStores),
                new TestData(OpenAIBeta2.VectorStoreFiles.class, openAI::vectorStoreFiles),
                new TestData(OpenAIBeta2.VectorStoreFileBatches.class, openAI::vectorStoreFileBatches)
        };
        for (TestData testData : data) {
            when(cleverClient.create(any()))
                    .thenReturn(createProxy(testData.serviceClass));
            repeat(NUMBER_CALLINGS, testData.calling);
            verify(cleverClient, times(NUMBER_INVOCATIONS)).create(testData.serviceClass);
        }
    }

    @Test
    void shouldThrownExceptionWhenBodyRequestFailsValidations() {
        var openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
        var chatService = openAI.chatCompletions();
        var chatRequest = ChatRequest.builder()
                .model("big-model")
                .toolChoice("ToolChoice")
                .stop(List.of("one", "two", "three", "four", "five"))
                .build();
        var exception = assertThrows(ConstraintViolationException.class, () -> chatService.create(chatRequest));
        var expectedErrorMessage = "messages must have a value.\n"
                + "stop type must be or String or Collection<String> (max 4 items).\n"
                + "toolChoice type must be or ToolChoiceOption or ToolChoice.";
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

    @Test
    void shouldInstanceRealtimeWhenMinimalConfigIsPassed() {
        var openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .realtimeConfig(RealtimeConfig.of("realtime_model"))
                .build();
        assertNotNull(openAI.realtime());
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
