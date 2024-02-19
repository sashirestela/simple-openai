package io.github.sashirestela.openai;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Optional;

import io.github.sashirestela.openai.support.Constant;
import lombok.Builder;
import lombok.NonNull;

/**
 * This class provides the implements additional {@link OpenAI OpenAI}
 * interfaces targeting the OpenAI service.
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
    private OpenAI.Assistants assistantService;
    private OpenAI.Threads threadService;

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey         Identifier to be used for authentication. Mandatory.
     * @param organizationId Organization's id to be charged for usage. Optional.
     * @param baseUrl        Host's url, If not provided, it'll be
     *                       'https://api.openai.com'. Optional.
     * @param httpClient     A {@link java.net.http.HttpClient HttpClient} object.
     *                       One is created by default if not provided. Optional.
     */
    @Builder
    public SimpleOpenAI(@NonNull String apiKey, String organizationId, String baseUrl, HttpClient httpClient) {
        super(prepareBaseSimpleOpenAIArgs(apiKey, organizationId, baseUrl, httpClient));
    }

    public static BaseSimpleOpenAIArgs prepareBaseSimpleOpenAIArgs(
            String apiKey, String organizationId, String baseUrl, HttpClient httpClient) {

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

    /**
     * Generates an implementation of the Audios interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Audios audios() {
        if (audioService == null) {
            audioService = cleverClient.create(OpenAI.Audios.class);
        }
        return audioService;
    }

    /**
     * Generates an implementation of the Completions interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Completions completions() {
        if (completionService == null) {
            completionService = cleverClient.create(OpenAI.Completions.class);
        }
        return completionService;
    }

    /**
     * Generates an implementation of the Embeddings interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */

    public OpenAI.Embeddings embeddings() {
        if (embeddingService == null) {
            embeddingService = cleverClient.create(OpenAI.Embeddings.class);
        }
        return embeddingService;
    }

    /**
     * Generates an implementation of the Files interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Files files() {
        if (fileService == null) {
            fileService = cleverClient.create(OpenAI.Files.class);
        }
        return fileService;
    }

    /**
     * Generates an implementation of the FineTunings interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.FineTunings fineTunings() {
        if (fineTuningService == null) {
            fineTuningService = cleverClient.create(OpenAI.FineTunings.class);
        }
        return fineTuningService;
    }

    /**
     * Generates an implementation of the Images interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Images images() {
        if (imageService == null) {
            imageService = cleverClient.create(OpenAI.Images.class);
        }
        return imageService;
    }

    /**
     * Generates an implementation of the Models interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Models models() {
        if (modelService == null) {
            modelService = cleverClient.create(OpenAI.Models.class);
        }
        return modelService;
    }

    /**
     * Generates an implementation of the Moderations interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Moderations moderations() {
        if (moderationService == null) {
            moderationService = cleverClient.create(OpenAI.Moderations.class);
        }
        return moderationService;
    }

    /**
     * Generates an implementation of the Assistant interface to handle requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Assistants assistants() {
        if (assistantService == null) {
            assistantService = cleverClient.create(OpenAI.Assistants.class);
        }
        return assistantService;
    }

    /**
     * Spawns a single instance of the Threads interface to manage requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.Threads threads() {
        if (threadService == null) {
            threadService = cleverClient.create(OpenAI.Threads.class);
        }
        return threadService;
    }
}