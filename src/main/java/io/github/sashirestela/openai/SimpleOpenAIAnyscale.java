package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.client.HttpClientAdapter;
import io.github.sashirestela.cleverclient.retry.RetryConfig;
import io.github.sashirestela.openai.base.ClientConfig;
import io.github.sashirestela.openai.base.OpenAIConfigurator;
import io.github.sashirestela.openai.base.OpenAIProvider;
import io.github.sashirestela.openai.service.ChatCompletionServices;
import io.github.sashirestela.openai.support.Constant;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.Optional;

/**
 * The Anyscale OpenAI implementation which implements a subset of the standard services.
 */
public class SimpleOpenAIAnyscale extends OpenAIProvider implements
        ChatCompletionServices {

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey        Identifier to be used for authentication. Mandatory.
     * @param baseUrl       Host's url. Optional.
     * @param httpClient    A {@link java.net.http.HttpClient HttpClient} object. One is created by
     *                      default if not provided. Optional. Deprecated in favor of clientAdapter.
     * @param clientAdapter Component to make http services. If none is passed the JavaHttpClientAdapter
     *                      will be used. Optional.
     * @param retryConfig   Configuration for request retrying. If not provided, default values will be
     *                      used. Optional.
     * @param objectMapper  Provides Json conversions either to and from objects. Optional.
     */
    @Builder
    public SimpleOpenAIAnyscale(@NonNull String apiKey, String baseUrl, HttpClient httpClient,
            HttpClientAdapter clientAdapter, RetryConfig retryConfig, ObjectMapper objectMapper) {
        super(AnyscaleConfigurator.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .httpClient(httpClient)
                .clientAdapter(clientAdapter)
                .retryConfig(retryConfig)
                .objectMapper(objectMapper)
                .build());
    }

    @Override
    public OpenAI.ChatCompletions chatCompletions() {
        return getOrCreateService(OpenAI.ChatCompletions.class);
    }

    @SuperBuilder
    static class AnyscaleConfigurator extends OpenAIConfigurator {

        @Override
        public ClientConfig buildConfig() {
            return ClientConfig.builder()
                    .baseUrl(Optional.ofNullable(baseUrl).orElse(Constant.ANYSCALE_BASE_URL))
                    .headers(Map.of(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + apiKey))
                    .httpClient(httpClient)
                    .clientAdapter(clientAdapter)
                    .retryConfig(retryConfig)
                    .objectMapper(objectMapper)
                    .build();
        }

    }

}
