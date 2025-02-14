package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.sashirestela.cleverclient.client.HttpClientAdapter;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.http.HttpResponseData;
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
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * The Gemini implementation which implements a subset of the standard services.
 */
public class SimpleOpenAIGeminiVertex extends OpenAIProvider implements ChatCompletionServices {

    /**
     * Constructor used to generate a builder.
     *
     * @param baseUrl        The URL of the Azure OpenAI deployment. Mandatory.
     * @param httpClient     A {@link HttpClient} object. One is created by default if not provided.
     *                       Optional.
     * @param apiKeyProvider Provides the API key to be used for authentication. Mandatory.
     * @param objectMapper   Provides JSON conversions either to and from objects. Optional.
     */
    @Builder
    public SimpleOpenAIGeminiVertex(@NonNull String baseUrl, @NonNull Supplier<String> apiKeyProvider,
            HttpClient httpClient, HttpClientAdapter clientAdapter, RetryConfig retryConfig,
            ObjectMapper objectMapper) {
        super(GeminiConfigurator.builder()
                .apiKeyProvider(apiKeyProvider)
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
    static class GeminiConfigurator extends OpenAIConfigurator {

        private final Supplier<String> apiKeyProvider;

        @Override
        public ClientConfig buildConfig() {
            return ClientConfig.builder()
                    .baseUrl(Optional.ofNullable(baseUrl).orElse(Constant.GEMINIGOOGLE_BASE_URL))
                    .httpClient(httpClient)
                    .headers(
                            Map.of(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + apiKeyProvider.get()))
                    .requestInterceptor(makeRequestInterceptor())
                    .responseInterceptor(makeResponseInterceptor())
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

        private UnaryOperator<HttpResponseData> makeResponseInterceptor() {
            return response -> {
                var body = response.getBody();
                if (body == null) {
                    return response;
                }

                try {
                    ObjectMapper mapper = new ObjectMapper();
                    var rootNode = mapper.readTree(body);
                    var choicesNode = rootNode.path("choices");

                    if (choicesNode.isArray() && !choicesNode.isEmpty()) {
                        var messageNode = choicesNode.get(0).path("message");
                        var contentNode = messageNode.path("content");

                        if (contentNode.isArray()) {
                            ((ObjectNode) messageNode).putNull("content");
                        }
                    }

                    String modifiedBody = mapper.writeValueAsString(rootNode);
                    response.setBody(modifiedBody);
                } catch (Exception e) {
                    throw new RuntimeException("Failed to modify response", e);
                }

                return response;
            };
        }

    }

}
