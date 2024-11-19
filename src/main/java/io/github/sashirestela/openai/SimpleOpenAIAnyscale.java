package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.support.Constant;
import lombok.Builder;
import lombok.NonNull;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.Optional;

/**
 * The Anyscale OpenAI implementation which implements a subset of the standard services.
 */
public class SimpleOpenAIAnyscale extends BaseSimpleOpenAI {

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey       Identifier to be used for authentication. Mandatory.
     * @param baseUrl      Host's url. Optional.
     * @param httpClient   A {@link java.net.http.HttpClient HttpClient} object. One is created by
     *                     default if not provided. Optional.
     * @param objectMapper Provides Json conversions either to and from objects. Optional.
     */
    @Builder
    public SimpleOpenAIAnyscale(@NonNull String apiKey, String baseUrl, HttpClient httpClient,
            ObjectMapper objectMapper) {
        super(prepareBaseSimpleOpenAIArgs(apiKey, baseUrl, httpClient, objectMapper));
    }

    public static BaseSimpleOpenAIArgs prepareBaseSimpleOpenAIArgs(String apiKey, String baseUrl,
            HttpClient httpClient, ObjectMapper objectMapper) {
        var headers = Map.of(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + apiKey);

        return BaseSimpleOpenAIArgs.builder()
                .baseUrl(Optional.ofNullable(baseUrl).orElse(Constant.ANYSCALE_BASE_URL))
                .headers(headers)
                .httpClient(httpClient)
                .objectMapper(objectMapper)
                .build();
    }

    /**
     * Throw not implemented
     */
    @Override
    public OpenAI.Files files() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

}
