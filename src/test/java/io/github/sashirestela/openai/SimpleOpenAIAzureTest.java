package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.http.HttpRequestData;
import io.github.sashirestela.cleverclient.retry.RetryConfig;
import io.github.sashirestela.cleverclient.support.ContentType;
import io.github.sashirestela.openai.support.Constant;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimpleOpenAIAzureTest {

    @Test
    void shouldCreateFullConfigWhenAllParametersArePassed() {
        var clientConfig = SimpleOpenAIAzure.AzureConfigurator.builder()
                .apiKey("apiKey")
                .baseUrl("https://example.org")
                .apiVersion("12-34-5678")
                .httpClient(HttpClient.newHttpClient())
                .retryConfig(RetryConfig.builder().maxAttempts(4).build())
                .objectMapper(new ObjectMapper())
                .build()
                .buildConfig();

        assertEquals("https://example.org", clientConfig.getBaseUrl());
        assertEquals(1, clientConfig.getHeaders().size());
        assertEquals("apiKey", clientConfig.getHeaders().get(Constant.AZURE_APIKEY_HEADER));
        assertNotNull(clientConfig.getHttpClient());
        assertNotNull(clientConfig.getRetryConfig());
        assertNotNull(clientConfig.getObjectMapper());
        assertNotNull(clientConfig.getRequestInterceptor());
    }

    @Test
    void shouldInterceptUrlCorrectlyWhenBodyIsJson() {
        var baseUrl = "https://example.org/openai/deployments/some-deployment";
        var request = HttpRequestData.builder()
                .url(baseUrl + "/chat/completions")
                .contentType(ContentType.APPLICATION_JSON)
                .headers(Map.of(Constant.AZURE_APIKEY_HEADER, "apiKey"))
                .body("{\"messages\":[],\"model\":\"model1\",\"stream\":false}")
                .build();
        var expectedRequest = HttpRequestData.builder()
                .url(baseUrl + "/chat/completions?" + Constant.AZURE_API_VERSION + "=12-34-5678")
                .contentType(ContentType.APPLICATION_JSON)
                .headers(Map.of(Constant.AZURE_APIKEY_HEADER, "apiKey"))
                .body("{\"messages\":[],\"stream\":false}")
                .build();
        var clientConfig = SimpleOpenAIAzure.AzureConfigurator.builder()
                .apiKey("apiKey")
                .baseUrl(baseUrl)
                .apiVersion("12-34-5678")
                .build()
                .buildConfig();
        var actualRequest = clientConfig.getRequestInterceptor().apply(request);
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
                .headers(Map.of(Constant.AZURE_APIKEY_HEADER, "apiKey"))
                .body(data)
                .build();
        var expectedRequest = HttpRequestData.builder()
                .url("https://example.org/endpoint?" + Constant.AZURE_API_VERSION + "=12-34-5678")
                .contentType(ContentType.MULTIPART_FORMDATA)
                .headers(Map.of(Constant.AZURE_APIKEY_HEADER, "apiKey"))
                .body(Map.of())
                .build();
        var clientConfig = SimpleOpenAIAzure.AzureConfigurator.builder()
                .apiKey("apiKey")
                .baseUrl("https://example.org")
                .apiVersion("12-34-5678")
                .build()
                .buildConfig();
        var actualRequest = clientConfig.getRequestInterceptor().apply(request);
        assertEquals(expectedRequest.getUrl(), actualRequest.getUrl());
        assertEquals(expectedRequest.getContentType(), actualRequest.getContentType());
        assertEquals(expectedRequest.getHeaders(), actualRequest.getHeaders());
        assertEquals(expectedRequest.getBody(), actualRequest.getBody());
    }

    @Test
    void shouldInterceptUrlCorrectlyWhenUrlContainsAssistants() {
        var baseUrl = "https://example.org/openai/deployments/some-deployment/assistants/some-assistant";
        var clientConfig = SimpleOpenAIAzure.AzureConfigurator.builder()
                .apiKey("apiKey")
                .baseUrl(baseUrl)
                .apiVersion("12-34-5678")
                .build()
                .buildConfig();

        var requestBuilder = HttpRequestData.builder()
                .url(baseUrl)
                .headers(Map.of(Constant.AZURE_APIKEY_HEADER, "apiKey"))
                .body("{\"model\":\"n/a\"}");

        var expectedRequestBuilder = HttpRequestData.builder()
                .url("https://example.org/openai/assistants/some-assistant?" + Constant.AZURE_API_VERSION
                        + "=12-34-5678")
                .headers(Map.of(Constant.AZURE_APIKEY_HEADER, "apiKey"))
                .body("{\"model\":\"some-deployment\"}");

        // Test with json
        var request = requestBuilder.contentType(ContentType.APPLICATION_JSON).build();
        var expectedRequest = expectedRequestBuilder.contentType(ContentType.APPLICATION_JSON).build();
        var actualRequest = clientConfig.getRequestInterceptor().apply(request);
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
        actualRequest = clientConfig.getRequestInterceptor().apply(request);
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
        assertNotNull(openAI.assistants());
        assertNotNull(openAI.threadMessages());
        assertNotNull(openAI.threadRunSteps());
        assertNotNull(openAI.threadRuns());
        assertNotNull(openAI.threads());
        assertNotNull(openAI.vectorStoreFileBatches());
        assertNotNull(openAI.vectorStoreFiles());
        assertNotNull(openAI.vectorStores());
    }

}
