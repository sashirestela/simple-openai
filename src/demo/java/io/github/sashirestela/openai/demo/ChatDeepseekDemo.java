package io.github.sashirestela.openai.demo;

public class ChatDeepseekDemo extends ChatDemo {

    public ChatDeepseekDemo(String model) {
        super("deepseek", model, null);
        this.chatProvider = this.openAIDeepseek;
    }

    public static void main(String[] args) {
        var demo = new ChatDeepseekDemo("deepseek-chat");

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);

        demo.run();
    }

}
