package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import lombok.Builder;
import lombok.NonNull;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * The Gemini implementation which implements a subset of the standard services.
 */
public class SimpleOpenAIGemini extends BaseSimpleOpenAI {

    /**
     * Constructor used to generate a builder.
     *
     * @param baseUrl        The URL of the Azure OpenAI deployment. Mandatory.
     * @param httpClient     A {@link HttpClient HttpClient} object. One is created by default if not
     *                       provided. Optional.
     * @param apiKeyProvider Provides the API key to be used for authentication. Mandatory.
     * @param objectMapper   Provides Json conversions either to and from objects. Optional.
     */
    @Builder
    public SimpleOpenAIGemini(@NonNull String baseUrl, @NonNull Supplier<String> apiKeyProvider,
            HttpClient httpClient, ObjectMapper objectMapper) {
        super(prepareBaseSimpleOpenAIArgs(baseUrl, apiKeyProvider, httpClient, objectMapper));
    }

    static BaseSimpleOpenAIArgs prepareBaseSimpleOpenAIArgs(String baseUrl,
            Supplier<String> apiKeyProvider,
            HttpClient httpClient,
            ObjectMapper objectMapper) {
        UnaryOperator<HttpRequestData> requestInterceptor = request -> {
            var url = request.getUrl().replace("/v1/", "/");
            request.setUrl(url);

            var headers = request.getHeaders();
            headers.put("Authorization", "Bearer " + apiKeyProvider.get());
            request.setHeaders(headers);
            return request;
        };

        var args = BaseSimpleOpenAIArgs.builder()
                .baseUrl(baseUrl)
                .httpClient(httpClient)
                .headers(Map.of())
                .requestInterceptor(requestInterceptor)
                .objectMapper(objectMapper)
                .build();

        return args;
    }

}
