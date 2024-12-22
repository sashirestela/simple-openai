package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.chat.ChatRequest.Modality;
import io.github.sashirestela.openai.domain.realtime.RealtimeSession;

public class SessionTokenDemo extends AbstractDemo {

    public void demoCallCreateSessionToken() {
        var sessionRequest = RealtimeSession.builder()
                .model("gpt-4o-mini-realtime-preview")
                .modality(Modality.TEXT)
                .modality(Modality.AUDIO)
                .instructions("You are a friendly assistant.")
                .build();
        var sessionResponse = openAI.sessionTokens().create(sessionRequest).join();
        System.out.println(sessionResponse);
    }

    public static void main(String[] args) {
        var demo = new SessionTokenDemo();

        demo.addTitleAction("Call Session Token Creation", demo::demoCallCreateSessionToken);

        demo.run();
    }

}
