package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.CleverClient;
import io.github.sashirestela.cleverclient.util.JsonUtil;
import io.github.sashirestela.cleverclient.websocket.Action;
import io.github.sashirestela.cleverclient.websocket.JavaHttpWebSocketAdapter;
import io.github.sashirestela.openai.base.RealtimeConfig;
import io.github.sashirestela.openai.domain.realtime.BaseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class OpenAIRealtime {

    private static final Logger logger = LoggerFactory.getLogger(OpenAIRealtime.class);

    private final CleverClient.WebSocket webSocket;
    private Map<Class<?>, Consumer<Object>> eventHandlers = new HashMap<>();

    public OpenAIRealtime(RealtimeConfig realtimeConfig) {
        webSocket = CleverClient.WebSocket.builder()
                .baseUrl(realtimeConfig.getEndpointUrl())
                .queryParams(realtimeConfig.getQueryParams())
                .headers(realtimeConfig.getHeaders())
                .webSockewAdapter(Optional.ofNullable(realtimeConfig.getWebSocketAdapter())
                        // Lazy evaluation to not fail on devices without support for HttpClient
                        .orElseGet(() -> new JavaHttpWebSocketAdapter()))
                .build();

        webSocket.onMessage(message -> {
            logger.debug("Response Event : {}", message);
            var event = JsonUtil.jsonToObject(message, BaseEvent.class);
            Consumer<Object> handler = eventHandlers.get(event.getClass());
            if (handler != null) {
                handler.accept(event);
            }
        });
    }

    public <T> void onEvent(Class<T> eventClass, Consumer<T> handler) {
        @SuppressWarnings("unchecked")
        Consumer<Object> genericHandler = (Consumer<Object>) handler;
        this.eventHandlers.put(eventClass, genericHandler);
    }

    public void onOpen(Action openHandler) {
        this.webSocket.onOpen(openHandler);
    }

    public void onClose(BiConsumer<Integer, String> closeHandler) {
        this.webSocket.onClose(closeHandler);
    }

    public void onError(Consumer<Throwable> errorHandler) {
        this.webSocket.onError(errorHandler);
    }

    public CompletableFuture<Void> connect() {
        logger.debug("Starting connection");
        return this.webSocket.connect();
    }

    public void disconnect() {
        logger.debug("Closing connection");
        this.webSocket.close();
    }

    public CompletableFuture<Void> send(BaseEvent clientEvent) {
        var jsonData = JsonUtil.objectToJson(clientEvent);
        logger.debug("Request Event : {}", jsonData);
        return this.webSocket.send(jsonData);
    }

}
