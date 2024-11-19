package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.BaseSimpleOpenAI;
import io.github.sashirestela.openai.SimpleOpenAIAzure;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartImageUrl;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartImageUrl.ImageUrl;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartText;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.support.Base64Util;
import io.github.sashirestela.openai.support.Base64Util.MediaType;

import java.util.List;

public class ChatAzureDemo extends ChatDemo {

    public ChatAzureDemo(BaseSimpleOpenAI openAI, String model) {
        super(openAI, model, null);
    }

    @Override
    public void demoCallChatWithVisionExternalImage() {
        var chatRequest = ChatRequest.builder()
                .model(model)
                .messages(List.of(
                        UserMessage.of(List.of(
                                ContentPartText.of(
                                        "What do you see in the image? Give in details in no more than 100 words."),
                                ContentPartImageUrl.of(ImageUrl.of(
                                        "https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg"))))))
                .temperature(0.0)
                .maxCompletionTokens(500)
                .build();
        var chatResponse = openAI.chatCompletions().create(chatRequest).join();
        System.out.println(chatResponse.firstContent());
    }

    @Override
    public void demoCallChatWithVisionLocalImage() {
        var chatRequest = ChatRequest.builder()
                .model(model)
                .messages(List.of(
                        UserMessage.of(List.of(
                                ContentPartText.of(
                                        "What do you see in the image? Give in details in no more than 100 words."),
                                ContentPartImageUrl.of(ImageUrl.of(
                                        Base64Util.encode("src/demo/resources/machupicchu.jpg", MediaType.IMAGE)))))))
                .temperature(0.0)
                .maxCompletionTokens(500)
                .build();
        var chatResponse = openAI.chatCompletions().create(chatRequest).join();
        System.out.println(chatResponse.firstContent());
    }

    public static void main(String[] args) {
        var openAI = SimpleOpenAIAzure.builder()
                .apiKey(System.getenv("AZURE_OPENAI_API_KEY"))
                .apiVersion(System.getenv("AZURE_OPENAI_API_VERSION"))
                .baseUrl(System.getenv("AZURE_OPENAI_BASE_URL"))
                .build();
        var demo = new ChatAzureDemo(openAI, "N/A");

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);
        demo.addTitleAction("Call Chat with Vision (External image)", demo::demoCallChatWithVisionExternalImage);
        demo.addTitleAction("Call Chat with Vision (Local image)", demo::demoCallChatWithVisionLocalImage);
        demo.addTitleAction("Call Chat with Structured Outputs", demo::demoCallChatWithStructuredOutputs);
        demo.addTitleAction("Call Chat with Structured Outputs 2", demo::demoCallChatWithStructuredOutputs2);

        demo.run();
    }

}
