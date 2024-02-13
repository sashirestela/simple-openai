package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.http.HttpRequestData;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.github.sashirestela.cleverclient.CleverClient;
import java.util.Set;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * The factory that generates implementations of the {@link OpenAI OpenAI}
 * interfaces.
 */
@Getter
public class SimpleOpenAI {

    public static final String OPENAI_BASE_URL = "https://api.openai.com";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String ORGANIZATION_HEADER = "OpenAI-Organization";
    private static final String BEARER_AUTHORIZATION = "Bearer ";
    private static final String END_OF_STREAM = "[DONE]";

    @NonNull
    private String apiKey;

    private String organizationId;
    private final String baseUrl;
    @Deprecated
    private final String urlBase = null;

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
     * @param apiKey         Identifier to be used for authentication. Mandatory.
     * @param organizationId Organization's id to be charged for usage. Optional.
     * @param baseUrl        Host's url, If not provided (including via the
     *                       deprecated urlBase), it'll be
     *                       <a href="https://api.openai.com">...</a>. Optional.
     * @param urlBase        [[ Deprecated ]] Host's url. See baseUrl. urlBase will
     *                       be removed in a future version. Optional.
     * @param httpClient     A {@link java.net.http.HttpClient HttpClient} object.
     *                       One is created by default if not provided. Optional.
     */
    @Builder
    public SimpleOpenAI(
            String apiKey,
            String organizationId,
            String baseUrl,
            String urlBase,
            HttpClient httpClient,
            UnaryOperator<HttpRequestData> requestInterceptor) {
        this.apiKey = apiKey;
        this.organizationId = organizationId;
        this.baseUrl = Optional.ofNullable(baseUrl)
                .orElse(Optional.ofNullable(urlBase).orElse(OPENAI_BASE_URL));

        this.httpClient = Optional.ofNullable(httpClient).orElse(HttpClient.newHttpClient());

        var headers = new HashMap<String, String>();
        headers.put(AUTHORIZATION_HEADER, BEARER_AUTHORIZATION + apiKey);
        if (organizationId != null) {
            headers.put(ORGANIZATION_HEADER, organizationId);
        }
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