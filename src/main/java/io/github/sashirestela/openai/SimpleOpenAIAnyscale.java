package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.CleverClient;
import io.github.sashirestela.openai.OpenAI.Assistants;
import io.github.sashirestela.openai.OpenAI.Audios;
import io.github.sashirestela.openai.OpenAI.ChatCompletions;
import io.github.sashirestela.openai.OpenAI.Completions;
import io.github.sashirestela.openai.OpenAI.Embeddings;
import io.github.sashirestela.openai.OpenAI.Files;
import io.github.sashirestela.openai.OpenAI.FineTunings;
import io.github.sashirestela.openai.OpenAI.Images;
import io.github.sashirestela.openai.OpenAI.Models;
import io.github.sashirestela.openai.OpenAI.Moderations;
import io.github.sashirestela.openai.OpenAI.Threads;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Optional;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * The factory that generates implementations of the {@link OpenAI OpenAI}
 * interfaces.
 */
@Getter
public class SimpleOpenAIAnyscale implements BaseSimpleOpenAI {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_AUTHORIZATION = "Bearer ";
    private static final String END_OF_STREAM = "[DONE]"; ;
    private final String apiKey;
    private final String baseUrl;
    private final HttpClient httpClient;
    private final CleverClient cleverClient;
    private ChatCompletions chatCompletionService;

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey         Identifier to be used for authentication. Mandatory.
     * @param baseUrl        Host's url
     * @param httpClient     A {@link java.net.http.HttpClient HttpClient} object.
     *                       One is created by default if not provided. Optional.
     */
    @Builder
    public SimpleOpenAIAnyscale(
        @NonNull String apiKey,
        @NonNull String baseUrl,
        HttpClient httpClient) {

        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.httpClient = Optional.ofNullable(httpClient).orElse(HttpClient.newHttpClient());

        var headers = new HashMap<String, String>();
        headers.put(AUTHORIZATION_HEADER, BEARER_AUTHORIZATION + apiKey);
        this.cleverClient = CleverClient.builder()
            .httpClient(this.httpClient)
            .baseUrl(this.baseUrl)
            .headers(headers)
            .endOfStream(END_OF_STREAM)
            .build();
    }

    @Override
    public Audios audios() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public ChatCompletions chatCompletions() {
        if (this.chatCompletionService == null) {
            this.chatCompletionService = cleverClient.create(OpenAI.ChatCompletions.class);
        }
        return this.chatCompletionService;
    }

    @Override
    public Completions completions() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Embeddings embeddings() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Files files() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public FineTunings fineTunings() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Images images() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Models models() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Moderations moderations() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Assistants assistants() {
        throw new SimpleUncheckedException("Not implemented");
    }

    @Override
    public Threads threads() {
        throw new SimpleUncheckedException("Not implemented");
    }
}
