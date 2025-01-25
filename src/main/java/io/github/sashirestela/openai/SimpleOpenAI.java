package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.client.HttpClientAdapter;
import io.github.sashirestela.openai.base.ClientConfig;
import io.github.sashirestela.openai.base.OpenAIConfigurator;
import io.github.sashirestela.openai.base.OpenAIProvider;
import io.github.sashirestela.openai.base.RealtimeConfig;
import io.github.sashirestela.openai.exception.SimpleOpenAIException;
import io.github.sashirestela.openai.service.AssistantServices;
import io.github.sashirestela.openai.service.AudioServices;
import io.github.sashirestela.openai.service.BatchServices;
import io.github.sashirestela.openai.service.ChatCompletionServices;
import io.github.sashirestela.openai.service.CompletionServices;
import io.github.sashirestela.openai.service.EmbeddingServices;
import io.github.sashirestela.openai.service.FileServices;
import io.github.sashirestela.openai.service.FineTunningServices;
import io.github.sashirestela.openai.service.ImageServices;
import io.github.sashirestela.openai.service.ModelServices;
import io.github.sashirestela.openai.service.ModerationServices;
import io.github.sashirestela.openai.service.RealtimeServices;
import io.github.sashirestela.openai.service.SessionServices;
import io.github.sashirestela.openai.service.UploadServices;
import io.github.sashirestela.openai.support.Constant;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The standard OpenAI implementation which implements the full services.
 */
