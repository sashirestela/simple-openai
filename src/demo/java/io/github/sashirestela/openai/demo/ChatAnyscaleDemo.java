package io.github.sashirestela.openai.demo;

public class ChatAnyscaleDemo extends ChatDemo {

    public ChatAnyscaleDemo(String model) {
        super("anyscale", model, null);
        this.chatProvider = this.openAIAnyscale;
    }

    public static void main(String[] args) {
        var demo = new ChatAnyscaleDemo("mistralai/Mixtral-8x7B-Instruct-v0.1");

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);

        demo.run();
    }

}
