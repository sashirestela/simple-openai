package io.github.sashirestela.openai;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.Optional;

import io.github.sashirestela.openai.support.Constant;
import lombok.Builder;
import lombok.NonNull;

/**
 * This class provides the chatCompletion() service for the Anyscale provider
 */
public class SimpleOpenAIAnyscale extends BaseSimpleOpenAI {

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey     Identifier to be used for authentication. Mandatory.
     * @param baseUrl    Host's url. Optional.
     * @param httpClient A {@link java.net.http.HttpClient HttpClient} object.
     *                   One is created by default if not provided. Optional.
     */
    @Builder
    public SimpleOpenAIAnyscale(@NonNull String apiKey, String baseUrl, HttpClient httpClient) {
        super(prepareBaseSimpleOpenAIArgs(apiKey, baseUrl, httpClient));
    }

    public static BaseSimpleOpenAIArgs prepareBaseSimpleOpenAIArgs(String apiKey, String baseUrl,
            HttpClient httpClient) {
        var headers = Map.of(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + apiKey);

        return BaseSimpleOpenAIArgs.builder()
                .baseUrl(Optional.ofNullable(baseUrl).orElse(Constant.ANYSCALE_BASE_URL))
                .headers(headers)
                .httpClient(httpClient)
                .build();
    }
}