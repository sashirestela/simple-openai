package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.http.HttpRequestData;
import java.net.http.HttpClient;
import java.util.Map;
import java.util.function.UnaryOperator;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class BaseSimpleOpenAiArgs {
    @NonNull
    private final String baseUrl;
    private final Map<String, String> headers;
    private final HttpClient httpClient;
    private final UnaryOperator<HttpRequestData> requestInterceptor;
}
