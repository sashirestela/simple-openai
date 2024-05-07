package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.SimpleOpenAIAzure;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.demo.ChatDemo.Product;
import io.github.sashirestela.openai.demo.ChatDemo.RunAlarm;
import io.github.sashirestela.openai.demo.ChatDemo.Weather;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.ToolMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ContentPart.ContentPartImage;
import io.github.sashirestela.openai.domain.chat.ContentPart.ContentPartImage.ImageUrl;
import io.github.sashirestela.openai.domain.chat.ContentPart.ContentPartText;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ChatAzureDemo extends AbstractDemo {

    private ChatRequest chatRequest;

    public ChatAzureDemo(String baseUrl, String apiKey, String apiVersion) {
        super(SimpleOpenAIAzure.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .apiVersion(apiVersion)
                .build());
        chatRequest = ChatRequest.builder()
                .model("N/A")
                .message(SystemMessage.of("You are an expert in AI."))
                .message(UserMessage.of("Write a technical article about ChatGPT, no more than 100 words."))
                .temperature(0.0)
                .maxTokens(300)
                .build();
    }

    public void demoCallChatBlocking() {
        var futureChat = openAI.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();
        System.out.println(chatResponse.firstContent());
    }

    public void demoCallChatWithFunctions() {
        var functionExecutor = new FunctionExecutor();
        functionExecutor.enrollFunction(
                FunctionDef.builder()
                        .name("get_weather")
                        .description("Get the current weather of a location")
                        .functionalClass(Weather.class)
                        .build());
        functionExecutor.enrollFunction(
                FunctionDef.builder()
                        .name("product")
                        .description("Get the product of two numbers")
                        .functionalClass(Product.class)
                        .build());
        functionExecutor.enrollFunction(
                FunctionDef.builder()
                        .name("run_alarm")
                        .description("Run an alarm")
                        .functionalClass(RunAlarm.class)
                        .build());
        var messages = new ArrayList<ChatMessage>();
        messages.add(UserMessage.of("What is the product of 123 and 456?"));
        chatRequest = ChatRequest.builder()
                .model("N/A")
                .messages(messages)
                .tools(functionExecutor.getToolFunctions())
                .build();
        var futureChat = openAI.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();
        var chatMessage = chatResponse.firstMessage();
        var chatToolCall = chatMessage.getToolCalls().get(0);
        var result = functionExecutor.execute(chatToolCall.getFunction());
        messages.add(chatMessage);
        messages.add(ToolMessage.of(result.toString(), chatToolCall.getId()));
        chatRequest = ChatRequest.builder()
                .model("N/A")
                .messages(messages)
                .tools(functionExecutor.getToolFunctions())
                .build();
        futureChat = openAI.chatCompletions().create(chatRequest);
        chatResponse = futureChat.join();
        System.out.println(chatResponse.firstContent());
    }

    public void demoCallChatWithVisionExternalImage() {
        var chatRequest = ChatRequest.builder()
                .model("N/A")
                .messages(List.of(
                        UserMessage.of(List.of(
                                ContentPartText.of(
                                        "What do you see in the image? Give in details in no more than 100 words."),
                                ContentPartImage.of(ImageUrl.of(
                                        "https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg"))))))
                .temperature(0.0)
                .maxTokens(500)
                .build();

        var chatResponse = openAI.chatCompletions().create(chatRequest).join();
        System.out.println(chatResponse.firstContent());
        System.out.println();

    }

    public void demoCallChatWithVisionLocalImage() {
        var chatRequest = ChatRequest.builder()
                .model("N/A")
                .messages(List.of(
                        UserMessage.of(List.of(
                                ContentPartText.of(
                                        "What do you see in the image? Give in details in no more than 100 words."),
                                ContentPartImage.of(loadImageAsBase64("src/demo/resources/machupicchu.jpg"))))))
                .temperature(0.0)
                .maxTokens(500)
                .build();
        var chatResponse = openAI.chatCompletions().create(chatRequest).join();
        System.out.println(chatResponse.firstContent());
    }

    private static ImageUrl loadImageAsBase64(String imagePath) {
        try {
            Path path = Paths.get(imagePath);
            byte[] imageBytes = Files.readAllBytes(path);
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            var extension = imagePath.substring(imagePath.lastIndexOf('.') + 1);
            var prefix = "data:image/" + extension + ";base64,";
            return ImageUrl.of(prefix + base64String);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void chatWithFunctionsDemo(String apiVersion) {
        var baseUrl = System.getenv("AZURE_OPENAI_BASE_URL");
        var apiKey = System.getenv("AZURE_OPENAI_API_KEY");
        var chatDemo = new ChatAzureDemo(baseUrl, apiKey, apiVersion);
        chatDemo.addTitleAction("Call Chat (Blocking Approach)", chatDemo::demoCallChatBlocking);
        chatDemo.addTitleAction("Call Chat with Functions", chatDemo::demoCallChatWithFunctions);

        chatDemo.run();
    }

    private static void chatWithVisionDemo(String apiVersion) {
        var baseUrl = System.getenv("AZURE_OPENAI_BASE_URL_VISION");
        var apiKey = System.getenv("AZURE_OPENAI_API_KEY_VISION");
        var visionDemo = new ChatAzureDemo(baseUrl, apiKey, apiVersion);
        visionDemo.addTitleAction("Call Chat with Vision (External image)",
                visionDemo::demoCallChatWithVisionExternalImage);
        visionDemo.addTitleAction("Call Chat with Vision (Local image)", visionDemo::demoCallChatWithVisionLocalImage);
        visionDemo.run();
    }

    public static void main(String[] args) {
        var apiVersion = System.getenv("AZURE_OPENAI_API_VERSION");

        chatWithFunctionsDemo(apiVersion);
        chatWithVisionDemo(apiVersion);
    }

}
