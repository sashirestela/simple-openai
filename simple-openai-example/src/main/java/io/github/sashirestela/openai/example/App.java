package io.github.sashirestela.openai.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import io.github.sashirestela.openai.FunctionExecutor;
import io.github.sashirestela.openai.OpenAIApi;
import io.github.sashirestela.openai.domain.chat.ChatFunction;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatResponse;
import io.github.sashirestela.openai.domain.chat.Role;
import io.github.sashirestela.openai.domain.model.Model;
import io.github.sashirestela.openai.domain.model.ModelResponse;
import io.github.sashirestela.openai.service.ChatService;
import io.github.sashirestela.openai.service.ModelService;
import io.github.sashirestela.openai.support.JsonUtil;

public class App {
  private OpenAIApi openAIApi;

  public App() {
    openAIApi = new OpenAIApi(System.getenv("OPENAI_API_KEY"));
  }

  public void runModelService() throws IOException, InterruptedException {
    ModelService modelService = openAIApi.createModelService();
    
    System.out.println("\n===== List of Models =====");
    ModelResponse modelResponse = modelService.callModels();
    modelResponse.getData().stream()
      .map(model -> model.getId())
      .filter(modelId -> modelId.contains("gpt"))
      .forEach(System.out::println);
    System.out.println("\n===== A Model =====");
    Model model = modelService.callModel("gpt-3.5-turbo-16k-0613");
    System.out.println(model);
  }

  public void runChatService() throws IOException, InterruptedException {
    ChatService chatService = openAIApi.createChatService();
    
    ChatRequest chatRequest = ChatRequest.builder()
      .model("gpt-3.5-turbo-16k-0613")
      .messages(List.of(
        new ChatMessage(Role.SYSTEM, "Generate Java code with no comments."),
        new ChatMessage(Role.USER, "# Function to get the max of an array of integers.")
      ))
      .temperature(0.0)
      .maxTokens(100)
      .build();
    
    System.out.println("\n===== Stream Chat Completion =====");
    Stream<ChatResponse> chatResponseStream = chatService.callChatCompletionStream(chatRequest);
    System.out.println("---- Token by Token");
    String response = chatResponseStream
      .map(chatResponse -> chatResponse.firstContent())
      .peek(System.out::print)
      .collect(Collectors.joining());
    System.out.println();
    System.out.println("----- Accumulated");
    System.out.println(response);
    
    System.out.println("\n===== Normal Chat Completion =====");
    ChatResponse chatResponse = chatService.callChatCompletion(chatRequest);
    System.out.println(chatResponse.firstContent());
  }

  public void runChatServiceWithFunctions() throws IOException, InterruptedException {
    ChatService chatService = openAIApi.createChatService();

    FunctionExecutor functionExecutor = new FunctionExecutor();
    functionExecutor.addFunction(
      ChatFunction.builder()
      .name("get_weather")
      .description("Get the current weather of a location")
      .functionToExecute(Weather.class,
                         weather -> weather.location + " - 29 " + weather.unit + " degrees")
      .build()
    );
    functionExecutor.addFunction(
      ChatFunction.builder()
      .name("product")
      .description("Get the product of two numbers")
      .functionToExecute(Product.class,
                         p -> p.multiplicand * p.multiplier)
      .build()      
    );
    functionExecutor.addFunction(
      ChatFunction.builder()
      .name("run_alarm")
      .description("Run an alarm")
      .functionToExecute(null,
                         none -> "DONE")
      .build()
    );

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
    
    String jsonChatRequest = JsonUtil.one().objectToJson(chatRequest);
    System.out.println(jsonChatRequest);
    
    ChatResponse chatResponse = chatService.callChatCompletion(chatRequest);
    
    String jsonChatResponse = JsonUtil.one().objectToJson(chatResponse);
    System.out.println(jsonChatResponse);
    
    ChatMessage chatMessage = chatResponse.getChoices().get(0).getMessage();
    Object result = functionExecutor.execute(chatMessage.getFunctionCall());
    System.out.println(result);
  }
  
  public static void main( String[] args ) throws IOException, InterruptedException {
    App app = new App();
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