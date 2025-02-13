package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.client.HttpClientAdapter;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.retry.RetryConfig;
import io.github.sashirestela.openai.OpenAI.ChatCompletions;
import io.github.sashirestela.openai.OpenAI.Embeddings;
import io.github.sashirestela.openai.base.ClientConfig;
import io.github.sashirestela.openai.base.OpenAIConfigurator;
import io.github.sashirestela.openai.base.OpenAIProvider;
import io.github.sashirestela.openai.service.ChatCompletionServices;
import io.github.sashirestela.openai.service.EmbeddingServices;
import io.github.sashirestela.openai.support.Constant;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

public class SimpleOpenAIGeminiGoogle extends OpenAIProvider implements
        ChatCompletionServices,
        EmbeddingServices {

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey        Identifier to be used for authentication. Mandatory.
     * @param baseUrl       Host's url. Optional.
     * @param clientAdapter Component to make http services. If none is passed the JavaHttpClientAdapter
     *                      will be used. Optional.
     * @param retryConfig   Configuration for request retrying. If not provided, default values will be
     *                      used. Optional.
     * @param objectMapper  Provides Json conversions either to and from objects. Optional.
     */
    @Builder
    protected SimpleOpenAIGeminiGoogle(@NonNull String apiKey, String baseUrl, HttpClientAdapter clientAdapter,
            RetryConfig retryConfig, ObjectMapper objectMapper) {
        super(GeminiGoogleConfigurator.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .clientAdapter(clientAdapter)
                .retryConfig(retryConfig)
                .objectMapper(objectMapper)
                .build());
    }

    @Override
    public ChatCompletions chatCompletions() {
        return getOrCreateService(OpenAI.ChatCompletions.class);
    }

    @Override
    public Embeddings embeddings() {
        return getOrCreateService(OpenAI.Embeddings.class);
    }

    @SuperBuilder
    static class GeminiGoogleConfigurator extends OpenAIConfigurator {

        @Override
        public ClientConfig buildConfig() {
            return ClientConfig.builder()
                    .baseUrl(Optional.ofNullable(baseUrl).orElse(Constant.GEMINIGOOGLE_BASE_URL))
                    .headers(Map.of(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + apiKey))
                    .clientAdapter(clientAdapter)
                    .retryConfig(retryConfig)
                    .requestInterceptor(makeRequestInterceptor())
                    .objectMapper(objectMapper)
                    .build();
        }

        private UnaryOperator<HttpRequestData> makeRequestInterceptor() {
            final String VERSION_REGEX = "/v\\d+(\\.\\d+)?/";
            return request -> {
                var newUrl = request.getUrl().replaceFirst(VERSION_REGEX, "/");
                request.setUrl(newUrl);
                return request;
            };
        }

    }

}
