package io.github.sashirestela.openai.domain.chat;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.function.FunctionExecutor;
import io.github.sashirestela.openai.function.Functional;

@SuppressWarnings("unchecked")
public class ChatDomainTest {

  static HttpClient httpClient;
  static SimpleOpenAI openAI;
  static ChatRequest chatRequest;

  @BeforeAll
  static void setup() {
    httpClient = mock(HttpClient.class);
    openAI = SimpleOpenAI.builder()
        .apiKey("apiKey")
        .httpClient(httpClient)
        .build();
    chatRequest = ChatRequest.builder()
        .model("gpt-3.5-turbo-16k-0613")
        .messages(List.of(
            new ChatMessage(Role.SYSTEM, "Generate Python code with no comments."),
            new ChatMessage(Role.USER, "// Function to get the max of an array of integers.")))
        .temperature(0.0)
        .maxTokens(300)
        .functionCall("none")
        .topP(1.0)
        .n(1)
        .stop(null)
        .presencePenalty(0.0)
        .frequencyPenalty(0.0)
        .logitBias(null)
        .user("test")
        .build();
  }

  @Test
  void testChatCompletionsCreateStream() throws IOException {
    HttpResponse<Stream<String>> httpResponse = mock(HttpResponse.class);
    var listResponse = Files.readAllLines(Paths.get("src/test/resources/chatcompletions_create_stream.txt"));
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofLines().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn(listResponse.stream());

    var chatResponse = openAI.chatCompletions().createStream(chatRequest).join();
    chatResponse.filter(chatResp -> chatResp.firstContent() != null)
        .map(chatResp -> chatResp.firstContent())
        .forEach(System.out::print);
    assertNotNull(chatResponse);
  }

  @Test
  void testChatCompletionsCreate() throws IOException {
    HttpResponse<String> httpResponse = mock(HttpResponse.class);
    var jsonResponse = Files.readAllLines(Paths.get("src/test/resources/chatcompletions_create.json")).get(0);
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn(jsonResponse);

    var chatResponse = openAI.chatCompletions().create(chatRequest).join();
    System.out.println(chatResponse);
    assertNotNull(chatResponse);
  }

  @Test
  void testChatCompletionsCreateWithFunction() throws IOException {
    HttpResponse<String> httpResponse = mock(HttpResponse.class);
    var jsonResponse = Files.readAllLines(Paths.get("src/test/resources/chatcompletions_create_functions.json")).get(0);
    when(httpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandlers.ofString().getClass())))
        .thenReturn(CompletableFuture.completedFuture(httpResponse));
    when(httpResponse.statusCode()).thenReturn(HttpURLConnection.HTTP_OK);
    when(httpResponse.body()).thenReturn(jsonResponse);

    var functionExecutor = new FunctionExecutor();
    functionExecutor.enrollFunction(
        ChatFunction.builder()
            .name("product")
            .description("Get the product of two numbers")
            .functionalClass(Product.class)
            .build());
    var chatRequest = ChatRequest.builder()
        .model("gpt-3.5-turbo-16k-0613")
        .messages(List.of(
            new ChatMessage(Role.USER, "What is the product of 123 and 456?")))
        .functions(functionExecutor.getFunctions())
        .temperature(0.0)
        .maxTokens(100)
        .functionCall("auto")
        .topP(1.0)
        .n(1)
        .stop(null)
        .presencePenalty(0.0)
        .frequencyPenalty(0.0)
        .logitBias(null)
        .user("test")
        .build();
    var chatResponse = openAI.chatCompletions().create(chatRequest).join();
    System.out.println(chatResponse);
    assertNotNull(chatResponse);
  }

  @Test
  void shouldNotThrowExceptionWhenChatMessageIsCreatedWithAllFields() {
    assertDoesNotThrow(
        () -> ChatMessage.builder().role(Role.FUNCTION).content("content").name("name").build());
  }

  @Test
  void shouldThrownExceptionWhenChatMessageIsCreatedWithoutContentAndRoleIsDifferentThanAssistant() {
    var exception = assertThrows(RuntimeException.class,
        () -> ChatMessage.builder().role(Role.USER).build());
    assertEquals("The content is required for ChatMessage when role is other than assistant.", exception.getMessage());
  }

  @Test
  void shouldThrownExceptionWhenChatMessageIsCreatedWithoutNameAndRoleIsFunction() {
    var exception = assertThrows(RuntimeException.class,
        () -> ChatMessage.builder().role(Role.FUNCTION).content("content").build());
    assertEquals("The name is required for ChatMessage when role is function.", exception.getMessage());
  }

  static class Product implements Functional {
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
}