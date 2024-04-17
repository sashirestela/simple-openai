package io.github.sashirestela.openai;

import io.github.sashirestela.openai.support.Constant;
import lombok.Builder;
import lombok.NonNull;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Optional;

/**
 * This class provides the implements additional {@link OpenAI OpenAI} interfaces targeting the
 * OpenAI service.
 */
public class SimpleOpenAI extends BaseSimpleOpenAI {

    private OpenAI.Audios audioService;
    private OpenAI.Completions completionService;
    private OpenAI.Embeddings embeddingService;
    private OpenAI.Files fileService;
    private OpenAI.FineTunings fineTuningService;
    private OpenAI.Images imageService;
    private OpenAI.Models modelService;
    private OpenAI.Moderations moderationService;
    private OpenAIBeta.Assistants assistantService;
    private OpenAIBeta.Threads threadService;

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey         Identifier to be used for authentication. Mandatory.
     * @param organizationId Organization's id to be charged for usage. Optional.
     * @param baseUrl        Host's url, If not provided, it'll be 'https://api.openai.com'. Optional.
     * @param httpClient     A {@link java.net.http.HttpClient HttpClient} object. One is created by
     *                       default if not provided. Optional.
     */
    @Builder
    public SimpleOpenAI(@NonNull String apiKey, String organizationId, String baseUrl, HttpClient httpClient) {
        super(prepareBaseSimpleOpenAIArgs(apiKey, organizationId, baseUrl, httpClient));
    }

    public static BaseSimpleOpenAIArgs prepareBaseSimpleOpenAIArgs(String apiKey, String organizationId, String baseUrl,
            HttpClient httpClient) {

        var headers = new HashMap<String, String>();
        headers.put(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + apiKey);
        if (organizationId != null) {
            headers.put(Constant.OPENAI_ORG_HEADER, organizationId);
        }

        return BaseSimpleOpenAIArgs.builder()
                .baseUrl(Optional.ofNullable(baseUrl).orElse(Constant.OPENAI_BASE_URL))
                .headers(headers)
                .httpClient(httpClient)
                .build();
    }

}
