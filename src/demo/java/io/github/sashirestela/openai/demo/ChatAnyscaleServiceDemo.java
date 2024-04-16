package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.SimpleOpenAIAnyscale;

public class ChatAnyscaleServiceDemo extends BaseChatServiceDemo {

    private static final String NOT_IMPLEMENTED = "Not implemented.";

    public ChatAnyscaleServiceDemo(String apiKey) {
        super(SimpleOpenAIAnyscale.builder()
                .apiKey(apiKey)
                .build(), "mistralai/Mixtral-8x7B-Instruct-v0.1", "");
    }

    /**
     * Throw not implemented
     */
    @Override
    public void demoCallChatWithVisionExternalImageBlocking() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    @Override
    public void demoCallChatWithVisionExternalImageStreaming() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    @Override
    public void demoCallChatWithVisionLocalImageBlocking() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
    }

    /**
     * Throw not implemented
     */
    @Override
    public void demoCallChatWithVisionLocalImageStreaming() {
        throw new UnsupportedOperationException(NOT_IMPLEMENTED);
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
