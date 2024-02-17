package io.github.sashirestela.openai;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Optional;
import lombok.Builder;
import lombok.NonNull;

/**
 * This class provides the chatCompletion() service for the Anyscale provider
 */
public class SimpleOpenAIAnyscale extends BaseSimpleOpenAI {
    public static final String DEFAULT_BASE_URL = "https://api.endpoints.anyscale.com";

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_AUTHORIZATION = "Bearer ";

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey         Identifier to be used for authentication. Mandatory.
     * @param baseUrl        Host's url
     * @param httpClient     A {@link java.net.http.HttpClient HttpClient} object.
     *                       One is created by default if not provided. Optional.
     */
    public static BaseSimpleOpenAIArgs prepareBaseSimpleOpenAIArgs(String apiKey, String baseUrl, HttpClient httpClient) {
        baseUrl = Optional.ofNullable(baseUrl).orElse(DEFAULT_BASE_URL);
        var headers = new HashMap<String, String>();
        headers.put(AUTHORIZATION_HEADER, BEARER_AUTHORIZATION + apiKey);

        return BaseSimpleOpenAIArgs.builder()
            .baseUrl(baseUrl)
            .headers(headers)
            .httpClient(httpClient)
            .build();
    }

    @Builder
    public SimpleOpenAIAnyscale(
        @NonNull String apiKey,
        String baseUrl,
        HttpClient httpClient) {
        super(prepareBaseSimpleOpenAIArgs(apiKey, baseUrl, httpClient));
    }
}
