package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.support.Constant;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class SimpleOpenAIAnyscaleTest {

    @Test
    void shouldCreateFullConfigWhenAllParametersArePassed() {
        var clientConfig = SimpleOpenAIAnyscale.AnyscaleConfigurator.builder()
                .apiKey("apiKey")
                .baseUrl("https://example.org")
                .httpClient(HttpClient.newHttpClient())
                .objectMapper(new ObjectMapper())
                .build()
                .buildConfig();

        assertEquals("https://example.org", clientConfig.getBaseUrl());
        assertEquals(1, clientConfig.getHeaders().size());
        assertEquals(Constant.BEARER_AUTHORIZATION + "apiKey",
                clientConfig.getHeaders().get(Constant.AUTHORIZATION_HEADER));
        assertNotNull(clientConfig.getHttpClient());
        assertNotNull(clientConfig.getObjectMapper());
        assertNull(clientConfig.getRequestInterceptor());
    }

    @Test
    void shouldCreateConfigWithDefaultValuesWhenRequiredParametersArePassed() {
        var clientConfig = SimpleOpenAIAnyscale.AnyscaleConfigurator.builder()
                .apiKey("apiKey")
                .build()
                .buildConfig();

        assertEquals(Constant.ANYSCALE_BASE_URL, clientConfig.getBaseUrl());
        assertEquals(1, clientConfig.getHeaders().size());
        assertEquals(Constant.BEARER_AUTHORIZATION + "apiKey",
                clientConfig.getHeaders().get(Constant.AUTHORIZATION_HEADER));
        assertNull(clientConfig.getHttpClient());
        assertNull(clientConfig.getObjectMapper());
        assertNull(clientConfig.getRequestInterceptor());
    }

    @Test
    void shouldCreateEndpoints() {
        var openAI = SimpleOpenAIAnyscale.builder()
                .apiKey("apiKey")
                .baseUrl("baseUrl")
                .build();
        assertNotNull(openAI.chatCompletions());
    }

}
