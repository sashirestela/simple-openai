package io.github.sashirestela.openai;

import java.net.http.HttpClient;
import java.util.HashMap;
import lombok.Builder;
import lombok.NonNull;

/**
 * The factory that generates implementations of the {@link OpenAI OpenAI}
 * interfaces.
 */

public class SimpleOpenAIAnyscale extends BaseSimpleOpenAI {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_AUTHORIZATION = "Bearer ";

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey         Identifier to be used for authentication. Mandatory.
     * @param baseUrl        Host's url
     * @param httpClient     A {@link java.net.http.HttpClient HttpClient} object.
     *                       One is created by default if not provided. Optional.
     */
    private static BaseSimpleOpenAiArgs prepareBaseSimpleOpenAiArgs(
        String apiKey, String baseUrl, HttpClient httpClient) {

        var headers = new HashMap<String, String>();
        headers.put(AUTHORIZATION_HEADER, BEARER_AUTHORIZATION + apiKey);

        return BaseSimpleOpenAiArgs.builder()
            .baseUrl(baseUrl)
            .headers(headers)
            .httpClient(httpClient)
            .build();
    }

    @Builder
    public SimpleOpenAIAnyscale(
        @NonNull String apiKey,
        @NonNull String baseUrl,
        HttpClient httpClient) {
        super(prepareBaseSimpleOpenAiArgs(apiKey, baseUrl, httpClient));
    }
}
