package io.github.sashirestela.openai.demo;

public class ChatAnyscaleDemo extends ChatDemo {

    public ChatAnyscaleDemo(String provider, String model) {
        super(provider, model, null);
    }

    public static void main(String[] args) {
        var demo = new ChatAnyscaleDemo("anyscale", "mistralai/Mixtral-8x7B-Instruct-v0.1");

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);

        demo.run();
    }

}
