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
    private static final String NOT_IMPLEMENTED = "Not implemented.";

    @Setter
    protected CleverClient cleverClient;

    protected OpenAI.ChatCompletions chatCompletionService;

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
     * @return An instance of the interface. It is created only once.
     */
    public OpenAI.ChatCompletions chatCompletions() {
        if (this.chatCompletionService == null) {
            this.chatCompletionService = this.cleverClient.create(OpenAI.ChatCompletions.class);
        }
        return this.chatCompletionService;

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
     * Throw not implemented
     */
    public OpenAI.Files files() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
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
    public OpenAIBeta.Assistants assistants() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    public OpenAIBeta.Threads threads() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

}
