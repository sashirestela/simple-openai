package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.CleverClient;
import io.github.sashirestela.slimvalidator.Validator;
import io.github.sashirestela.slimvalidator.exception.ConstraintViolationException;
import lombok.NonNull;
import lombok.Setter;

import java.net.http.HttpClient;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * The base abstract class that all providers extend. It generates an implementation to the
 * chatCompletions() interface of {@link OpenAI OpenAI} interfaces. It throws a NOT_IMPLEMENTED
 * exception for all other interfaces
 */
public abstract class BaseSimpleOpenAI {

    private static final String END_OF_STREAM = "[DONE]";

    @Setter
    protected CleverClient cleverClient;

    protected OpenAI.Audios audioService;

    protected OpenAI.ChatCompletions chatCompletionService;
    protected OpenAI.Completions completionService;
    protected OpenAI.Embeddings embeddingService;
    protected OpenAI.Files fileService;
    protected OpenAI.FineTunings fineTuningService;
    protected OpenAI.Images imageService;
    protected OpenAI.Models modelService;
    protected OpenAI.Moderations moderationService;
    protected OpenAIBeta.Assistants assistantService;
    protected OpenAIBeta.Threads threadService;

    BaseSimpleOpenAI(@NonNull BaseSimpleOpenAIArgs args) {
        var httpClient = Optional.ofNullable(args.getHttpClient()).orElse(HttpClient.newHttpClient());
        Consumer<Object> bodyInspector = body -> {
            var validator = new Validator();
            var violations = validator.validate(body);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        };
        this.cleverClient = CleverClient.builder()
                .httpClient(httpClient)
                .baseUrl(args.getBaseUrl())
                .headers(args.getHeaders())
                .endOfStream(END_OF_STREAM)
                .requestInterceptor(args.getRequestInterceptor())
                .bodyInspector(bodyInspector)
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
    public OpenAI.ChatCompletions chatCompletions() {
        if (chatCompletionService == null) {
            chatCompletionService = cleverClient.create(OpenAI.ChatCompletions.class);
        }
        return chatCompletionService;
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
    public OpenAIBeta.Assistants assistants() {
        if (assistantService == null) {
            assistantService = cleverClient.create(OpenAIBeta.Assistants.class);
        }
        return assistantService;
    }

    /**
     * Spawns a single instance of the Threads interface to manage requests.
     *
     * @return An instance of the interface. It is created only once.
     */
    public OpenAIBeta.Threads threads() {
        if (threadService == null) {
            threadService = cleverClient.create(OpenAIBeta.Threads.class);
        }
        return threadService;
    }

}
