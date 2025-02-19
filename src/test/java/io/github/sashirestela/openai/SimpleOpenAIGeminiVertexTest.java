package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.http.HttpResponseData;
import io.github.sashirestela.cleverclient.support.ContentType;
import io.github.sashirestela.openai.exception.SimpleOpenAIException;
import io.github.sashirestela.openai.support.Constant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.function.UnaryOperator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleOpenAIGeminiVertexTest {

    UnaryOperator<HttpResponseData> responseInterceptor;

    @BeforeEach
    void setUp() {
        var configurator = SimpleOpenAIGeminiVertex.GeminiVertexConfigurator.builder()
                .apiKeyProvider(() -> "apiKey")
                .baseUrl("baseUrl")
                .build();

        var clientConfig = configurator.buildConfig();
        responseInterceptor = clientConfig.getResponseInterceptor();
    }

    @Test
    void shouldCreateEndpoints() {
        var openAI = SimpleOpenAIGeminiVertex.builder()
                .apiKeyProvider(() -> "apiKey")
                .baseUrl("baseUrl")
                .build();
        assertNotNull(openAI.chatCompletions());
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
        var clientConfig = SimpleOpenAIGeminiVertex.GeminiVertexConfigurator.builder()
                .apiKeyProvider(() -> "apiKey")
                .baseUrl(baseUrl)
                .build()
                .buildConfig();
        var actualRequest = clientConfig.getRequestInterceptor().apply(request);
        assertEquals(expectedRequest.getUrl(), actualRequest.getUrl());
    }

    @Test
    void shouldNotModifyResponseIfBodyIsNull() {
        var response = HttpResponseData.builder().body(null).build();
        var modifiedResponse = responseInterceptor.apply(response);

        assertNull(modifiedResponse.getBody());
    }

    @Test
    void shouldNotModifyResponseIfChoicesArrayIsEmpty() {
        var responseBody = "{ \"choices\": [] }";

        var response = HttpResponseData.builder().body(responseBody).build();
        var modifiedResponse = responseInterceptor.apply(response);

        assertEquals(responseBody.replaceAll("\\s+", ""), modifiedResponse.getBody().replaceAll("\\s+", ""));
    }

    @Test
    void shouldModifyResponseIfChoicesContainMessageWithArrayContent() {
        var responseBody = "{ \"choices\": [ { \"message\": { \"content\": [\"Step 1\", \"Step 2\"] } } ] }";
        var expectedResponse = "{ \"choices\": [ { \"message\": { \"content\": null } } ] }";

        var response = HttpResponseData.builder().body(responseBody).build();
        var modifiedResponse = responseInterceptor.apply(response);

        assertEquals(expectedResponse.replaceAll("\\s+", ""), modifiedResponse.getBody().replaceAll("\\s+", ""));
    }

    @Test
    void shouldNotModifyResponseIfChoicesContainMessageWithNonArrayContent() {
        var responseBody = "{ \"choices\": [ { \"message\": { \"content\": \"This is a single string\" } } ] }";

        var response = HttpResponseData.builder().body(responseBody).build();
        var modifiedResponse = responseInterceptor.apply(response);

        assertEquals(responseBody.replaceAll("\\s+", ""), modifiedResponse.getBody().replaceAll("\\s+", ""));
    }

    @Test
    void shouldThrowExceptionIfJsonParsingFails() {
        var response = HttpResponseData.builder().body("{ invalid json }").build();

        assertThrows(SimpleOpenAIException.class, () -> responseInterceptor.apply(response));
    }

}
