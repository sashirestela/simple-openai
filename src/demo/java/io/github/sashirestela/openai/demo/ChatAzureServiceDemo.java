package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.SimpleOpenAIAzure;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

public class ChatAzureServiceDemo extends BaseChatServiceDemo {

    private ChatRequest chatRequest;

    public ChatAzureServiceDemo(String baseUrl, String apiKey, String apiVersion) {
        super(SimpleOpenAIAzure.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .apiVersion(apiVersion)
                .build(), "N/A", "N/A");
    }

    private static void chatWithFunctionsDemo(String apiVersion) {
        var baseUrl = System.getenv("AZURE_OPENAI_BASE_URL");
        var apiKey = System.getenv("AZURE_OPENAI_API_KEY");
        var chatDemo = new ChatAzureServiceDemo(baseUrl, apiKey, apiVersion);
        chatDemo.addTitleAction("Call Chat (Blocking Approach)", chatDemo::demoCallChatBlocking);
        chatDemo.addTitleAction("Call Chat with Functions", chatDemo::demoCallChatWithFunctions);

        chatDemo.run();
    }

    private static void chatWithVisionDemo(String apiVersion) {
        var baseUrl = System.getenv("AZURE_OPENAI_BASE_URL_VISION");
        var apiKey = System.getenv("AZURE_OPENAI_API_KEY_VISION");
        var visionDemo = new ChatAzureServiceDemo(baseUrl, apiKey, apiVersion);
        visionDemo.addTitleAction("Call Chat with Vision (External image)",
                visionDemo::demoCallChatWithVisionExternalImageBlocking);
        visionDemo.addTitleAction("Call Chat with Vision (Local image)",
                visionDemo::demoCallChatWithVisionLocalImageBlocking);
        visionDemo.run();
    }

    public static void main(String[] args) {
        var apiVersion = System.getenv("AZURE_OPENAI_API_VERSION");

        chatWithFunctionsDemo(apiVersion);
        chatWithVisionDemo(apiVersion);
    }

}
