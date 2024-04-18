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
 * The base abstract class that all providers extend. The implmentation of services implemented by
 * two or more providers goes here and if a provider does not implement a service implemented by
 * others, it must override it to thrown an Unsuported exception.
 */
public abstract class BaseSimpleOpenAI {

    private static final String END_OF_STREAM = "[DONE]";
    protected static final String NOT_IMPLEMENTED = "Not implemented.";

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
     * Throw not implemented
     */
    public OpenAI.Audios audios() {
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
     * Generates an implementation of the Assistant interface to handle requests.
     *
     * @return An instance of the interface.
     */
    public OpenAIBeta.Assistants assistants() {
        if (assistantService == null) {
            assistantService = cleverClient.create(OpenAIBeta.Assistants.class);
        }
        return assistantService;
    }

    /**
     * Generates an implementation of the Threads interface to handle requests.
     *
     * @return An instance of the interface.
     */
    public OpenAIBeta.Threads threads() {
        if (threadService == null) {
            threadService = cleverClient.create(OpenAIBeta.Threads.class);
        }
        return threadService;
    }

}
