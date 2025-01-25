package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.websocket.JavaHttpWebSocketAdapter;
import io.github.sashirestela.openai.base.RealtimeConfig;
import io.github.sashirestela.openai.domain.realtime.BaseEvent;
import io.github.sashirestela.openai.domain.realtime.ClientEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OpenAIRealtimeTest {

    @Mock
    private JavaHttpWebSocketAdapter mockWebSocketAdapter;

    private RealtimeConfig realtimeConfig;

    private OpenAIRealtime openAIRealtime;

    @BeforeEach
    void setUp() {
        realtimeConfig = RealtimeConfig.builder()
                .queryParams(Map.of("model", "the-model"))
                .headers(Map.of("api-key", "qwerty"))
                .endpointUrl("wss://test.endpoint.com")
                .webSocketAdapter(mockWebSocketAdapter)
                .build();
        openAIRealtime = new OpenAIRealtime(realtimeConfig);
    }

    @Test
    void testConnect() {
        when(mockWebSocketAdapter.connect(anyString(), anyMap())).thenReturn(CompletableFuture.completedFuture(null));
        CompletableFuture<Void> resultFuture = openAIRealtime.connect();
        assertNotNull(resultFuture);
    }

    @Test
    void testDisconnect() {
        openAIRealtime.disconnect();
        verify(mockWebSocketAdapter).close();
    }

    @Test
    void testSendEvent() {
        when(mockWebSocketAdapter.send(anyString())).thenReturn(CompletableFuture.completedFuture(null));
        BaseEvent testEvent = ClientEvent.ResponseCancel.of("123");
        CompletableFuture<Void> resultFuture = openAIRealtime.send(testEvent);
        assertNotNull(resultFuture);
    }

    @Test
    void testOnOpen() {
        openAIRealtime.onOpen(() -> {
        });
        verify(mockWebSocketAdapter).onOpen(any());
    }

    @Test
    void testOnClose() {
        BiConsumer<Integer, String> closeHandler = (code, reason) -> {
        };
        openAIRealtime.onClose(closeHandler);
        verify(mockWebSocketAdapter).onClose(closeHandler);
    }

    @Test
    void testOnError() {
        Consumer<Throwable> errorHandler = error -> {
        };
        openAIRealtime.onError(errorHandler);
        verify(mockWebSocketAdapter).onError(errorHandler);
    }

    @Test
    void testOnEvent() {
        class TestEvent extends BaseEvent {
        }
        Consumer<TestEvent> testEventHandler = event -> {
        };
        assertDoesNotThrow(() -> openAIRealtime.onEvent(TestEvent.class, testEventHandler));
    }

}
