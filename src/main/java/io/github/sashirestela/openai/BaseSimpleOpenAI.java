package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.CleverClient;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * The factory that generates implementations of the {@link OpenAI OpenAI}
 * interfaces.
 */
@Getter
public class BaseSimpleOpenAI {

    private static final String END_OF_STREAM = "[DONE]";

    @NonNull
    private String apiKey;

    @NonNull
    private final String baseUrl;

    private HttpClient httpClient;

    private CleverClient cleverClient;

    @Getter(AccessLevel.NONE)
    private OpenAI.Audios audioService;

    @Getter(AccessLevel.NONE)
    private OpenAI.ChatCompletions chatCompletionService;

    @Getter(AccessLevel.NONE)
    private OpenAI.Completions completionService;

    @Getter(AccessLevel.NONE)
    private OpenAI.Embeddings embeddingService;

    @Getter(AccessLevel.NONE)
    private OpenAI.Files fileService;

    @Getter(AccessLevel.NONE)
    private OpenAI.FineTunings fineTuningService;

    @Getter(AccessLevel.NONE)
    private OpenAI.Images imageService;

    @Getter(AccessLevel.NONE)
    private OpenAI.Models modelService;

    @Getter(AccessLevel.NONE)
    private OpenAI.Moderations moderationService;

    @Getter(AccessLevel.NONE)
    private OpenAI.Assistants assistantService;

    @Getter(AccessLevel.NONE)
    private OpenAI.Threads threadService;

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey             Identifier to be used for authentication. Mandatory.
     * @param baseUrl            Host's url. Mandatory.
     * @param httpClient         A {@link HttpClient HttpClient} object.
     *                           One is created by default if not provided. Optional.
     * @param requestInterceptor A function to modify the request before it is sent.
     */
    public BaseSimpleOpenAI(
            String apiKey,
            String baseUrl,
            Map<String, String> headers,
            HttpClient httpClient,
            UnaryOperator<HttpRequestData> requestInterceptor) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;

        this.httpClient = Optional.ofNullable(httpClient).orElse(HttpClient.newHttpClient());

        this.cleverClient = CleverClient.builder()
                .httpClient(this.httpClient)
                .baseUrl(this.baseUrl)
                .headers(headers)
                .endOfStream(END_OF_STREAM)
                .requestInterceptor(requestInterceptor)
                .build();
    }

    public void setCleverClient(CleverClient cleverClient) {
        this.cleverClient = cleverClient;
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
     * Generates an implementation of the ChatCompletions interface to handle
     * requests.
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
