package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.CleverClient;
import java.net.http.HttpClient;
import java.util.Optional;
import lombok.NonNull;
import lombok.Setter;


/**
 * The base abstract class that all providers extend
 */


public class BaseSimpleOpenAI {

    private static final String END_OF_STREAM = "[DONE]";

    @Setter
    protected CleverClient cleverClient;

    protected OpenAI.ChatCompletions chatCompletionService;

    BaseSimpleOpenAI(@NonNull BaseSimpleOpenAIArgs args) {
        var httpClient =
            Optional.ofNullable(args.getHttpClient()).orElse(HttpClient.newHttpClient());
        this.cleverClient = CleverClient.builder()
            .httpClient(httpClient)
            .baseUrl(args.getBaseUrl())
            .headers(args.getHeaders())
            .endOfStream(END_OF_STREAM)
            .requestInterceptor(args.getRequestInterceptor())
            .build();
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Audios audios() {
        throw new SimpleUncheckedException("Not implemented");
    }

    /**
     * Generates an implementation of the ChatCompletions interface to handle
     * requests.
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
        throw new SimpleUncheckedException("Not implemented");
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Embeddings embeddings() {
        throw new SimpleUncheckedException("Not implemented");
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Files files() {
        throw new SimpleUncheckedException("Not implemented");
    }

    /**
     * Throw not implemented
     */
    public OpenAI.FineTunings fineTunings() {
        throw new SimpleUncheckedException("Not implemented");
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Images images() {
        throw new SimpleUncheckedException("Not implemented");
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Models models() {
        throw new SimpleUncheckedException("Not implemented");
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Moderations moderations() {
        throw new SimpleUncheckedException("Not implemented");
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Assistants assistants() {
        throw new SimpleUncheckedException("Not implemented");
    }

    /**
     * Throw not implemented
     */
    public OpenAI.Threads threads() {
        throw new SimpleUncheckedException("Not implemented");
    }
}
