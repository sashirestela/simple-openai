package io.github.sashirestela.openai.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import io.github.sashirestela.openai.SimpleFunctionExecutor;
import io.github.sashirestela.openai.domain.chat.ChatFunction;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatResponse;
import io.github.sashirestela.openai.domain.chat.Role;
import io.github.sashirestela.openai.service.ChatService;

public class ChatServiceDemo extends AbstractDemo {

  private ChatService chatService;
  private ChatRequest chatRequest;
  private String modelIdToUse;

  public ChatServiceDemo() {
    chatService = openAIApi.createChatService();
    modelIdToUse = "gpt-3.5-turbo-16k-0613";
  }

  public void demoCallChatStreaming() {
    chatRequest = ChatRequest.builder()
        .model(modelIdToUse)
        .messages(List.of(
            new ChatMessage(Role.SYSTEM, "You are an expert in AI."),
            new ChatMessage(Role.USER, "Write a technical article about ChatGPT, no more than 100 words.")))
        .temperature(0.0)
        .maxTokens(300)
        .build();
    CompletableFuture<Stream<ChatResponse>> futureChat = chatService.callChatCompletionStream(chatRequest);
    Stream<ChatResponse> chatResponse = futureChat.join();
    chatResponse.filter(chatResp -> chatResp.firstContent() != null)
        .map(chatResp -> chatResp.firstContent())
        .forEach(System.out::print);
    System.out.println();
  }

  public void demoCallChatBlocking() {
    CompletableFuture<ChatResponse> futureChat = chatService.callChatCompletion(chatRequest);
    ChatResponse chatResponse = futureChat.join();
    System.out.println(chatResponse.firstContent());
  }

  public void demoCallChatWithFunctions() {
    SimpleFunctionExecutor functionExecutor = new SimpleFunctionExecutor();
    functionExecutor.enrollFunction(
        ChatFunction.builder()
            .name("get_weather")
            .description("Get the current weather of a location")
            .functionToExecute(Weather.class,
                weather -> weather.location + " - 29 " + weather.unit + " degrees")
            .build());
    functionExecutor.enrollFunction(
        ChatFunction.builder()
            .name("product")
            .description("Get the product of two numbers")
            .functionToExecute(Product.class,
                p -> p.multiplicand * p.multiplier)
            .build());
    functionExecutor.enrollFunction(
        ChatFunction.builder()
            .name("run_alarm")
            .description("Run an alarm")
            .functionToExecute(null,
                none -> "DONE")
            .build());
    List<ChatMessage> messages = new ArrayList<>();
    messages.add(new ChatMessage(Role.USER, "What is the product of 123 and 456?"));
    ChatRequest chatRequest = ChatRequest.builder()
        .model(modelIdToUse)
        .messages(messages)
        .functions(functionExecutor.getFunctions())
        .functionCall("auto")
        .build();
    CompletableFuture<ChatResponse> futureChat = chatService.callChatCompletion(chatRequest);
    ChatResponse chatResponse = futureChat.join();
    ChatMessage chatMessage = chatResponse.firstMessage();
    Object result = functionExecutor.execute(chatMessage.getFunctionCall());
    messages.add(chatMessage);
    messages.add(
        ChatMessage.builder()
            .role(Role.FUNCTION)
            .content(result.toString())
            .name(chatMessage.getFunctionCall().getName())
            .build());
    chatRequest = ChatRequest.builder()
        .model(modelIdToUse)
        .messages(messages)
        .functions(functionExecutor.getFunctions())
        .functionCall("auto")
        .build();
    futureChat = chatService.callChatCompletion(chatRequest);
    chatResponse = futureChat.join();
    System.out.println(chatResponse.firstContent());
  }

  public static class Weather {
    @JsonPropertyDescription("City and state, for example: León, Guanajuato")
    public String location;

    @JsonPropertyDescription("The temperature unit, can be 'celsius' or 'fahrenheit'")
    @JsonProperty(required = true)
    public String unit;
  }

  public static class Product {
    @JsonPropertyDescription("The multiplicand part of a product")
    @JsonProperty(required = true)
    public double multiplicand;

    @JsonPropertyDescription("The multiplier part of a product")
    @JsonProperty(required = true)
    public double multiplier;
  }

  public static void main(String[] args) {
    ChatServiceDemo demo = new ChatServiceDemo();

    demo.addTitleAction("Call Chat (Streaming Approach)", () -> demo.demoCallChatStreaming());
    demo.addTitleAction("Call Chat (Blocking Approach)", () -> demo.demoCallChatBlocking());
    demo.addTitleAction("Call Chat with Functions", () -> demo.demoCallChatWithFunctions());

    demo.run();
  }
}