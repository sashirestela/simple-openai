package io.github.sashirestela.openai.example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import io.github.sashirestela.openai.SimpleFunctionExecutor;
import io.github.sashirestela.openai.SimpleOpenAIApi;
import io.github.sashirestela.openai.domain.chat.ChatFunction;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatResponse;
import io.github.sashirestela.openai.domain.chat.Role;
import io.github.sashirestela.openai.domain.model.Model;
import io.github.sashirestela.openai.service.ChatService;
import io.github.sashirestela.openai.service.ModelService;

public class App {
  private SimpleOpenAIApi openAIApi;

  public App() {
    openAIApi = new SimpleOpenAIApi(System.getenv("OPENAI_API_KEY"));
  }

  public void runModelService() {
    ModelService modelService = openAIApi.createModelService();

    System.out.println("\n===== List of Models =====");
    List<Model> models = modelService.callModels().join();
    models.stream()
        .map(model -> model.getId())
        .filter(modelId -> modelId.contains("gpt"))
        .forEach(System.out::println);

    System.out.println("\n===== A Model =====");
    Model model = modelService.callModel(models.get(0).getId()).join();
    System.out.println(model);
  }

  @SuppressWarnings("unused")
  public void runChatService() {
    ChatService chatService = openAIApi.createChatService();

    ChatRequest chatRequest = ChatRequest.builder()
        .model("gpt-3.5-turbo-16k-0613")
        .messages(List.of(
            new ChatMessage(Role.SYSTEM, "Generate Java code with no comments."),
            new ChatMessage(Role.USER,
                "// Create a function to get the second highest max of an array of integers. Do not use predefined libraries.")))
        .temperature(0)
        .maxTokens(300)
        .build();

    System.out.println("\n===== Stream Chat Completion =====");
    Stream<ChatResponse> chatResponseStream = chatService.callChatCompletionStream(chatRequest).join();
    String response = chatResponseStream
        .filter(chatResponse -> chatResponse.firstContent() != null)
        .map(chatResponse -> chatResponse.firstContent())
        .peek(System.out::print)
        .collect(Collectors.joining());

    System.out.println("\n===== Normal Chat Completion =====");
    ChatResponse chatResponse = chatService.callChatCompletion(chatRequest).join();
    System.out.println(chatResponse.firstContent());
  }

  public void runChatServiceWithFunctions() {
    ChatService chatService = openAIApi.createChatService();

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
        .model("gpt-3.5-turbo-16k-0613")
        .messages(messages)
        .temperature(0.0)
        .maxTokens(1000)
        .functions(functionExecutor.getFunctions())
        .functionCall("auto")
        .build();

    System.out.println("\n===== Chat Completion with Call Function =====");
    ChatResponse chatResponse = chatService.callChatCompletion(chatRequest).join();

    ChatMessage chatMessage = chatResponse.firstMessage();
    Object result = functionExecutor.execute(chatMessage.getFunctionCall());
    System.out.println(result);
  }

  public static void main(String[] args) {
    App app = new App();
    app.runModelService();
    app.runChatService();
    app.runChatServiceWithFunctions();
    System.exit(0);
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
}