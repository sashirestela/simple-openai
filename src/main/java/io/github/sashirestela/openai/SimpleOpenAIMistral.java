package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.support.ContentType;
import io.github.sashirestela.openai.OpenAI.ChatCompletions;
import io.github.sashirestela.openai.OpenAI.Embeddings;
import io.github.sashirestela.openai.OpenAI.Models;
import io.github.sashirestela.openai.base.ClientConfig;
import io.github.sashirestela.openai.base.OpenAIConfigurator;
import io.github.sashirestela.openai.base.OpenAIProvider;
import io.github.sashirestela.openai.service.ChatCompletionServices;
import io.github.sashirestela.openai.service.EmbeddingServices;
import io.github.sashirestela.openai.service.ModelServices;
import io.github.sashirestela.openai.support.Constant;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * The Mistral OpenAI provider which implements a subset of the standard services.
 */
public class SimpleOpenAIMistral extends OpenAIProvider implements
        ChatCompletionServices,
        EmbeddingServices,
        ModelServices {

    /**
     * Constructor used to generate a builder.
     *
     * @param apiKey       Identifier to be used for authentication. Mandatory.
     * @param baseUrl      Host's url. Optional.
     * @param httpClient   A {@link java.net.http.HttpClient HttpClient} object. One is created by
     *                     default if not provided. Optional.
     * @param objectMapper Provides Json conversions either to and from objects. Optional.
     */
    @Builder
    public SimpleOpenAIMistral(@NonNull String apiKey, String baseUrl, HttpClient httpClient,
            ObjectMapper objectMapper) {
        super(MistralConfigurator.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .httpClient(httpClient)
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

    @Override
    public Models models() {
        return getOrCreateService(OpenAI.Models.class);
    }

    @SuperBuilder
    static class MistralConfigurator extends OpenAIConfigurator {

        @Override
        public ClientConfig buildConfig() {
            return ClientConfig.builder()
                    .baseUrl(Optional.ofNullable(baseUrl).orElse(Constant.MISTRAL_BASE_URL))
                    .headers(Map.of(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + apiKey))
                    .httpClient(httpClient)
                    .requestInterceptor(makeRequestInterceptor())
                    .objectMapper(objectMapper)
                    .build();
        }

        private UnaryOperator<HttpRequestData> makeRequestInterceptor() {
            return request -> {
                var contentType = request.getContentType();
                if (contentType != null && contentType.equals(ContentType.APPLICATION_JSON)) {
                    var body = makeNewBody(request);
                    request.setBody(body);
                }
                return request;
            };
        }

        private String makeNewBody(HttpRequestData request) {
            Map<String, String> mapRegexReplace = new HashMap<>();
            mapRegexReplace.put(",\\s*\"stream_options\"\\s*:\\s*\\{[^{}]*\\}", "");  // Remove "stream_options"
            mapRegexReplace.put(",?\\s*\"additionalProperties\"\\s*:\\s*false\\s*", "");  // Remove "additionalProperties"
            mapRegexReplace.put(",?\\s*\"strict\"\\s*:\\s*true\\s*", "");  // Remove "strict"
            mapRegexReplace.put(",\\s*,", ",");  // Replace double commas by one comma
            mapRegexReplace.put(",\\s*}", "}");  // Replace trailing comma by closing brace
            mapRegexReplace.put("\"index\"\\s*:\\s*null\\s*,\\s*", "");  // Remove "index: null"
            mapRegexReplace.put(",\\s*\"refusal\"\\s*:\\s*null", "");  // Remove "refusal: null"
            mapRegexReplace.put(",\\s*\"audio\"\\s*:\\s*null", "");  // Remove "audio: null"
            var body = (String) request.getBody();
            for (var entry : mapRegexReplace.entrySet()) {
                body = body.replaceAll(entry.getKey(), entry.getValue());
            }
            return body;
        }

    }

}
