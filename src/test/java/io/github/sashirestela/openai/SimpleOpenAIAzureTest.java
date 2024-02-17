package io.github.sashirestela.openai;

import static io.github.sashirestela.openai.SimpleOpenAIAzure.API_KEY_HEADER;
import static io.github.sashirestela.openai.SimpleOpenAIAzure.API_VERSION;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.support.ContentType;
import java.net.http.HttpClient;
import java.util.Map;
import org.junit.jupiter.api.Test;

class SimpleOpenAIAzureTest {


    @Test
    void shouldPrepareBaseOpenSimpleAIArgsCorrectly() {
        var args = SimpleOpenAIAzure.prepareBaseSimpleOpenAIArgs(
            "the-api-key",
            "https://example.org",
            "12-34-5678",
            HttpClient.newHttpClient());

        assertEquals("https://example.org", args.getBaseUrl());
        assertEquals(1, args.getHeaders().size());
        assertEquals("the-api-key", args.getHeaders().get(API_KEY_HEADER));
        assertNotNull(args.getHttpClient());
        assertNotNull(args.getRequestInterceptor());
    }

    @Test
    void shouldInterceptUrlCorrectly() {
        var request = HttpRequestData.builder()
            .url("https://example.org/v1/endpoint")
            .contentType(ContentType.APPLICATION_JSON)
            .headers(Map.of(API_KEY_HEADER, "the-api-key"))
            .body("{\"model\":\"model1\"}")
            .build();
        var expectedRequest = HttpRequestData.builder()
            .url("https://example.org/endpoint?" + API_VERSION + "=12-34-5678")
            .contentType(ContentType.APPLICATION_JSON)
            .headers(Map.of(API_KEY_HEADER, "the-api-key"))
            .body("{}")
            .build();
        var args = SimpleOpenAIAzure.prepareBaseSimpleOpenAIArgs(
            "the-api-key",
            "https://example.org",
            "12-34-5678",
            null);
        var actualRequest = args.getRequestInterceptor().apply(request);
        assertEquals(expectedRequest.getUrl()   , actualRequest.getUrl());
        assertEquals(expectedRequest.getContentType(), actualRequest.getContentType());
        assertEquals(expectedRequest.getHeaders(), actualRequest.getHeaders());
        assertEquals(expectedRequest.getBody(), actualRequest.getBody());
    }
}
