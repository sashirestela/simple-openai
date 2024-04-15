package io.github.sashirestela.openai.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.BaseSimpleOpenAI;
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
import io.github.sashirestela.openai.function.Functional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

public class BaseChatServiceDemo extends AbstractDemo {

    ChatRequest chatRequest;
    String chatModelId;
    String visionModelId;

    public BaseChatServiceDemo(String chatModelId, String visionModelId) {
        init(chatModelId, visionModelId);
    }

    public BaseChatServiceDemo(BaseSimpleOpenAI openAI, String chatModelId, String visionModelId) {
        super(openAI);
        init(chatModelId, visionModelId);
    }

    private void init(String chatModelId, String visionModelId) {
        this.chatModelId = chatModelId;
        this.visionModelId = visionModelId;
        chatRequest = ChatRequest.builder()
                .model(chatModelId)
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
                .model(chatModelId)
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
                .model(chatModelId)
                .messages(messages)
                .tools(functionExecutor.getToolFunctions())
                .build();
        futureChat = openAI.chatCompletions().create(chatRequest);
        chatResponse = futureChat.join();
        System.out.println(chatResponse.firstContent());
    }

    public void demoCallChatWithVisionExternalImageStreaming() {
        demoCallChatWithVision(false, false);
    }

    public void demoCallChatWithVisionExternalImageBlocking() {
        demoCallChatWithVision(true, false);
    }

    public void demoCallChatWithVisionLocalImageStreaming() {
        demoCallChatWithVision(false, true);
    }

    public void demoCallChatWithVisionLocalImageBlocking() {
        demoCallChatWithVision(true, true);
    }

    public void demoCallChatWithVision(boolean isBlocking, boolean isLocalImage) {
        var image = new ContentPartImage(
                isLocalImage ? loadImageAsBase64("src/demo/resources/machupicchu.jpg")
                        : new ImageUrl("https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg"));

        var chatRequest = ChatRequest.builder()
                .model(visionModelId)
                .messages(List.of(
                        new ChatMsgUser(List.of(
                                new ContentPartText(
                                        "What do you see in the image? Give in details in no more than 100 words."),
                                image))))
                .temperature(0.0)
                .maxTokens(500)
                .build();
        if (isBlocking) {
            var futureChat = openAI.chatCompletions().create(chatRequest);
            var chatResponse = futureChat.join();
            System.out.println(chatResponse.firstContent());
        } else {
            var chatResponse = openAI.chatCompletions().createStream(chatRequest).join();
            chatResponse.map(ChatResponse::firstContent)
                    .filter(Objects::nonNull)
                    .forEach(System.out::print);
            System.out.println();
        }
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

    public static class Weather implements Functional {

        @JsonPropertyDescription("City and state, for example: León, Guanajuato")
        public String location;

        @JsonPropertyDescription("The temperature unit, can be 'celsius' or 'fahrenheit'")
        @JsonProperty(required = true)
        public String unit;

        @Override
        public Object execute() {
            return Math.random() * 45;
        }

    }

    public static class Product implements Functional {

        @JsonPropertyDescription("The multiplicand part of a product")
        @JsonProperty(required = true)
        public double multiplicand;

        @JsonPropertyDescription("The multiplier part of a product")
        @JsonProperty(required = true)
        public double multiplier;

        @Override
        public Object execute() {
            return multiplicand * multiplier;
        }

    }

    public static class RunAlarm implements Functional {

        @Override
        public Object execute() {
            return "DONE";
        }

    }

}
