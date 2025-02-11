package io.github.sashirestela.openai.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.client.HttpClientAdapter;
import io.github.sashirestela.cleverclient.retry.RetryConfig;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.net.http.HttpClient;

@SuperBuilder
@AllArgsConstructor
public abstract class OpenAIConfigurator {

    protected String apiKey;
    protected String baseUrl;
    protected HttpClientAdapter clientAdapter;
    protected RetryConfig retryConfig;
    protected ObjectMapper objectMapper;
    /**
     * @deprecated CleverClient has deprecated this field in favor of clientAdapter.
     */
    @Deprecated(since = "3.16.0", forRemoval = true)
    protected HttpClient httpClient;

    public abstract ClientConfig buildConfig();

}
