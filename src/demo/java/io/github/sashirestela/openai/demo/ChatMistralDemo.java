package io.github.sashirestela.openai.demo;

public class ChatMistralDemo extends ChatDemo {

    public ChatMistralDemo(String model) {
        super("mistral", model, null);
        this.sleepSeconds = 1;  //Free tier limit: 1 request per 1 second
        this.chatProvider = this.openAIMistral;
    }

    public static void main(String[] args) {
        var demo = new ChatMistralDemo("pixtral-12b-2409");

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);
        demo.addTitleAction("Call Chat with Vision (External image)", demo::demoCallChatWithVisionExternalImage);
        demo.addTitleAction("Call Chat with Vision (Local image)", demo::demoCallChatWithVisionLocalImage);

        demo.run();
    }

}
