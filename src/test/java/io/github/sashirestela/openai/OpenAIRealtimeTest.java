package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.support.Configurator;
import io.github.sashirestela.openai.OpenAIRealtime.BaseRealtimeConfig;
import io.github.sashirestela.openai.domain.realtime.BaseEvent;
import io.github.sashirestela.openai.domain.realtime.ClientEvent;
import io.github.sashirestela.openai.domain.realtime.Configuration;
import io.github.sashirestela.openai.domain.realtime.ServerEvent;
import io.github.sashirestela.openai.support.Action;
import io.github.sashirestela.openai.support.Constant;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpenAIRealtimeTest {

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private WebSocket mockWebSocket;

    @Mock
    private WebSocket.Builder mockWebSocketBuilder;

    @Mock
    private Consumer<ServerEvent.SessionUpdated> mockEventHandler;

    @Mock
    private Action mockOpenHandler;

    @Mock
    private BiConsumer<Integer, String> mockCloseHandler;

    @Mock
    private Consumer<Throwable> mockErrorHandler;

    private OpenAIRealtime openAIRealtime;
    private static final String API_KEY = "test-api-key";
    private static final String ENDPOINT_URL = "wss://api.openai.com/v1/realtime";
    private static final String MODEL = "gpt-4o-realtime-preview-2024-10-01";

    @BeforeAll
    static void setup() {
        Configurator.builder().objectMapper(new ObjectMapper()).build();
    }

    @BeforeEach
    void setUp() {
        when(mockHttpClient.newWebSocketBuilder()).thenReturn(mockWebSocketBuilder);
        when(mockWebSocketBuilder.header(anyString(), anyString())).thenReturn(mockWebSocketBuilder);

        openAIRealtime = OpenAIRealtime.builder()
                .httpClient(mockHttpClient)
                .baseRealtimeConfig(BaseRealtimeConfig.builder()
                        .endpointUrl(ENDPOINT_URL)
                        .headers(Map.of(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + API_KEY,
                                Constant.OPENAI_BETA_HEADER, Constant.OPENAI_REALTIME_VERSION))
                        .queryParams(Map.of(Constant.OPENAI_REALTIME_MODEL_NAME, MODEL))
                        .build())
                .build();
    }

    @Test
    void testConnect() {
        // Arrange
        CompletableFuture<WebSocket> future = CompletableFuture.completedFuture(mockWebSocket);
        when(mockWebSocketBuilder.buildAsync(any(URI.class), any(WebSocket.Listener.class)))
                .thenReturn(future);

        // Act
        openAIRealtime.connect();

        // Assert
        verify(mockWebSocketBuilder).header(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + API_KEY);
        verify(mockWebSocketBuilder).header(Constant.OPENAI_BETA_HEADER, Constant.OPENAI_REALTIME_VERSION);
        verify(mockWebSocketBuilder).buildAsync(
                eq(URI.create(ENDPOINT_URL + "?model=" + MODEL)),
                any(WebSocket.Listener.class));
    }

    @Test
    void testSend() {
        // Arrange
        CompletableFuture<WebSocket> future = CompletableFuture.completedFuture(mockWebSocket);
        BaseEvent clientEvent = ClientEvent.ResponseCreate.of("eventId",
                Configuration.builder().instructions("instructions").build());
        when(mockWebSocketBuilder.buildAsync(any(URI.class), any(WebSocket.Listener.class))).thenReturn(future);

        // Act
        openAIRealtime.connect().join();
        openAIRealtime.send(clientEvent);

        // Assert
        verify(mockWebSocket).sendText(anyString(), eq(true));
    }

    @Test
    void testDisconnect() {
        // Arrange
        CompletableFuture<WebSocket> future = CompletableFuture.completedFuture(mockWebSocket);
        when(mockWebSocketBuilder.buildAsync(any(URI.class), any(WebSocket.Listener.class))).thenReturn(future);

        // Act
        openAIRealtime.connect().join();
        openAIRealtime.disconnect();

        // Assert
        verify(mockWebSocket).sendClose(eq(WebSocket.NORMAL_CLOSURE), anyString());
    }

    @Test
    void testEventHandler() {
        // Arrange
        CompletableFuture<WebSocket> future = CompletableFuture.completedFuture(mockWebSocket);
        String testEventJson = "{\"event_id\":\"111\",\"type\":\"session.updated\",\"session\":{\"modalities\":[\"text\"],\"instructions\":\"This is a demo\"}}";
        when(mockWebSocketBuilder.buildAsync(any(URI.class), any(WebSocket.Listener.class))).thenAnswer(invocation -> {
            WebSocket.Listener listener = invocation.getArgument(1);
            listener.onOpen(mockWebSocket);
            listener.onText(mockWebSocket, testEventJson, true);
            return future;
        });
        openAIRealtime.onEvent(ServerEvent.SessionUpdated.class, mockEventHandler);

        // Act
        openAIRealtime.connect().join();

        // Assert
        verify(mockWebSocket, times(2)).request(1);
        verify(mockEventHandler).accept(any(ServerEvent.SessionUpdated.class));
    }

    @Test
    void testOpenHandler() {
        // Arrange
        CompletableFuture<WebSocket> future = CompletableFuture.completedFuture(mockWebSocket);
        when(mockWebSocketBuilder.buildAsync(any(URI.class), any(WebSocket.Listener.class))).thenAnswer(invocation -> {
            WebSocket.Listener listener = invocation.getArgument(1);
            listener.onOpen(mockWebSocket);
            return future;
        });
        openAIRealtime.onOpen(mockOpenHandler);

        // Act
        openAIRealtime.connect().join();

        // Assert
        verify(mockOpenHandler).execute();
    }

    @Test
    void testCloseHandler() {
        // Arrange
        CompletableFuture<WebSocket> future = CompletableFuture.completedFuture(mockWebSocket);
        when(mockWebSocketBuilder.buildAsync(any(URI.class), any(WebSocket.Listener.class))).thenAnswer(invocation -> {
            WebSocket.Listener listener = invocation.getArgument(1);
            listener.onClose(mockWebSocket, 1000, "Closing connection");
            return future;
        });
        openAIRealtime.onClose(mockCloseHandler);

        // Act
        openAIRealtime.connect().join();

        // Assert
        verify(mockCloseHandler).accept(anyInt(), anyString());
    }

    @Test
    void testErroHandler() {
        // Arrange
        CompletableFuture<WebSocket> future = CompletableFuture.completedFuture(mockWebSocket);
        when(mockWebSocketBuilder.buildAsync(any(URI.class), any(WebSocket.Listener.class))).thenAnswer(invocation -> {
            WebSocket.Listener listener = invocation.getArgument(1);
            listener.onError(mockWebSocket, new Throwable("error"));
            return future;
        });
        openAIRealtime.onError(mockErrorHandler);

        // Act
        openAIRealtime.connect().join();

        // Assert
        verify(mockErrorHandler).accept(any(Throwable.class));
    }

}
