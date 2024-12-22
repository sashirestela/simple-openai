package io.github.sashirestela.openai.domain.sessiontoken;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.chat.ChatRequest.Modality;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class SessionTokenDomainTest {

    static HttpClient httpClient;
    static SimpleOpenAI openAI;

    @BeforeAll
    static void setup() {
        httpClient = mock(HttpClient.class);
        openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
    }

    @Test
    void testSessionTokenCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/sessiontokens_create.json");
        var sessionRequest = RealtimeSession.builder()
                .model("gpt-4o-mini-realtime-preview")
                .modality(Modality.TEXT)
                .modality(Modality.AUDIO)
                .instructions("You are a friendly assistant.")
                .build();
        var sessionResponse = openAI.sessionTokens().create(sessionRequest).join();
        System.out.println(sessionResponse);
        assertNotNull(sessionResponse);
    }

}
