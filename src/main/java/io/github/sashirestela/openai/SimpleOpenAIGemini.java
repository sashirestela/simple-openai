package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.openai.base.ClientConfig;
import io.github.sashirestela.openai.base.OpenAIConfigurator;
import io.github.sashirestela.openai.base.OpenAIProvider;
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
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * The Gemini implementation which implements a subset of the standard services.
 */
public class SimpleOpenAIGemini extends OpenAIProvider implements
        ChatCompletionServices,
        EmbeddingServices,
        ModelServices
 {

    /**
     * Constructor used to generate a builder.
     *
     * @param baseUrl        The URL of the Azure OpenAI deployment. Mandatory.
     * @param httpClient     A {@link HttpClient HttpClient} object. One is created by default if not
     *                       provided. Optional.
     * @param apiKeyProvider Provides the API key to be used for authentication. Mandatory.
     * @param objectMapper   Provides Json conversions either to and from objects. Optional.
     */
    @Builder
    public SimpleOpenAIGemini(@NonNull String baseUrl, @NonNull Supplier<String> apiKeyProvider,
            HttpClient httpClient, ObjectMapper objectMapper) {
        super(GeminiConfigurator.builder()
                .baseUrl(baseUrl)
                .apiKeyProvider(apiKeyProvider)
                .httpClient(httpClient)
                .objectMapper(objectMapper)
                .build());
    }

    @Override
    public OpenAI.ChatCompletions chatCompletions() {
        return getOrCreateService(OpenAI.ChatCompletions.class);
    }


    @Override
    public OpenAI.Embeddings embeddings() {
        return getOrCreateService(OpenAI.Embeddings.class);
    }


    @Override
    public OpenAI.Models models() {
        return getOrCreateService(OpenAI.Models.class);
    }


    @SuperBuilder
    static class GeminiConfigurator extends OpenAIConfigurator {

        private Supplier<String> apiKeyProvider;

        @Override
        public ClientConfig buildConfig() {
            return ClientConfig.builder()
                    .baseUrl(baseUrl)
                    .httpClient(httpClient)
                    .headers(new HashMap<>())
                    .requestInterceptor(makeRequestInterceptor())
                    .objectMapper(objectMapper)
                    .build();
        }

        private UnaryOperator<HttpRequestData> makeRequestInterceptor() {
            return request -> {
                var url = request.getUrl().replace("/v1/", "/");
                request.setUrl(url);

                var headers = request.getHeaders();
                headers = headers == null ? new HashMap<>() : headers;
                headers.put("Authorization", "Bearer " + apiKeyProvider.get());
                request.setHeaders(headers);
                return request;
            };
        }

    }

}
