package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.support.ContentType;
import io.github.sashirestela.openai.support.Constant;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleOpenAIAzureTest {

    @Test
    void shouldPrepareBaseOpenSimpleAIArgsCorrectly() {
        var args = SimpleOpenAIAzure.prepareBaseSimpleOpenAIArgs("the-api-key", "https://example.org", "12-34-5678",
                HttpClient.newHttpClient(), new ObjectMapper());

        assertEquals("https://example.org", args.getBaseUrl());
        assertEquals(1, args.getHeaders().size());
        assertEquals("the-api-key", args.getHeaders().get(Constant.AZURE_APIKEY_HEADER));
        assertNotNull(args.getHttpClient());
        assertNotNull(args.getObjectMapper());
        assertNotNull(args.getRequestInterceptor());
    }

    @Test
    void shouldInterceptUrlCorrectlyWhenBodyIsJson() {
        var baseUrl = "https://example.org/openai/deployments/some-deployment";
        var request = HttpRequestData.builder()
                .url(baseUrl + "/chat/completions")
                .contentType(ContentType.APPLICATION_JSON)
                .headers(Map.of(Constant.AZURE_APIKEY_HEADER, "the-api-key"))
                .body("{\"messages\":[],\"model\":\"model1\",\"stream\":false}")
                .build();
        var expectedRequest = HttpRequestData.builder()
                .url(baseUrl + "/chat/completions?" + Constant.AZURE_API_VERSION + "=12-34-5678")
                .contentType(ContentType.APPLICATION_JSON)
                .headers(Map.of(Constant.AZURE_APIKEY_HEADER, "the-api-key"))
                .body("{\"messages\":[],\"stream\":false}")
                .build();
        var args = SimpleOpenAIAzure.prepareBaseSimpleOpenAIArgs(
                "the-api-key",
                baseUrl,
                "12-34-5678",
                null,
                null);
        var actualRequest = args.getRequestInterceptor().apply(request);
        assertEquals(expectedRequest.getUrl(), actualRequest.getUrl());
        assertEquals(expectedRequest.getContentType(), actualRequest.getContentType());
        assertEquals(expectedRequest.getHeaders(), actualRequest.getHeaders());
        assertEquals(expectedRequest.getBody(), actualRequest.getBody());
    }

    @Test
    void shouldInterceptUrlCorrectlyWhenBodyIsMap() {
        Map<String, Object> data = new HashMap<>();
        data.put("model", "model1");

        var request = HttpRequestData.builder()
                .url("https://example.org/v1/endpoint")
                .contentType(ContentType.MULTIPART_FORMDATA)
                .headers(Map.of(Constant.AZURE_APIKEY_HEADER, "the-api-key"))
                .body(data)
                .build();
        var expectedRequest = HttpRequestData.builder()
                .url("https://example.org/endpoint?" + Constant.AZURE_API_VERSION + "=12-34-5678")
                .contentType(ContentType.MULTIPART_FORMDATA)
                .headers(Map.of(Constant.AZURE_APIKEY_HEADER, "the-api-key"))
                .body(Map.of())
                .build();
        var args = SimpleOpenAIAzure.prepareBaseSimpleOpenAIArgs(
                "the-api-key",
                "https://example.org",
                "12-34-5678",
                null,
                null);
        var actualRequest = args.getRequestInterceptor().apply(request);
        assertEquals(expectedRequest.getUrl(), actualRequest.getUrl());
        assertEquals(expectedRequest.getContentType(), actualRequest.getContentType());
        assertEquals(expectedRequest.getHeaders(), actualRequest.getHeaders());
        assertEquals(expectedRequest.getBody(), actualRequest.getBody());
    }

    @Test
    void shouldInterceptUrlCorrectlyWhenUrlContainsAssistants() {
        var baseUrl = "https://example.org/openai/deployments/some-deployment/assistants/some-assistant";
        var args = SimpleOpenAIAzure.prepareBaseSimpleOpenAIArgs(
                "the-api-key",
                baseUrl,
                "12-34-5678",
                null,
                null);

        var requestBuilder = HttpRequestData.builder()
                .url(baseUrl)
                .headers(Map.of(Constant.AZURE_APIKEY_HEADER, "the-api-key"))
                .body("{\"model\":\"n/a\"}");

        var expectedRequestBuilder = HttpRequestData.builder()
                .url("https://example.org/openai/assistants/some-assistant?" + Constant.AZURE_API_VERSION
                        + "=12-34-5678")
                .headers(Map.of(Constant.AZURE_APIKEY_HEADER, "the-api-key"))
                .body("{\"model\":\"some-deployment\"}");

        // Test with json
        var request = requestBuilder.contentType(ContentType.APPLICATION_JSON).build();
        var expectedRequest = expectedRequestBuilder.contentType(ContentType.APPLICATION_JSON).build();
        var actualRequest = args.getRequestInterceptor().apply(request);
        assertEquals(expectedRequest.getUrl(), actualRequest.getUrl());
        assertEquals(expectedRequest.getContentType(), actualRequest.getContentType());
        assertEquals(expectedRequest.getHeaders(), actualRequest.getHeaders());
        assertEquals(expectedRequest.getBody(), actualRequest.getBody());

        // Test with multipart form data
        var body = new HashMap<>();
        body.put("model", "n/a");
        request = requestBuilder
                .contentType(ContentType.MULTIPART_FORMDATA)
                .body(body)
                .build();
        expectedRequest = expectedRequestBuilder
                .contentType(ContentType.MULTIPART_FORMDATA)
                .body(Map.of("model", "some-deployment"))
                .build();
        actualRequest = args.getRequestInterceptor().apply(request);
        assertEquals(expectedRequest.getUrl(), actualRequest.getUrl());
        assertEquals(expectedRequest.getContentType(), actualRequest.getContentType());
        assertEquals(expectedRequest.getHeaders(), actualRequest.getHeaders());
        assertEquals(expectedRequest.getBody(), actualRequest.getBody());
    }

    @Test
    void shouldCreateEndpoints() {
        var openAI = SimpleOpenAIAzure.builder()
                .apiKey("apiKey")
                .baseUrl("baseUrl")
                .apiVersion("apiVersion")
                .build();
        assertNotNull(openAI.chatCompletions());
        assertNotNull(openAI.files());
    }

    @Test
    void shouldThrownExceptionWhenCallingUnimplementedMethods() {
        var openAI = SimpleOpenAIAzure.builder()
                .apiKey("apiKey")
                .baseUrl("baseUrl")
                .apiVersion("apiVersion")
                .build();
        Runnable[] callingData = {
                openAI::audios,
                openAI::batches,
                openAI::completions,
                openAI::embeddings,
                openAI::fineTunings,
                openAI::images,
                openAI::models,
                openAI::moderations,
                openAI::assistants,
                openAI::threads,
                openAI::threadMessages,
                openAI::threadRuns,
                openAI::threadRunSteps,
                openAI::vectorStores,
                openAI::vectorStoreFiles,
                openAI::vectorStoreFileBatches
        };
        for (Runnable calling : callingData) {
            assertThrows(UnsupportedOperationException.class, () -> calling.run());
        }
    };

}
