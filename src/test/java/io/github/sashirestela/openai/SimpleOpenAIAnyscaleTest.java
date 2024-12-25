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
    void shouldPrepareBaseOpenSimpleAIArgsCorrectlyWithCustomBaseURL() {
        var args = SimpleOpenAIAnyscale.buildConfig("the-api-key", "https://example.org",
                HttpClient.newHttpClient(), new ObjectMapper());

        assertEquals("https://example.org", args.getBaseUrl());
        assertEquals(1, args.getHeaders().size());
        assertEquals(Constant.BEARER_AUTHORIZATION + "the-api-key",
                args.getHeaders().get(Constant.AUTHORIZATION_HEADER));
        assertNotNull(args.getHttpClient());
        assertNotNull(args.getObjectMapper());
        assertNull(args.getRequestInterceptor());
    }

    @Test
    void shouldPrepareBaseOpenSimpleAIArgsCorrectlyWithOnlyApiKey() {
        var args = SimpleOpenAIAnyscale.buildConfig("the-api-key", null, null, null);

        assertEquals(Constant.ANYSCALE_BASE_URL, args.getBaseUrl());
        assertEquals(1, args.getHeaders().size());
        assertEquals(Constant.BEARER_AUTHORIZATION + "the-api-key",
                args.getHeaders().get(Constant.AUTHORIZATION_HEADER));
        assertNull(args.getHttpClient());
        assertNull(args.getObjectMapper());
        assertNull(args.getRequestInterceptor());
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
