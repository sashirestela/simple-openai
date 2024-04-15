package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.SimpleOpenAIAnyscale;

public class ChatAnyscaleServiceDemo extends BaseChatServiceDemo {

    public ChatAnyscaleServiceDemo(String apiKey) {
        super(SimpleOpenAIAnyscale.builder()
                .apiKey(apiKey)
                .build(), "mistralai/Mixtral-8x7B-Instruct-v0.1", "");
    }

    public static void main(String[] args) {
        var apiKey = System.getenv("ANYSCALE_API_KEY");

        var demo = new ChatAnyscaleServiceDemo(apiKey);

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);

        demo.run();
    }

}