public class SimpleOpenAI extends OpenAIProvider implements
        AssistantServices,
        AudioServices,
        BatchServices,
        ChatCompletionServices,
        CompletionServices,
        EmbeddingServices,
        FileServices,
        FineTunningServices,
        ImageServices,
        ModelServices,
        ModerationServices,
        RealtimeServices,
        SessionServices,
        UploadServices {

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey         Identifier to be used for authentication. Mandatory.
     * @param organizationId Organization's id to be charged for usage. Optional.
     * @param projectId      Project's id to provide access to a single project. Optional.
     * @param baseUrl        Host's url, If not provided, it'll be 'https://api.openai.com'. Optional.
     * @param httpClient     A {@link java.net.http.HttpClient HttpClient} object. One is created by
     *                       default if not provided. Optional. Deprecated in favor of clientAdapter.
     * @param clientAdapter  Component to make http services. If none is passed the
     *                       JavaHttpClientAdapter will be used. Optional.
     * @param objectMapper   Provides Json conversions either to and from objects. Optional.
     * @param realtimeConfig Configuration for websocket Realtime API. Optional.
     */
    @Builder
    public SimpleOpenAI(@NonNull String apiKey, String organizationId, String projectId, String baseUrl,
            HttpClient httpClient, HttpClientAdapter clientAdapter, ObjectMapper objectMapper,
            RealtimeConfig realtimeConfig) {
        super(StandardConfigurator.builder()
                .apiKey(apiKey)
                .organizationId(organizationId)
                .projectId(projectId)
                .baseUrl(baseUrl)
                .httpClient(httpClient)
                .clientAdapter(clientAdapter)
                .objectMapper(objectMapper)
                .realtimeConfig(realtimeConfig)
                .build());
    }

    @Override
    public OpenAI.Audios audios() {
        return getOrCreateService(OpenAI.Audios.class);
    }

    @Override
    public OpenAI.Batches batches() {
        return getOrCreateService(OpenAI.Batches.class);
    }

    @Override
    public OpenAI.ChatCompletions chatCompletions() {
        return getOrCreateService(OpenAI.ChatCompletions.class);
    }

    @Override
    public OpenAI.Completions completions() {
        return getOrCreateService(OpenAI.Completions.class);
    }

    @Override
    public OpenAI.Embeddings embeddings() {
        return getOrCreateService(OpenAI.Embeddings.class);
    }

    @Override
    public OpenAI.Files files() {
        return getOrCreateService(OpenAI.Files.class);
    }

    @Override
    public OpenAI.FineTunings fineTunings() {
        return getOrCreateService(OpenAI.FineTunings.class);
    }

    @Override
    public OpenAI.Images images() {
        return getOrCreateService(OpenAI.Images.class);
    }

    @Override
    public OpenAI.Models models() {
        return getOrCreateService(OpenAI.Models.class);
    }

    @Override
    public OpenAI.Moderations moderations() {
        return getOrCreateService(OpenAI.Moderations.class);
    }

    @Override
    public OpenAI.Uploads uploads() {
        return getOrCreateService(OpenAI.Uploads.class);
    }

    @Override
    public OpenAI.SessionTokens sessionTokens() {
        return getOrCreateService(OpenAI.SessionTokens.class);
    }

    @Override
    public OpenAIBeta2.Assistants assistants() {
        return getOrCreateService(OpenAIBeta2.Assistants.class);
    }

    @Override
    public OpenAIBeta2.Threads threads() {
        return getOrCreateService(OpenAIBeta2.Threads.class);
    }

    @Override
    public OpenAIBeta2.ThreadMessages threadMessages() {
        return getOrCreateService(OpenAIBeta2.ThreadMessages.class);
    }

    @Override
    public OpenAIBeta2.ThreadRuns threadRuns() {
        return getOrCreateService(OpenAIBeta2.ThreadRuns.class);
    }

    @Override
    public OpenAIBeta2.ThreadRunSteps threadRunSteps() {
        return getOrCreateService(OpenAIBeta2.ThreadRunSteps.class);
    }

    @Override
    public OpenAIBeta2.VectorStores vectorStores() {
        return getOrCreateService(OpenAIBeta2.VectorStores.class);
    }

    @Override
    public OpenAIBeta2.VectorStoreFiles vectorStoreFiles() {
        return getOrCreateService(OpenAIBeta2.VectorStoreFiles.class);
    }

    @Override
    public OpenAIBeta2.VectorStoreFileBatches vectorStoreFileBatches() {
        return getOrCreateService(OpenAIBeta2.VectorStoreFileBatches.class);
    }

    @Override
    public OpenAIRealtime realtime() {
        return this.realtime;
    }

    @SuperBuilder
    static class StandardConfigurator extends OpenAIConfigurator {

        private String organizationId;
        private String projectId;
        private RealtimeConfig realtimeConfig;

        @Override
        public ClientConfig buildConfig() {
            return ClientConfig.builder()
                    .baseUrl(Optional.ofNullable(baseUrl).orElse(Constant.OPENAI_BASE_URL))
                    .headers(makeHeaders())
                    .httpClient(httpClient)
                    .clientAdapter(clientAdapter)
                    .objectMapper(objectMapper)
                    .realtimeConfig(makeRealtimeConfig())
                    .build();
        }

        private Map<String, String> makeHeaders() {
            var headers = new HashMap<String, String>();
            headers.put(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + apiKey);
            if (organizationId != null) {
                headers.put(Constant.OPENAI_ORG_HEADER, organizationId);
            }
            if (projectId != null) {
                if (organizationId == null) {
                    throw new SimpleOpenAIException("OrganizationId should be provided if ProjectId is provided.");
                }
                headers.put(Constant.OPENAI_PRJ_HEADER, projectId);
            }
            return headers;
        }

        private RealtimeConfig makeRealtimeConfig() {
            if (realtimeConfig == null) {
                return null;
            }
            var headers = new HashMap<String, String>();
            headers.put(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + apiKey);
            headers.put(Constant.OPENAI_BETA_HEADER, Constant.OPENAI_REALTIME_VERSION);
            var queryParams = new HashMap<String, String>();
            queryParams.put(Constant.OPENAI_REALTIME_MODEL_NAME, realtimeConfig.getModel());
            return RealtimeConfig.builder()
                    .endpointUrl(
                            Optional.ofNullable(realtimeConfig.getEndpointUrl())
                                    .orElse(Constant.OPENAI_WS_ENDPOINT_URL))
                    .headers(headers)
                    .queryParams(queryParams)
                    .webSocketAdapter(realtimeConfig.getWebSocketAdapter())
                    .build();
        }

    }

}
