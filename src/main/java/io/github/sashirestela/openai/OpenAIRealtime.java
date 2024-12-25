package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.util.JsonUtil;
import io.github.sashirestela.openai.base.RealtimeConfig;
import io.github.sashirestela.openai.domain.realtime.BaseEvent;
import io.github.sashirestela.openai.support.Action;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.net.http.WebSocket.Listener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class OpenAIRealtime {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIRealtime.class);

    private Map<Class<?>, Consumer<Object>> eventHandlers = new HashMap<>();
    private WebSocket.Builder webSocketWithHeaders;
    private String fullUrl;
    private Action openHandler;
    private BiConsumer<Integer, String> closeHandler;
    private Consumer<Throwable> errorHandler;

    private WebSocket webSocket;

    @Builder
    public OpenAIRealtime(HttpClient httpClient, RealtimeConfig realtimeConfig) {
        this.webSocketWithHeaders = buildWebSocketWithHeaders(httpClient, realtimeConfig);
        this.fullUrl = buildFullUrl(realtimeConfig);
    }

    private WebSocket.Builder buildWebSocketWithHeaders(HttpClient httpClient, RealtimeConfig realtimeConfig) {
        var webSocketBuilder = httpClient.newWebSocketBuilder();
        for (var entry : realtimeConfig.getHeaders().entrySet()) {
            webSocketBuilder = webSocketBuilder.header(entry.getKey(), entry.getValue());
        }
        return webSocketBuilder;
    }

    private String buildFullUrl(RealtimeConfig realtimeConfig) {
        var url = new StringBuilder(realtimeConfig.getEndpointUrl() + "?");
        for (var entry : realtimeConfig.getQueryParams().entrySet()) {
            url.append(entry.getKey() + "=" + entry.getValue());
        }
        return url.toString();
    }

    public <T> void onEvent(Class<T> eventClass, Consumer<T> handler) {
        @SuppressWarnings("unchecked")
        Consumer<Object> genericHandler = (Consumer<Object>) handler;
        this.eventHandlers.put(eventClass, genericHandler);
    }

    public void onOpen(Action openHandler) {
        this.openHandler = openHandler;
    }

    public void onClose(BiConsumer<Integer, String> closeHandler) {
        this.closeHandler = closeHandler;
    }

    public void onError(Consumer<Throwable> errorHandler) {
        this.errorHandler = errorHandler;
    }

    public CompletableFuture<WebSocket> connect() {
        logger.debug("Starting connection");
        return this.webSocketWithHeaders
                .buildAsync(URI.create(this.fullUrl), new OpenAIRealtimeListener())
                .thenApply(ws -> {
                    this.webSocket = ws;
                    return ws;
                });
    }

    public void disconnect() {
        if (this.webSocket != null) {
            logger.debug("Closing connection");
            webSocket.sendClose(WebSocket.NORMAL_CLOSURE, "Closing connection");
        }
    }

    public CompletableFuture<WebSocket> send(BaseEvent clientEvent) {
        var jsonData = JsonUtil.objectToJson(clientEvent);
        logger.debug("Request Event : {}", jsonData);
        return this.webSocket.sendText(jsonData, true);
    }

    private class OpenAIRealtimeListener implements Listener {

        private StringBuilder dataBuffer = new StringBuilder();

        @Override
        public void onOpen(WebSocket webSocket) {
            logger.debug("Connection started");
            if (OpenAIRealtime.this.openHandler != null) {
                OpenAIRealtime.this.openHandler.execute();
            }
            webSocket.request(1);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
            dataBuffer.append(data);
            if (last) {
                var jsonData = dataBuffer.toString();
                logger.debug("Response Event : {}", jsonData);
                dataBuffer = new StringBuilder();
                var event = JsonUtil.jsonToObject(jsonData, BaseEvent.class);
                Consumer<Object> handler = OpenAIRealtime.this.eventHandlers.get(event.getClass());
                if (handler != null) {
                    handler.accept(event);
                }

            }
            webSocket.request(1);
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
            logger.debug("Connection closed");
            if (OpenAIRealtime.this.closeHandler != null) {
                OpenAIRealtime.this.closeHandler.accept(statusCode, reason);
            }
            return CompletableFuture.completedFuture(null);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            var errorMessage = error.getMessage();
            if (error.getCause() != null) {
                errorMessage += "\n" + error.getCause().getMessage();
            }
            logger.debug("Connection error : {}", errorMessage);
            if (OpenAIRealtime.this.errorHandler != null) {
                OpenAIRealtime.this.errorHandler.accept(error);
            }
        }

    }

}
