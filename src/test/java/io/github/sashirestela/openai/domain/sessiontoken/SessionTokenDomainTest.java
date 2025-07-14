package io.github.sashirestela.openai.domain.sessiontoken;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.chat.ChatRequest.Modality;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession.EagernessType;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession.InputAudioNoiseReduction;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession.NoiseReductionType;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession.SecretConfig;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession.TurnDetection;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession.TurnDetectionType;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession.VoiceRealtime;
import io.github.sashirestela.openai.domain.realtime.RealtimeTranscriptionSession;
import io.github.sashirestela.openai.domain.realtime.RealtimeTranscriptionSession.ItemsToInclude;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;

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
                .clientSecret(SecretConfig.of(10))
                .model("gpt-4o-mini-realtime-preview")
                .modality(Modality.TEXT)
                .modality(Modality.AUDIO)
                .maxResponseOutputTokens(300)
                .instructions("You are a friendly assistant.")
                .voice(VoiceRealtime.CORAL)
                .build();
        var sessionResponse = openAI.sessionTokens().create(sessionRequest).join();
        System.out.println(sessionResponse);
        System.out.println(sessionResponse.getClientSecretAtResponse());
        assertNotNull(sessionResponse);
    }

    @Test
    void testTranscriptionSessionTokenCreate() throws IOException {
        DomainTestingHelper.get()
                .mockForObject(httpClient, "src/test/resources/sessiontokens_transcription_create.json");
        var transSessionRequest = RealtimeTranscriptionSession.builder()
                .clientSecret(SecretConfig.of(10))
                .include(List.of(ItemsToInclude.LOGPROBS))
                .inputAudioNoiseReduction(InputAudioNoiseReduction.of(NoiseReductionType.NEAR_FIELD))
                .turnDetection(TurnDetection.builder()
                        .eagerness(EagernessType.MEDIUM)
                        .type(TurnDetectionType.SEMANTIC_VAD)
                        .build())
                .build();
        var transSessionResponse = openAI.sessionTokens().create(transSessionRequest).join();
        System.out.println(transSessionResponse);
        System.out.println(transSessionResponse.getClientSecretAtResponse());
        assertNotNull(transSessionResponse);
    }

}
