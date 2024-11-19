package io.github.sashirestela.openai.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.OpenAI;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.common.ResponseFormat;
import io.github.sashirestela.openai.common.ResponseFormat.JsonSchema;
import io.github.sashirestela.openai.common.audio.AudioFormat;
import io.github.sashirestela.openai.common.audio.InputAudioFormat;
import io.github.sashirestela.openai.common.audio.Voice;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartImageUrl;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartImageUrl.ImageUrl;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartInputAudio;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartInputAudio.InputAudio;
import io.github.sashirestela.openai.common.content.ContentPart.ContentPartText;
import io.github.sashirestela.openai.common.content.ImageDetail;
import io.github.sashirestela.openai.common.function.FunctionCall;
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.common.function.Functional;
import io.github.sashirestela.openai.common.tool.ToolCall;
import io.github.sashirestela.openai.common.tool.ToolChoice;
import io.github.sashirestela.openai.common.tool.ToolChoiceOption;
import io.github.sashirestela.openai.common.tool.ToolType;
import io.github.sashirestela.openai.domain.DomainTestingHelper;
import io.github.sashirestela.openai.domain.DomainTestingHelper.MockForType;
import io.github.sashirestela.openai.domain.chat.ChatMessage.AssistantMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.ToolMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest.Audio;
import io.github.sashirestela.openai.domain.chat.ChatRequest.Modality;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

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
                .message(SystemMessage.of("You are an expert in Mathematics", "tutor"))
                .message(UserMessage.of("Tell me the Pitagoras theorem in less than 50 words.", "student"))
                .temperature(0.2)
                .maxCompletionTokens(500)
                .topP(1.0)
                .n(1)
                .stop("end")
                .presencePenalty(0.0)
                .frequencyPenalty(0.0)
                .logitBias(Map.of("21943", 0))
                .user("test")
                .responseFormat(ResponseFormat.TEXT)
                .seed(2)
                .logprobs(true)
                .topLogprobs(1)
                .build();
        functionExecutor = new FunctionExecutor();
        functionExecutor.enrollFunction(
                FunctionDef.builder()
                        .name("product")
                        .description("Get the product of two numbers")
                        .functionalClass(Product.class)
                        .strict(Boolean.TRUE)
                        .build());
    }

    @Test
    void testChatCompletionsCreateStream() throws IOException {
        DomainTestingHelper.get().mockForStream(httpClient, "src/test/resources/chatcompletions_create_stream.txt");
        var chatResponse = openAI.chatCompletions().createStream(chatTextRequest).join();
        chatResponse.forEach(responseChunk -> {
            var choices = responseChunk.getChoices();
            if (choices.size() > 0) {
                var delta = choices.get(0).getMessage();
                if (delta.getContent() != null) {
                    System.out.print(delta.getContent());
                }
            }
            var usage = responseChunk.getUsage();
            if (usage != null) {
                System.out.println("\n");
                System.out.println(usage);
            }
        });
        assertNotNull(chatResponse);
    }

    @Test
    void testChatCompletionsCreate() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/chatcompletions_create.json");
        var chatResponse = openAI.chatCompletions().create(chatTextRequest).join();
        System.out.println(chatResponse);
        assertNotNull(chatResponse);
    }

    @Test
    void testChatCompletionsCreateWithVision() throws IOException {
        DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/chatcompletions_create_vision.json");
        var chatRequest = ChatRequest.builder()
                .model("gpt-4-vision-preview")
                .message(UserMessage.of(List.of(
                        ContentPartText.of("What are in these images? Is there any difference between them?"),
                        ContentPartImageUrl.of(ImageUrl.of(
                                "https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg",
                                ImageDetail.AUTO)),
                        ContentPartImageUrl.of(ImageUrl.of(
                                "https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg")))))
                .temperature(0.2)
                .maxCompletionTokens(500)
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
    void testChatCompletionsCreateWithStructuredOutput() throws IOException {
        DomainTestingHelper.get()
                .mockForObject(httpClient, "src/test/resources/chatcompletions_create_structured_output.json");
        var chatRequest = ChatRequest.builder()
                .model("gpt-4o-mini")
                .message(SystemMessage
                        .of("You are a helpful math tutor. Guide the user through the solution step by step."))
                .message(UserMessage.of("How can I solve 8x + 7 = -23"))
                .responseFormat(ResponseFormat.jsonSchema(JsonSchema.builder()
                        .name("MathReasoning")
                        .schemaClass(MathReasoning.class)
                        .build()))
                .build();
        var chatResponse = openAI.chatCompletions().create(chatRequest).join();
        System.out.println(chatResponse.firstContent());
        assertNotNull(chatResponse);
    }

    @Test
    void testChatCompletionsCreateWithStructuredOutputAndCustomJsonSchema() throws IOException {
        DomainTestingHelper.get()
                .mockForObject(httpClient, "src/test/resources/chatcompletions_create_structured_output.json");
        JsonNode schema = null;
        try {
            var json = Files.readString(Paths.get("src/demo/resources/math_reasoning.json"));
            schema = new ObjectMapper().readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        var chatRequest = ChatRequest.builder()
                .model("gpt-4o-mini")
                .message(SystemMessage
                        .of("You are a helpful math tutor. Guide the user through the solution step by step."))
                .message(UserMessage.of("How can I solve 8x + 7 = -23"))
                .responseFormat(ResponseFormat.jsonSchema(JsonSchema.builder()
                        .name("MathReasoning")
                        .schema(schema)
                        .build()))
                .build();
        var chatResponse = openAI.chatCompletions().create(chatRequest).join();
        System.out.println(chatResponse.firstContent());
        assertNotNull(chatResponse);
    }

    @Test
    void testChatCompletionsCreateWithFunctionQuestion() throws IOException {
        DomainTestingHelper.get()
                .mockForObject(httpClient, "src/test/resources/chatcompletions_create_function_question.json");
        var chatRequest = ChatRequest.builder()
                .model("gpt-4-1106-preview")
                .message(SystemMessage.of("You are an expert in Mathematics"))
                .message(UserMessage.of("What is the product of 123 and 456?"))
                .tools(functionExecutor.getToolFunctions())
                .toolChoice(ToolChoice.function("product"))
                .temperature(0.2)
                .maxCompletionTokens(500)
                .topP(1.0)
                .n(1)
                .stop("end")
                .presencePenalty(0.0)
                .frequencyPenalty(0.0)
                .logitBias(Map.of("21943", 0))
                .user("test")
                .responseFormat(ResponseFormat.TEXT)
                .seed(1)
                .parallelToolCalls(Boolean.TRUE)
                .build();
        var chatResponse = openAI.chatCompletions().create(chatRequest).join();
        System.out.println(chatResponse.firstMessage().getToolCalls().get(0).getFunction().getName());
        System.out.println(chatResponse.firstMessage().getToolCalls().get(0).getFunction().getArguments());
        assertNotNull(chatResponse);
    }

    @Test
    void testChatCompletionsCreateWithFunctionAnswer() throws IOException {
        DomainTestingHelper.get()
                .mockForObject(httpClient, "src/test/resources/chatcompletions_create_function_answer.json");
        var chatRequest = ChatRequest.builder()
                .model("gpt-4-1106-preview")
                .message(SystemMessage.of("You are an expert in Mathematics"))
                .message(UserMessage.of("What is the product of 123 and 456?"))
                .message(AssistantMessage.of(List.of(new ToolCall(
                        0,
                        "call_tAoX6VHyjQVLnM9CZvEsTEwW",
                        ToolType.FUNCTION,
                        new FunctionCall("product", "{\"multiplicand\":123,\"multiplier\":456}")))))
                .message(ToolMessage.of("56088", "call_tAoX6VHyjQVLnM9CZvEsTEwW"))
                .tools(functionExecutor.getToolFunctions())
                .temperature(0.2)
                .maxCompletionTokens(500)
                .topP(1.0)
                .n(1)
                .stop("end")
                .presencePenalty(0.0)
                .frequencyPenalty(0.0)
                .logitBias(Map.of("21943", 0))
                .user("test")
                .responseFormat(ResponseFormat.TEXT)
                .seed(1)
                .build();
        var chatResponse = openAI.chatCompletions().create(chatRequest).join();
        System.out.println(chatResponse);
        assertNotNull(chatResponse);
    }

    @Test
    void testChatCompletionsCreateWithAudio() throws IOException {
        DomainTestingHelper.get()
                .mockFor(httpClient, Map.of(
                        MockForType.OBJECT, List.of(
                                "src/test/resources/chatcompletions_create_audio_1.json",
                                "src/test/resources/chatcompletions_create_audio_1.json",
                                "src/test/resources/chatcompletions_create_audio_2.json")));
        var messages = new ArrayList<ChatMessage>();
        messages.add(SystemMessage.of("Respond in a short and concise way."));
        messages.add(UserMessage.of(List.of(ContentPartInputAudio.of(InputAudio.of(
                "question_1_in_base64", InputAudioFormat.MP3)))));
        var chatRequest = ChatRequest.builder()
                .model("modelAudio")
                .modality(Modality.TEXT)
                .modality(Modality.AUDIO)
                .audio(Audio.of(Voice.ALLOY, AudioFormat.MP3))
                .messages(messages)
                .build();
        var chatResponse = openAI.chatCompletions().create(chatRequest).join();
        var audio = chatResponse.firstMessage().getAudio();
        System.out.println("Answer 1: " + audio.getTranscript());
        assertNotNull(chatResponse);

        messages.add(AssistantMessage.builder().audioId(audio.getId()).build());
        messages.add(UserMessage.of(List.of(ContentPartInputAudio.of(InputAudio.of(
                "question_2_in_base64", InputAudioFormat.MP3)))));
        chatRequest = ChatRequest.builder()
                .model("modelAudio")
                .modality(Modality.TEXT)
                .modality(Modality.AUDIO)
                .audio(Audio.of(Voice.ALLOY, AudioFormat.MP3))
                .messages(messages)
                .build();
        chatResponse = openAI.chatCompletions().create(chatRequest).join();
        audio = chatResponse.firstMessage().getAudio();
        System.out.println("Answer 2: " + audio.getTranscript());
        assertNotNull(chatResponse);
    }

    @Test
    void shouldUpdateChatRequestWithAutoToolChoiceWhenToolsAreProvidedWithoutToolChoice() {
        var charRequest = ChatRequest.builder()
                .model("model")
                .message(UserMessage.of("content"))
                .tools(functionExecutor.getToolFunctions())
                .build();

        assertNull(charRequest.getToolChoice());
        var updatedChatRequest = OpenAI.updateRequest(charRequest, Boolean.TRUE);
        assertEquals(ToolChoiceOption.AUTO, updatedChatRequest.getToolChoice());
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

    static class MathReasoning {

        public List<Step> steps;
        public String finalAnswer;

        static class Step {

            public String explanation;
            public String output;

        }

    }

}
