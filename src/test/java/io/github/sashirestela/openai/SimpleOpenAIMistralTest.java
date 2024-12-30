package io.github.sashirestela.openai;

import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.support.ContentType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimpleOpenAIMistralTest {

    @Test
    void shouldCreateEndpoints() {
        var openAI = SimpleOpenAIMistral.builder()
                .apiKey("apiKey")
                .baseUrl("baseUrl")
                .build();
        assertNotNull(openAI.chatCompletions());
        assertNotNull(openAI.embeddings());
        assertNotNull(openAI.models());
    }

    @Test
    void shouldInterceptRequest() {
        var actualBody = readJsonFile("src/test/resources/mistral_body_actual.json");
        var actualRequest = HttpRequestData.builder()
                .url("url")
                .contentType(ContentType.APPLICATION_JSON)
                .body(actualBody)
                .build();
        var expectedBody = readJsonFile("src/test/resources/mistral_body_expected.json");
        var expectedRequest = HttpRequestData.builder()
                .url("url")
                .contentType(ContentType.APPLICATION_JSON)
                .body(expectedBody)
                .build();
        var clientConfig = SimpleOpenAIMistral.MistralConfigurator.builder()
                .apiKey("apiKey")
                .baseUrl("url")
                .build()
                .buildConfig();
        actualRequest = clientConfig.getRequestInterceptor().apply(actualRequest);
        assertEquals(expectedRequest.getBody(), actualRequest.getBody());
    }

    private String readJsonFile(String filePath) {
        String json;
        try {
            json = Files.readAllLines(Paths.get(filePath)).get(0);
        } catch (IOException e) {
            json = null;
        }
        return json;
    }

}
