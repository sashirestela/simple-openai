package io.github.sashirestela.openai.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.client.HttpClientAdapter;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.http.HttpResponseData;
import io.github.sashirestela.cleverclient.retry.RetryConfig;
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
    private final UnaryOperator<HttpRequestData> requestInterceptor;
    private final UnaryOperator<HttpResponseData> responseInterceptor;
    private final RetryConfig retryConfig;
    private final HttpClientAdapter clientAdapter;
    private final ObjectMapper objectMapper;
    private final RealtimeConfig realtimeConfig;
    // @deprecated CleverClient has deprecated this field in favor of clientAdapter.
    @Deprecated(since = "3.16.0", forRemoval = true)
    private final HttpClient httpClient;

}
