package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.support.ContentType;
import io.github.sashirestela.openai.support.Constant;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SimpleOpenAIGeminiGoogleTest {

    @Test
    void shouldCreateEndpoints() {
        var openAI = SimpleOpenAIGeminiGoogle.builder()
                .apiKey("apiKey")
                .baseUrl("baseUrl")
                .build();
        assertNotNull(openAI.chatCompletions());
        assertNotNull(openAI.embeddings());
    }

    @Test
    void shouldInterceptUrlCorrectlyWhenBodyIsJson() {
        var baseUrl = "https://example.org/v1beta/openai";
        var request = HttpRequestData.builder()
                .url(baseUrl + "/v1/chat/completions")
                .contentType(ContentType.APPLICATION_JSON)
                .headers(Map.of(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + "apiKey"))
                .body("{\"messages\":[],\"model\":\"model1\",\"stream\":false}")
                .build();
        var expectedRequest = HttpRequestData.builder()
                .url(baseUrl + "/chat/completions")
                .contentType(ContentType.APPLICATION_JSON)
                .headers(Map.of(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + "apiKey"))
                .body("{\"messages\":[],\"model\":\"model1\",\"stream\":false}")
                .build();
        var clientConfig = SimpleOpenAIGeminiGoogle.GeminiGoogleConfigurator.builder()
                .apiKey("apiKey")
                .baseUrl(baseUrl)
                .build()
                .buildConfig();
        var actualRequest = clientConfig.getRequestInterceptor().apply(request);
        assertEquals(expectedRequest.getUrl(), actualRequest.getUrl());
    }

}
