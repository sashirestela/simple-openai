package io.github.sashirestela.openai.base;

import io.github.sashirestela.cleverclient.websocket.WebSocketAdapter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class RealtimeConfig {

    private String model;
    private String endpointUrl;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
    private WebSocketAdapter webSocketAdapter;

    public static RealtimeConfig of(String model) {
        return new RealtimeConfig(model, null, null, null, null);
    }

    public static RealtimeConfig of(String model, WebSocketAdapter webSocketAdapter) {
        return new RealtimeConfig(model, null, null, null, webSocketAdapter);
    }

}
