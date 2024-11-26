package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.support.Constant;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SimpleOpenAIAnyscaleTest {

    @Test
    void shouldPrepareBaseOpenSimpleAIArgsCorrectlyWithCustomBaseURL() {
        var args = SimpleOpenAIAnyscale.prepareBaseSimpleOpenAIArgs("the-api-key", "https://example.org",
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
        var args = SimpleOpenAIAnyscale.prepareBaseSimpleOpenAIArgs("the-api-key", null, null, null);

        assertEquals(Constant.ANYSCALE_BASE_URL, args.getBaseUrl());
        assertEquals(1, args.getHeaders().size());
        assertEquals(Constant.BEARER_AUTHORIZATION + "the-api-key",
                args.getHeaders().get(Constant.AUTHORIZATION_HEADER));
        assertNull(args.getHttpClient());
        assertNull(args.getObjectMapper());
        assertNull(args.getRequestInterceptor());
    }

    @Test
    void shouldThrownExceptionWhenCallingUnimplementedMethods() {
        var openAI = SimpleOpenAIAnyscale.builder()
                .apiKey("api-key-test")
                .build();
        Runnable[] callingData = {
                openAI::audios,
                openAI::batches,
                openAI::completions,
                openAI::embeddings,
                openAI::files,
                openAI::fineTunings,
                openAI::images,
                openAI::models,
                openAI::moderations,
                openAI::uploads,
                openAI::assistants,
                openAI::threads,
                openAI::threadMessages,
                openAI::threadRuns,
                openAI::threadRunSteps,
                openAI::vectorStores,
                openAI::vectorStoreFiles,
                openAI::vectorStoreFileBatches,
                openAI::realtime
        };
        for (Runnable calling : callingData) {
            assertThrows(UnsupportedOperationException.class, () -> calling.run());
        }
    };

}
