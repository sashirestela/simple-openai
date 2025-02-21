package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.common.Usage;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartImageUrl;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartImageUrl.ImageUrl;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartText;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.support.Base64Util;
import io.github.sashirestela.openai.support.Base64Util.MediaType;
import io.github.sashirestela.openai.support.GeminiAccessToken;

import java.util.ArrayList;
import java.util.List;

public class ChatGeminiVertexDemo extends ChatDemo {

    static GeminiAccessToken geminiAccessToken = new GeminiAccessToken(System.getenv("GEMINI_VERTEX_SA_CREDS_PATH"));

    ArrayList<Usage> localImageUageList = new ArrayList<>();
    ArrayList<String> localImageResponseList = new ArrayList<>();

    public ChatGeminiVertexDemo(String model) {
        super("gemini_vertex", model, null);
        this.chatProvider = this.openAIGeminiVertex;
    }

    static String getApiKey() {
        return geminiAccessToken.get();
    }

    void processLocalImageResponseChunk(Chat responseChunk) {
        var choices = responseChunk.getChoices();
        if (!choices.isEmpty()) {
            var delta = choices.get(0).getMessage();
            if (delta.getContent() != null) {
                localImageResponseList.add(delta.getContent());
            }
        }
        var usage = responseChunk.getUsage();
        if (usage != null && usage.getCompletionTokens() != 0) {  // completionTokens condition for GeminiGoogle
            localImageUageList.add(usage);
        }
    }

    @Override
    public void demoCallChatWithVisionLocalImage() {
        chatRequest = ChatRequest.builder()
                .model(model)
                .messages(List.of(
                        UserMessage.of(List.of(
                                ContentPartText.of(
                                        "What do you see in the image? Give in details in no more than 100 words."),
                                ContentPartImageUrl.of(ImageUrl.of(
                                        Base64Util.encode("src/demo/resources/machupicchu.jpg", MediaType.IMAGE)))))))
                .temperature(0.0)
                .maxTokens(500)
                .build();
        var chatResponse = chatProvider.chatCompletions().createStream(chatRequest).join();
        chatResponse.forEach(this::processLocalImageResponseChunk);

        var response = String.join("", localImageResponseList);
        var promptTokens = localImageUageList.stream().map(Usage::getPromptTokens).reduce(0, Integer::sum);
        var completionTokens = localImageUageList.stream().map(Usage::getCompletionTokens).reduce(0, Integer::sum);
        var totalTokens = promptTokens + completionTokens;

        System.out.println(response);
        System.out.println("Prompt tokens: " + promptTokens + ", Completion tokens: " + completionTokens
                + ", Total tokens: " + totalTokens);
        System.out.println();
    }

    public static void main(String[] args) {
        var demo = new ChatGeminiVertexDemo("google/gemini-1.5-flash");

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);
        demo.addTitleAction("Call Chat with Vision (Local image)", demo::demoCallChatWithVisionLocalImage);
        demo.addTitleAction("Call Chat with Structured Outputs", demo::demoCallChatWithStructuredOutputs);
        demo.addTitleAction("Call Chat with Structured Outputs 2", demo::demoCallChatWithStructuredOutputs2);

        demo.run();
    }

}
