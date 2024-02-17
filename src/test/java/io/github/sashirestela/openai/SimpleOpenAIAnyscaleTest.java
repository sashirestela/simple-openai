package io.github.sashirestela.openai;

import static io.github.sashirestela.openai.SimpleOpenAIAnyscale.AUTHORIZATION_HEADER;
import static io.github.sashirestela.openai.SimpleOpenAIAnyscale.BEARER_AUTHORIZATION;
import static io.github.sashirestela.openai.SimpleOpenAIAnyscale.DEFAULT_BASE_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.net.http.HttpClient;
import org.junit.jupiter.api.Test;

class SimpleOpenAIAnyscaleTest {
    @Test
    void shouldPrepareBaseOpenSimpleAIArgsCorrectlyWithCustomBaseURL() {
        var args = SimpleOpenAIAnyscale.prepareBaseSimpleOpenAIArgs(
            "the-api-key",
            "https://example.org",
            HttpClient.newHttpClient());

        assertEquals("https://example.org", args.getBaseUrl());
        assertEquals(1, args.getHeaders().size());
        assertEquals(BEARER_AUTHORIZATION + "the-api-key", args.getHeaders().get(AUTHORIZATION_HEADER));
        assertNotNull(args.getHttpClient());

        // No request interceptor for SimpleOpenAIAnyscale
        assertNull(args.getRequestInterceptor());
    }

    @Test
    void shouldPrepareBaseOpenSimpleAIArgsCorrectlyWithDefaultBaseURL() {
        var args = SimpleOpenAIAnyscale.prepareBaseSimpleOpenAIArgs(
            "the-api-key",
            null,
            HttpClient.newHttpClient());

        assertEquals(SimpleOpenAIAnyscale.DEFAULT_BASE_URL, args.getBaseUrl());
        assertEquals(1, args.getHeaders().size());
        assertEquals(BEARER_AUTHORIZATION + "the-api-key", args.getHeaders().get(AUTHORIZATION_HEADER));
        assertNotNull(args.getHttpClient());

        // No request interceptor for SimpleOpenAIAnyscale
        assertNull(args.getRequestInterceptor());
    }

}
