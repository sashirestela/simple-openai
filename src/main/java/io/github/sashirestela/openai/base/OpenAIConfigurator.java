package io.github.sashirestela.openai.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.net.http.HttpClient;

@SuperBuilder
@AllArgsConstructor
public abstract class OpenAIConfigurator {

    protected String apiKey;
    protected String baseUrl;
    protected HttpClient httpClient;
    protected ObjectMapper objectMapper;

    public abstract ClientConfig buildConfig();

}
