package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.CleverClient;
import io.github.sashirestela.slimvalidator.Validator;
import io.github.sashirestela.slimvalidator.exception.ConstraintViolationException;
import lombok.NonNull;
import lombok.Setter;

import java.net.http.HttpClient;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The base abstract class that all providers extend. The implementation of services implemented by
 * two or more providers goes here and if a provider does not implement a service implemented by
 * others, it must override it to thrown an Unsuported exception.
 */
public abstract class BaseSimpleOpenAI {

    private static final String END_OF_STREAM = "[DONE]";
    protected static final String NOT_IMPLEMENTED = "Not implemented.";

    @Setter
    protected CleverClient cleverClient;

    protected OpenAIRealtime realtime;

    protected OpenAI.Audios audioService;
    protected OpenAI.Batches batchService;
    protected OpenAI.ChatCompletions chatCompletionService;
    protected OpenAI.Completions completionService;
    protected OpenAI.Embeddings embeddingService;
    protected OpenAI.Files fileService;
    protected OpenAI.FineTunings fineTuningService;
    protected OpenAI.Images imageService;
    protected OpenAI.Models modelService;
    protected OpenAI.Moderations moderationService;
    protected OpenAI.Uploads uploadService;
    protected OpenAIBeta2.Assistants assistantService;
    protected OpenAIBeta2.Threads threadService;
    protected OpenAIBeta2.ThreadMessages threadMessageService;
    protected OpenAIBeta2.ThreadRuns threadRunService;
    protected OpenAIBeta2.ThreadRunSteps threadRunStepService;
    protected OpenAIBeta2.VectorStores vectorStoreService;
    protected OpenAIBeta2.VectorStoreFiles vectorStoreFileService;
    protected OpenAIBeta2.VectorStoreFileBatches vectorStoreFileBatchService;

    BaseSimpleOpenAI(@NonNull BaseSimpleOpenAIArgs args) {
        var httpClient = Optional.ofNullable(args.getHttpClient()).orElse(HttpClient.newHttpClient());
        Consumer<Object> bodyInspector = body -> {
            var validator = new Validator();
            var violations = validator.validate(body);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        };
        var objectMapper = Optional.ofNullable(args.getObjectMapper()).orElse(new ObjectMapper());
        this.cleverClient = CleverClient.builder()
                .httpClient(httpClient)
                .baseUrl(args.getBaseUrl())
                .headers(args.getHeaders())
                .requestInterceptor(args.getRequestInterceptor())
                .bodyInspector(bodyInspector)
                .endOfStream(END_OF_STREAM)
                .objectMapper(objectMapper)
                .build();
        var baseRealtimeConfig = args.getBaseRealtimeConfig();
        if (baseRealtimeConfig != null) {
            this.realtime = OpenAIRealtime.builder()
                    .httpClient(httpClient)
                    .baseRealtimeConfig(baseRealtimeConfig)
                    .build();
        }
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Audios audios() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Batches batches() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Generates an implementation of the ChatCompletions interface to handle requests.
     *
     * @return An instance of the interface.
     */
    public OpenAI.ChatCompletions chatCompletions() {
        if (chatCompletionService == null) {
            chatCompletionService = cleverClient.create(OpenAI.ChatCompletions.class);
        }
        return chatCompletionService;
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Completions completions() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Embeddings embeddings() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Generates an implementation of the Files interface to handle requests.
     *
     * @return An instance of the interface.
     */
    public OpenAI.Files files() {
        if (fileService == null) {
            fileService = cleverClient.create(OpenAI.Files.class);
        }
        return fileService;
    }

    /**
     * Throw not implemented
     */
    public OpenAI.FineTunings fineTunings() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Images images() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Models models() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Moderations moderations() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Uploads uploads() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAIBeta2.Assistants assistants() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAIBeta2.Threads threads() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAIBeta2.ThreadMessages threadMessages() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAIBeta2.ThreadRuns threadRuns() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAIBeta2.ThreadRunSteps threadRunSteps() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAIBeta2.VectorStores vectorStores() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAIBeta2.VectorStoreFiles vectorStoreFiles() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAIBeta2.VectorStoreFileBatches vectorStoreFileBatches() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAIRealtime realtime() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

}
