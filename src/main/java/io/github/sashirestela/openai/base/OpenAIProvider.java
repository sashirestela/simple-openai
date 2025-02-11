package io.github.sashirestela.openai.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.CleverClient;
import io.github.sashirestela.cleverclient.client.JavaHttpClientAdapter;
import io.github.sashirestela.cleverclient.retry.RetryConfig;
import io.github.sashirestela.openai.OpenAIRealtime;
import io.github.sashirestela.slimvalidator.Validator;
import io.github.sashirestela.slimvalidator.exception.ConstraintViolationException;
import lombok.NonNull;
import lombok.Setter;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * The abstract class that providers must extend.
 */
public abstract class OpenAIProvider {

    @Setter
    protected CleverClient cleverClient;
    protected OpenAIRealtime realtime;
    private Map<Class<?>, Object> serviceCache = new ConcurrentHashMap<>();

    protected OpenAIProvider(@NonNull OpenAIConfigurator configurator) {
        var clientConfig = configurator.buildConfig();
        this.cleverClient = buildClient(clientConfig);
        this.realtime = buildRealtime(clientConfig);
    }

    @SuppressWarnings("unchecked")
    protected <T> T getOrCreateService(Class<T> serviceClass) {
        return (T) serviceCache.computeIfAbsent(serviceClass, key -> cleverClient.create(serviceClass));
    }

    public void shutDown() {
        this.cleverClient.getClientAdapter().shutdown();
    }

    private CleverClient buildClient(ClientConfig clientConfig) {
        final String END_OF_STREAM = "[DONE]";
        return CleverClient.builder()
                .clientAdapter(Optional.ofNullable(clientConfig.getClientAdapter())
                        // Lazy evaluation to not fail on devices without support for HttpClient
                        .orElseGet(() -> new JavaHttpClientAdapter(Optional.ofNullable(clientConfig.getHttpClient())
                                .orElse(HttpClient.newHttpClient()))))
                .baseUrl(clientConfig.getBaseUrl())
                .headers(clientConfig.getHeaders())
                .bodyInspector(bodyInspector())
                .requestInterceptor(clientConfig.getRequestInterceptor())
                .responseInterceptor(clientConfig.getResponseInterceptor())
                .retryConfig(Optional.ofNullable(clientConfig.getRetryConfig()).orElse(RetryConfig.defaultValues()))
                .endOfStream(END_OF_STREAM)
                .objectMapper(Optional.ofNullable(clientConfig.getObjectMapper()).orElse(new ObjectMapper()))
                .build();
    }

    private OpenAIRealtime buildRealtime(ClientConfig clientConfig) {
        var realtimeConfig = clientConfig.getRealtimeConfig();
        if (realtimeConfig != null) {
            return new OpenAIRealtime(realtimeConfig);
        }
        return null;
    }

    private Consumer<Object> bodyInspector() {
        return body -> {
            var validator = new Validator();
            var violations = validator.validate(body);
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException(violations);
            }
        };
    }

}
