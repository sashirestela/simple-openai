package io.github.sashirestela.openai.demo;

public class ChatGeminiGoogleDemo extends ChatDemo {

    public ChatGeminiGoogleDemo(String model) {
        super("gemini_google", model, null);
        this.chatProvider = this.openAIGeminiGoogle;
    }

    public static void main(String[] args) {
        var demo = new ChatGeminiGoogleDemo("gemini-1.5-flash");

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);
        demo.addTitleAction("Call Chat with Vision (Local image)", demo::demoCallChatWithVisionLocalImage);
        demo.addTitleAction("Call Chat with Structured Outputs", demo::demoCallChatWithStructuredOutputs);

        demo.run();
    }

}
