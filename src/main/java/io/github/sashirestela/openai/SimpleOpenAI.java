package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.support.Constant;
import lombok.Builder;
import lombok.NonNull;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Optional;

/**
 * The standard OpenAI implementation which implements the full services.
 */
public class SimpleOpenAI extends BaseSimpleOpenAI {

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey         Identifier to be used for authentication. Mandatory.
     * @param organizationId Organization's id to be charged for usage. Optional.
     * @param projectId      Project's id to provide access to a single project. Optional.
     * @param baseUrl        Host's url, If not provided, it'll be 'https://api.openai.com'. Optional.
     * @param httpClient     A {@link java.net.http.HttpClient HttpClient} object. One is created by
     *                       default if not provided. Optional.
     * @param objectMapper   Provides Json conversions either to and from objects. Optional.
     */
    @Builder
    public SimpleOpenAI(@NonNull String apiKey, String organizationId, String projectId, String baseUrl,
            HttpClient httpClient, ObjectMapper objectMapper) {
        super(prepareBaseSimpleOpenAIArgs(apiKey, organizationId, projectId, baseUrl, httpClient, objectMapper));
    }

    public static BaseSimpleOpenAIArgs prepareBaseSimpleOpenAIArgs(String apiKey, String organizationId,
            String projectId, String baseUrl, HttpClient httpClient, ObjectMapper objectMapper) {

        var headers = new HashMap<String, String>();
        headers.put(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + apiKey);
        if (organizationId != null) {
            headers.put(Constant.OPENAI_ORG_HEADER, organizationId);
        }
        if (projectId != null) {
            headers.put(Constant.OPENAI_PRJ_HEADER, projectId);
        }

        return BaseSimpleOpenAIArgs.builder()
                .baseUrl(Optional.ofNullable(baseUrl).orElse(Constant.OPENAI_BASE_URL))
                .headers(headers)
                .httpClient(httpClient)
                .objectMapper(objectMapper)
                .build();
    }

    /**
     * Generates an implementation of the Audios interface to handle requests.
     *
     * @return An instance of the interface.
     */
    @Override
    public OpenAI.Audios audios() {
        if (audioService == null) {
            audioService = cleverClient.create(OpenAI.Audios.class);
        }
        return audioService;
    }

    /**
     * Generates an implementation of the Audios interface to handle requests.
     *
     * @return An instance of the interface.
     */
    @Override
    public OpenAI.Batches batches() {
        if (batchService == null) {
            batchService = cleverClient.create(OpenAI.Batches.class);
        }
        return batchService;
    }

    /**
     * Generates an implementation of the Completions interface to handle requests.
     *
     * @return An instance of the interface.
     */
    @Override
    public OpenAI.Completions completions() {
        if (completionService == null) {
            completionService = cleverClient.create(OpenAI.Completions.class);
        }
        return completionService;
    }

    /**
     * Generates an implementation of the Embeddings interface to handle requests.
     *
     * @return An instance of the interface.
     */
    @Override
    public OpenAI.Embeddings embeddings() {
        if (embeddingService == null) {
            embeddingService = cleverClient.create(OpenAI.Embeddings.class);
        }
        return embeddingService;
    }

    /**
     * Generates an implementation of the FineTunings interface to handle requests.
     *
     * @return An instance of the interface.
     */
    @Override
    public OpenAI.FineTunings fineTunings() {
        if (fineTuningService == null) {
            fineTuningService = cleverClient.create(OpenAI.FineTunings.class);
        }
        return fineTuningService;
    }

    /**
     * Generates an implementation of the Images interface to handle requests.
     *
     * @return An instance of the interface.
     */
    @Override
    public OpenAI.Images images() {
        if (imageService == null) {
            imageService = cleverClient.create(OpenAI.Images.class);
        }
        return imageService;
    }

    /**
     * Generates an implementation of the Models interface to handle requests.
     *
     * @return An instance of the interface.
     */
    @Override
    public OpenAI.Models models() {
        if (modelService == null) {
            modelService = cleverClient.create(OpenAI.Models.class);
        }
        return modelService;
    }

    /**
     * Generates an implementation of the Moderations interface to handle requests.
     *
     * @return An instance of the interface.
     */
    @Override
    public OpenAI.Moderations moderations() {
        if (moderationService == null) {
            moderationService = cleverClient.create(OpenAI.Moderations.class);
        }
        return moderationService;
    }

    /**
     * Generates an implementation of the Uploads interface to handle requests.
     *
     * @return An instance of the interface.
     */
    @Override
    public OpenAI.Uploads uploads() {
        if (uploadService == null) {
            uploadService = cleverClient.create(OpenAI.Uploads.class);
        }
        return uploadService;
    }

    @Override
    public OpenAIBeta2.Assistants assistants() {
        if (assistantService == null) {
            assistantService = cleverClient.create(OpenAIBeta2.Assistants.class);
        }
        return assistantService;
    }

    @Override
    public OpenAIBeta2.Threads threads() {
        if (threadService == null) {
            threadService = cleverClient.create(OpenAIBeta2.Threads.class);
        }
        return threadService;
    }

    @Override
    public OpenAIBeta2.ThreadMessages threadMessages() {
        if (threadMessageService == null) {
            threadMessageService = cleverClient.create(OpenAIBeta2.ThreadMessages.class);
        }
        return threadMessageService;
    }

    @Override
    public OpenAIBeta2.ThreadRuns threadRuns() {
        if (threadRunService == null) {
            threadRunService = cleverClient.create(OpenAIBeta2.ThreadRuns.class);
        }
        return threadRunService;
    }

    @Override
    public OpenAIBeta2.ThreadRunSteps threadRunSteps() {
        if (threadRunStepService == null) {
            threadRunStepService = cleverClient.create(OpenAIBeta2.ThreadRunSteps.class);
        }
        return threadRunStepService;
    }

    @Override
    public OpenAIBeta2.VectorStores vectorStores() {
        if (vectorStoreService == null) {
            vectorStoreService = cleverClient.create(OpenAIBeta2.VectorStores.class);
        }
        return vectorStoreService;
    }

    @Override
    public OpenAIBeta2.VectorStoreFiles vectorStoreFiles() {
        if (vectorStoreFileService == null) {
            vectorStoreFileService = cleverClient.create(OpenAIBeta2.VectorStoreFiles.class);
        }
        return vectorStoreFileService;
    }

    @Override
    public OpenAIBeta2.VectorStoreFileBatches vectorStoreFileBatches() {
        if (vectorStoreFileBatchService == null) {
            vectorStoreFileBatchService = cleverClient.create(OpenAIBeta2.VectorStoreFileBatches.class);
        }
        return vectorStoreFileBatchService;
    }

}
