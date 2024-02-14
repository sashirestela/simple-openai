package io.github.sashirestela.openai.domain.chat;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.chat.content.ContentPartImage;
import io.github.sashirestela.openai.domain.chat.content.ContentPartText;
import io.github.sashirestela.openai.domain.chat.content.ImageDetail;
import io.github.sashirestela.openai.domain.chat.content.ImageUrl;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgAssistant;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgSystem;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgTool;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgUser;
import io.github.sashirestela.openai.domain.chat.tool.ChatFunction;
import io.github.sashirestela.openai.domain.chat.tool.ChatFunctionCall;
import io.github.sashirestela.openai.domain.chat.tool.ChatFunctionName;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolCall;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolChoice;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolChoiceType;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolType;
import io.github.sashirestela.openai.function.FunctionExecutor;
import io.github.sashirestela.openai.function.Functional;
import java.io.IOException;
import java.net.http.HttpClient;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ChatDomainTest {

    static HttpClient httpClient;
    static SimpleOpenAI openAI;
    static ChatRequest chatTextRequest;
    static FunctionExecutor functionExecutor;

    @BeforeAll
    static void setup() {
        httpClient = mock(HttpClient.class);
        openAI = SimpleOpenAI.builder()
                .apiKey("apiKey")
                .httpClient(httpClient)
                .build();
        chatTextRequest = ChatRequest.builder()
                .model("gpt-4-1106-preview")
                .message(new ChatMsgSystem("You are an expert in Mathematics", "tutor"))
                .message(new ChatMsgUser("Tell me the Pitagoras theorem in less than 50 words.", "student"))
                .temperature(0.2)
                .maxTokens(500)
                .topP(1.0)
                .n(1)
                .stop("end")
                .presencePenalty(0.0)
                .frequencyPenalty(0.0)
                .logitBias(Map.of("21943", 0))
                .user("test")
                .responseFormat(new ChatRespFmt(ChatRespFmtType.TEXT))
                .seed(2)
                .logprobs(true)
                .topLogprobs(1)
                .build();
        functionExecutor = new FunctionExecutor();
        functionExecutor.enrollFunction(
                ChatFunction.builder()
                        .name("product")
                        .description("Get the product of two numbers")
                        .functionalClass(Product.class)
                        .build());
    }

    @Test
    void testChatCompletionsCreateStream() throws IOException {
        DomainTestingHelper.get().mockForStream(httpClient,
                "src/test/resources/chatcompletions_create_stream.txt");
        var chatResponse = openAI.chatCompletions().createStream(chatTextRequest).join();
        chatResponse.filter(chatResp -> chatResp.firstContent() != null)
                .map(chatResp -> chatResp.firstContent())
                .forEach(System.out::print);
        assertNotNull(chatResponse);
    }

    @Test
    void testChatCompletionsCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient,
                "src/test/resources/chatcompletions_create.json");
        var chatResponse = openAI.chatCompletions().create(chatTextRequest).join();
        System.out.println(chatResponse.firstContent());
        assertNotNull(chatResponse);
    }

    @Test
    void testChatCompletionsCreateWithVision() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient,
                "src/test/resources/chatcompletions_create_vision.json");
        var chatRequest = ChatRequest.builder()
                .model("gpt-4-vision-preview")
                .message(new ChatMsgUser(List.of(
                        new ContentPartText("What are in these images? Is there any difference between them?"),
                        new ContentPartImage(new ImageUrl(
                                "https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg",
                                ImageDetail.AUTO)),
                        new ContentPartImage(new ImageUrl(
                                "https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg")))))
                .temperature(0.2)
                .maxTokens(500)
                .topP(1.0)
                .n(1)
                .stop("end")
                .presencePenalty(0.0)
                .frequencyPenalty(0.0)
                .user("test")
                .seed(1)
                .build();
        var chatResponse = openAI.chatCompletions().create(chatRequest).join();
        System.out.println(chatResponse.firstContent());
        assertNotNull(chatResponse);
    }

    @Test
    void testChatCompletionsCreateWithFunctionQuestion() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient,
                "src/test/resources/chatcompletions_create_function_question.json");
        var chatRequest = ChatRequest.builder()
                .model("gpt-4-1106-preview")
                .message(new ChatMsgSystem("You are an expert in Mathematics"))
                .message(new ChatMsgUser("What is the product of 123 and 456?"))
                .tools(functionExecutor.getToolFunctions())
                .toolChoice(new ChatToolChoice(ChatToolType.FUNCTION, new ChatFunctionName("product")))
                .temperature(0.2)
                .maxTokens(500)
                .topP(1.0)
                .n(1)
                .stop("end")
                .presencePenalty(0.0)
                .frequencyPenalty(0.0)
                .logitBias(Map.of("21943", 0))
                .user("test")
                .responseFormat(new ChatRespFmt(ChatRespFmtType.TEXT))
                .seed(1)
                .build();
        var chatResponse = openAI.chatCompletions().create(chatRequest).join();
        System.out.println(chatResponse.firstMessage().getToolCalls().get(0).getFunction().getName());
        System.out.println(chatResponse.firstMessage().getToolCalls().get(0).getFunction().getArguments());
        assertNotNull(chatResponse);
    }

    @Test
    void testChatCompletionsCreateWithFunctionAnswer() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient,
                "src/test/resources/chatcompletions_create_function_answer.json");
        var chatRequest = ChatRequest.builder()
                .model("gpt-4-1106-preview")
                .message(new ChatMsgSystem("You are an expert in Mathematics"))
                .message(new ChatMsgUser("What is the product of 123 and 456?"))
                .message(new ChatMsgAssistant(null, List.of(new ChatToolCall(
                        "call_tAoX6VHyjQVLnM9CZvEsTEwW",
                        ChatToolType.FUNCTION,
                        new ChatFunctionCall("product", "{\"multiplicand\":123,\"multiplier\":456}")))))
                .message(new ChatMsgTool("56088", "call_tAoX6VHyjQVLnM9CZvEsTEwW"))
                .tools(functionExecutor.getToolFunctions())
                .temperature(0.2)
                .maxTokens(500)
                .topP(1.0)
                .n(1)
                .stop("end")
                .presencePenalty(0.0)
                .frequencyPenalty(0.0)
                .logitBias(Map.of("21943", 0))
                .user("test")
                .responseFormat(new ChatRespFmt(ChatRespFmtType.TEXT))
                .seed(1)
                .build();
        var chatResponse = openAI.chatCompletions().create(chatRequest).join();
        System.out.println(chatResponse);
        assertNotNull(chatResponse);
    }

    @Test
    void shouldCreateChatRequestWhenToolChoiceIsRightClass() {
        Object[] testData = {
                ChatToolChoiceType.AUTO,
                new ChatToolChoice(ChatToolType.FUNCTION, new ChatFunctionName("myFunction"))
        };
        for (Object data : testData) {
            var chatRequestBuilder = ChatRequest.builder()
                    .model("model")
                    .message(new ChatMsgUser("My Content"))
                    .toolChoice(data);
            assertDoesNotThrow(() -> chatRequestBuilder.build());
        }
    }

    @Test
    void shouldThrownExceptionWhenCreatingChatRequestWithToolChoiceWrongClass() {
        var chatRequestBuilder = ChatRequest.builder()
                .model("model")
                .message(new ChatMsgUser("My Content"))
                .toolChoice("wrong value");
        var exception = assertThrows(SimpleUncheckedException.class, () -> chatRequestBuilder.build());
        var actualErrorMessage = exception.getMessage();
        var expectedErrorMessge = "The field toolChoice must be ChatToolChoiceType or ChatToolChoice classes.";
        assertEquals(expectedErrorMessge, actualErrorMessage);
    }

    @Test
    void shouldCreateChatRequestWhenStopIsRightClass() {
        Object[] testData = {
                "stop",
                List.of("stop", "end", "quit", "finish")
        };
        for (Object data : testData) {
            var chatRequestBuilder = ChatRequest.builder()
                    .model("model")
                    .message(new ChatMsgUser("My Content"))
                    .stop(data);
            assertDoesNotThrow(() -> chatRequestBuilder.build());
        }
    }

    @Test
    void shouldThrownExceptionWhenCreatingChatRequestWithStopWrongClass() {
        Object[] testData = {
                1001,
                List.of(17.65, 23.68),
                List.of("one", "two", "three", "four", "five")
        };
        for (Object data : testData) {
            var chatRequestBuilder = ChatRequest.builder()
                    .model("model")
                    .message(new ChatMsgUser("My Content"))
                    .stop(data);
            var exception = assertThrows(SimpleUncheckedException.class, () -> chatRequestBuilder.build());
            var actualErrorMessage = exception.getMessage();
            var expectedErrorMessge = "The field stop must be String or List<String> (max 4 items) classes.";
            assertEquals(expectedErrorMessge, actualErrorMessage);
        }
    }

    @Test
    void shouldCreateChtMsgUserWhenContentIsRigthClass() {
        Object[] testData = {
                "test data",
                List.of(new ContentPartText("text"), new ContentPartImage(new ImageUrl("image url", ImageDetail.AUTO)))
        };
        for (Object data : testData) {
            assertDoesNotThrow(() -> new ChatMsgUser(data));
        }
    }

    @Test
    void shouldThrownExceptionWhenCreatingChatMsgUserWithContentWrongClass() {
        Object[] testData = {
                1001,
                List.of("first", "second")
        };
        for (Object data : testData) {
            var exception = assertThrows(SimpleUncheckedException.class, () -> new ChatMsgUser(data));
            var actualErrorMessage = exception.getMessage();
            var expectedErrorMessge = "The field content must be String or List<ContentPart> classes.";
            assertEquals(expectedErrorMessge, actualErrorMessage);
        }
    }

    @Test
    void shouldCreateChatMsgAssistantWhenParametersAreCorrect() {
        ChatMsgAssistant[] testData = {
                new ChatMsgAssistant("content", "name"),
                new ChatMsgAssistant("content")
        };
        for (ChatMsgAssistant data : testData) {
            assertNotNull(data);
        }
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