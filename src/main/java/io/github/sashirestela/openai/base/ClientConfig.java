package io.github.sashirestela.openai.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.function.UnaryOperator;

@Getter
@Builder
public class ClientConfig {

    @NonNull
    private final String baseUrl;
    private final Map<String, String> headers;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequestData> requestInterceptor;
    private final ObjectMapper objectMapper;
    private final RealtimeConfig realtimeConfig;

}
