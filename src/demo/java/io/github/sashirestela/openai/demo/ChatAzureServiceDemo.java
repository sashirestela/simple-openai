package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.SimpleOpenAIAzure;
import io.github.sashirestela.openai.demo.ChatServiceDemo.Product;
import io.github.sashirestela.openai.demo.ChatServiceDemo.RunAlarm;
import io.github.sashirestela.openai.demo.ChatServiceDemo.Weather;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatResponse;
import io.github.sashirestela.openai.domain.chat.content.ContentPartImage;
import io.github.sashirestela.openai.domain.chat.content.ContentPartText;
import io.github.sashirestela.openai.domain.chat.content.ImageUrl;
import io.github.sashirestela.openai.domain.chat.message.ChatMsg;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgSystem;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgTool;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgUser;
import io.github.sashirestela.openai.domain.chat.tool.ChatFunction;
import io.github.sashirestela.openai.function.FunctionExecutor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class ChatAzureServiceDemo extends AbstractDemo {

    private ChatRequest chatRequest;

    public ChatAzureServiceDemo(String baseUrl, String apiKey, String apiVersion) {
        super(SimpleOpenAIAzure.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .apiVersion(apiVersion)
                .build());
        chatRequest = ChatRequest.builder()
                .model("N/A")
                .message(new ChatMsgSystem("You are an expert in AI."))
                .message(new ChatMsgUser("Write a technical article about ChatGPT, no more than 100 words."))
                .temperature(0.0)
                .maxTokens(300)
                .build();
    }

    public void demoCallChatStreaming() {
        var futureChat = openAI.chatCompletions().createStream(chatRequest);
        var chatResponse = futureChat.join();
        chatResponse.filter(chatResp -> chatResp.firstContent() != null)
                .map(ChatResponse::firstContent)
                .forEach(System.out::print);
        System.out.println();
    }

    public void demoCallChatBlocking() {
        var futureChat = openAI.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();
        System.out.println(chatResponse.firstContent());
    }

    public void demoCallChatWithFunctions() {
        var functionExecutor = new FunctionExecutor();
        functionExecutor.enrollFunction(
                ChatFunction.builder()
                        .name("get_weather")
                        .description("Get the current weather of a location")
                        .functionalClass(Weather.class)
                        .build());
        functionExecutor.enrollFunction(
                ChatFunction.builder()
                        .name("product")
                        .description("Get the product of two numbers")
                        .functionalClass(Product.class)
                        .build());
        functionExecutor.enrollFunction(
                ChatFunction.builder()
                        .name("run_alarm")
                        .description("Run an alarm")
                        .functionalClass(RunAlarm.class)
                        .build());
        var messages = new ArrayList<ChatMsg>();
        messages.add(new ChatMsgUser("What is the product of 123 and 456?"));
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
        messages.add(new ChatMsgTool(result.toString(), chatToolCall.getId()));
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
                        new ChatMsgUser(List.of(
                                new ContentPartText(
                                        "What do you see in the image? Give in details in no more than 100 words."),
                                new ContentPartImage(new ImageUrl(
                                        "https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg"))))))
                .temperature(0.0)
                .maxTokens(500)
                .build();

        var chatResponse = openAI.chatCompletions().create(chatRequest).join();
        System.out.println(chatResponse.firstContent());
        //        var chatResponse = openAI.chatCompletions().createStream(chatRequest).join();
        //        chatResponse.map(ChatResponse::firstContent)
        //            .filter(Objects::nonNull)
        //            .forEach(System.out::print);
        System.out.println();

    }

    public void demoCallChatWithVisionLocalImage() {
        var chatRequest = ChatRequest.builder()
                .model("N/A")
                .messages(List.of(
                        new ChatMsgUser(List.of(
                                new ContentPartText(
                                        "What do you see in the image? Give in details in no more than 100 words."),
                                new ContentPartImage(loadImageAsBase64("src/demo/resources/machupicchu.jpg"))))))
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
            return new ImageUrl(prefix + base64String);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void chatWithFunctionsDemo(String apiVersion) {
        var baseUrl = System.getenv("AZURE_OPENAI_BASE_URL");
        var apiKey = System.getenv("AZURE_OPENAI_API_KEY");
        var chatDemo = new ChatAzureServiceDemo(baseUrl, apiKey, apiVersion);
        chatDemo.addTitleAction("Call Chat (Blocking Approach)", chatDemo::demoCallChatBlocking);
        chatDemo.addTitleAction("Call Chat with Functions", chatDemo::demoCallChatWithFunctions);

        // The response for streaming returns 200 with empty choices. Commented out until resolved.
        //chatDemo.addTitleAction("Call Chat (Streaming Approach)", chatDemo::demoCallChatStreaming);
        chatDemo.run();
    }

    private static void chatWithVisionDemo(String apiVersion) {
        var baseUrl = System.getenv("AZURE_OPENAI_BASE_URL_VISION");
        var apiKey = System.getenv("AZURE_OPENAI_API_KEY_VISION");
        var visionDemo = new ChatAzureServiceDemo(baseUrl, apiKey, apiVersion);
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
