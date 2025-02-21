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
import io.github.sashirestela.openai.exception.SimpleOpenAIException;
import io.github.sashirestela.openai.service.ChatCompletionServices;
import io.github.sashirestela.openai.support.Constant;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * The Gemini Vertex implementation which implements a subset of the standard services.
 */
public class SimpleOpenAIGeminiVertex extends OpenAIProvider implements
        ChatCompletionServices {

    /**
     * Constructor used to generate a builder.
     *
     * @param baseUrl        The base URL of the Gemini Vertex API. Mandatory.
     * @param apiKeyProvider Provides the API key to be used for authentication. Mandatory.
     * @param clientAdapter  Component to make http services. If none is passed the
     *                       JavaHttpClientAdapter will be used. Optional.
     * @param retryConfig    Configuration for request retrying. If not provided, default values will be
     *                       used. Optional.
     * @param objectMapper   Provides JSON conversions either to and from objects. Optional.
     */
    @Builder
    public SimpleOpenAIGeminiVertex(@NonNull String baseUrl, @NonNull Supplier<String> apiKeyProvider,
            HttpClientAdapter clientAdapter, RetryConfig retryConfig,
            ObjectMapper objectMapper) {
        super(GeminiVertexConfigurator.builder()
                .apiKeyProvider(apiKeyProvider)
                .baseUrl(baseUrl)
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
    static class GeminiVertexConfigurator extends OpenAIConfigurator {

        private final Supplier<String> apiKeyProvider;

        @Override
        public ClientConfig buildConfig() {
            return ClientConfig.builder()
                    .baseUrl(baseUrl)
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
                    throw new SimpleOpenAIException("Failed to modify response", null, e);
                }

                return response;
            };
        }

    }

}
