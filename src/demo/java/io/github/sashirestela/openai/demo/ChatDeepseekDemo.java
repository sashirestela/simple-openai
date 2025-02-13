package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

public class ChatDeepseekDemo extends ChatDemo {

    public ChatDeepseekDemo(String model) {
        super("deepseek", model, null);
        this.chatProvider = this.openAIDeepseek;
    }

    public void demoCallChatShowThinking() {
        var chatResponse = chatProvider.chatCompletions()
                .createStream(ChatRequest.builder()
                        .model("deepseek-reasoner")
                        .message(UserMessage.of("9.11 and 9.8, which is greater?"))
                        .maxTokens(512)
                        .build())
                .join();
        chatResponse.forEach(this::processResponseChunk);
    }

    public static void main(String[] args) {
        var demo = new ChatDeepseekDemo("deepseek-chat");

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat (Show Thinking)", demo::demoCallChatShowThinking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);

        demo.run();
    }

}
