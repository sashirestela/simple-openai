package io.github.sashirestela.openai.demo;

public class ChatServiceDemo extends BaseChatServiceDemo {

    public ChatServiceDemo() {
        super("gpt-3.5-turbo-1106", "gpt-4-vision-preview");
    }

    public static void main(String[] args) {
        var demo = new ChatServiceDemo();

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);
        demo.addTitleAction("Call Chat with Vision (External image)",
                demo::demoCallChatWithVisionExternalImageStreaming);
        demo.addTitleAction("Call Chat with Vision (Local image)", demo::demoCallChatWithVisionLocalImageStreaming);

        demo.run();
    }

}
