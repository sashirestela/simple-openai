package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.BaseSimpleOpenAI;
import io.github.sashirestela.openai.SimpleOpenAIAnyscale;

public class ChatAnyscaleDemo extends ChatDemo {

    public ChatAnyscaleDemo(BaseSimpleOpenAI openAI, String model) {
        super(openAI, model);
    }

    public static void main(String[] args) {
        var openAI = SimpleOpenAIAnyscale.builder()
                .apiKey(System.getenv("ANYSCALE_API_KEY"))
                .build();
        var demo = new ChatAnyscaleDemo(openAI, "mistralai/Mixtral-8x7B-Instruct-v0.1");

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);
        //demo.addTitleAction("Call Chat with Vision (External image)", demo::demoCallChatWithVisionExternalImage);
        //demo.addTitleAction("Call Chat with Vision (Local image)", demo::demoCallChatWithVisionLocalImage);
        //demo.addTitleAction("Call Chat with Structured Outputs", demo::demoCallChatWithStructuredOutputs);

        demo.run();
    }

}
