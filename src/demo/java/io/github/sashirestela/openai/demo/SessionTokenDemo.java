package io.github.sashirestela.openai.demo;

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

import java.util.List;

public class SessionTokenDemo extends AbstractDemo {

    public void demoCallCreateSessionToken() {
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
        System.out.println("Reponse: " + sessionResponse);
        System.out.println("Token: " + sessionResponse.getClientSecretAtResponse().getValue());
    }

    public void demoCallCreateTranscriptionSessionToken() {
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
        System.out.println("Reponse: " + transSessionResponse);
        System.out.println("Token: " + transSessionResponse.getClientSecretAtResponse().getValue());
    }

    public static void main(String[] args) {
        var demo = new SessionTokenDemo();

        demo.addTitleAction("Call Session Token Creation", demo::demoCallCreateSessionToken);
        demo.addTitleAction("Call Transcription Session Token Creation", demo::demoCallCreateTranscriptionSessionToken);

        demo.run();
    }

}
