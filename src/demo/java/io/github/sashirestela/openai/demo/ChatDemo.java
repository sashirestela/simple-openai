package io.github.sashirestela.openai.demo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.util.CommonUtil;
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
import io.github.sashirestela.openai.common.function.FunctionDef;
import io.github.sashirestela.openai.common.function.FunctionExecutor;
import io.github.sashirestela.openai.common.function.Functional;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.AssistantMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.ToolMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatRequest.Audio;
import io.github.sashirestela.openai.domain.chat.ChatRequest.Modality;
import io.github.sashirestela.openai.domain.chat.ChatRequest.WebSearchOptions;
import io.github.sashirestela.openai.domain.chat.ChatRequest.WebSearchOptions.ApproxLocation;
import io.github.sashirestela.openai.domain.chat.ChatRequest.WebSearchOptions.UserLocation;
import io.github.sashirestela.openai.domain.response.ResponseTool.ContextSize;
import io.github.sashirestela.openai.service.ChatCompletionServices;
import io.github.sashirestela.openai.support.Base64Util;
import io.github.sashirestela.openai.support.Base64Util.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ChatDemo extends AbstractDemo {

    protected ChatRequest chatRequest;
    protected String model;
    protected String modelAudio;
    protected long sleepSeconds;
    protected ChatCompletionServices chatProvider;

    protected ChatDemo(String model, String modelAudio) {
        this("standard", model, modelAudio);
    }

    protected ChatDemo(String provider, String model, String modelAudio) {
        super(provider);
        this.model = model;
        this.modelAudio = modelAudio;
        chatRequest = ChatRequest.builder()
                .model(model)
                .message(SystemMessage.of("You are an expert in AI."))
                .message(UserMessage.of("Write a technical article about ChatGPT, no more than 100 words."))
                .temperature(0.0)
                .maxTokens(300)
                .build();
        this.sleepSeconds = 0; // No sleep by default
        this.chatProvider = this.openAI;
    }

    public void demoCallChatStreaming() {
        var futureChat = chatProvider.chatCompletions().createStream(chatRequest);
        var chatResponse = futureChat.join();
        chatResponse.forEach(this::processResponseChunk);
    }

    public void demoCallChatBlocking() {
        var futureChat = chatProvider.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();
        System.out.println(chatResponse.firstContent());
    }

    public void demoCallChatWithFunctions() {
        var question = "What is the product of 123 and 456?";
        System.out.println(question);
        var functionExecutor = new FunctionExecutor();
        functionExecutor.enrollFunction(
                FunctionDef.builder()
                        .name("get_weather")
                        .description("Get the current weather of a location")
                        .functionalClass(Weather.class)
                        .strict(Boolean.TRUE)
                        .build());
        functionExecutor.enrollFunction(
                FunctionDef.builder()
                        .name("product")
                        .description("Get the product of two numbers")
                        .functionalClass(Product.class)
                        .strict(Boolean.TRUE)
                        .build());
        functionExecutor.enrollFunction(
                FunctionDef.builder()
                        .name("run_alarm")
                        .description("Run an alarm")
                        .functionalClass(RunAlarm.class)
                        .strict(Boolean.TRUE)
                        .build());
        var messages = new ArrayList<ChatMessage>();
        messages.add(UserMessage.of(question));
        chatRequest = ChatRequest.builder()
                .model(model)
                .messages(messages)
                .tools(functionExecutor.getToolFunctions())
                .build();
        var futureChat = chatProvider.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();
        var chatMessage = chatResponse.firstMessage();
        var chatToolCall = chatMessage.getToolCalls().get(0);
        var result = functionExecutor.execute(chatToolCall.getFunction());
        messages.add(chatMessage);
        if (CommonUtil.isNullOrEmpty(chatToolCall.getId())) {  // For GeminiGoogle
            System.out.println("The result is: " + result.toString());
            return;
        }
        messages.add(ToolMessage.of(result.toString(), chatToolCall.getId()));
        chatRequest = ChatRequest.builder()
                .model(model)
                .messages(messages)
                .tools(functionExecutor.getToolFunctions())
                .build();
        sleep();
        futureChat = chatProvider.chatCompletions().create(chatRequest);
        chatResponse = futureChat.join();
        if (CommonUtil.isNullOrEmpty(chatResponse.firstContent())) {  // For DeepSeek
            System.out.println("The result is: " + result.toString());
            return;
        }
        System.out.println(chatResponse.firstContent());
    }

    public void demoCallChatWithVisionExternalImage() {
        chatRequest = ChatRequest.builder()
                .model(model)
                .messages(List.of(
                        UserMessage.of(List.of(
                                ContentPartText.of(
                                        "What do you see in the image? Give in details in no more than 100 words."),
                                ContentPartImageUrl.of(ImageUrl.of(
                                        "https://upload.wikimedia.org/wikipedia/commons/e/eb/Machu_Picchu%2C_Peru.jpg"))))))
                .temperature(0.0)
                .maxTokens(500)
                .build();
        var chatResponse = chatProvider.chatCompletions().createStream(chatRequest).join();
        chatResponse.forEach(this::processResponseChunk);
        System.out.println();
    }

    public void demoCallChatWithVisionLocalImage() {
        chatRequest = ChatRequest.builder()
                .model(model)
                .messages(List.of(
                        UserMessage.of(List.of(
                                ContentPartText.of(
                                        "What do you see in the image? Give in details in no more than 100 words."),
                                ContentPartImageUrl.of(ImageUrl.of(
                                        Base64Util.encode("src/demo/resources/machupicchu.jpg", MediaType.IMAGE)))))))
                .temperature(0.0)
                .maxTokens(500)
                .build();
        var chatResponse = chatProvider.chatCompletions().createStream(chatRequest).join();
        chatResponse.forEach(this::processResponseChunk);
        System.out.println();
    }

    public void demoCallChatWithStructuredOutputs() {
        chatRequest = ChatRequest.builder()
                .model(model)
                .message(SystemMessage
                        .of("You are a helpful math tutor. Guide the user through the solution step by step."))
                .message(UserMessage.of("How can I solve 8x + 7 = -23"))
                .responseFormat(ResponseFormat.jsonSchema(JsonSchema.builder()
                        .name("MathReasoning")
                        .schemaClass(MathReasoning.class)
                        .build()))
                .build();
        var chatResponse = chatProvider.chatCompletions().createStream(chatRequest).join();
        chatResponse.forEach(this::processResponseChunk);
        System.out.println();
    }

    public void demoCallChatWithStructuredOutputs2() {
        JsonNode schema = null;
        try {
            var json = Files.readString(Paths.get("src/demo/resources/math_reasoning.json"));
            schema = new ObjectMapper().readTree(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
        chatRequest = ChatRequest.builder()
                .model(model)
                .message(SystemMessage
                        .of("You are a helpful math tutor. Guide the user through the solution step by step."))
                .message(UserMessage.of("How can I solve 8x + 7 = -23"))
                .responseFormat(ResponseFormat.jsonSchema(JsonSchema.builder()
                        .name("MathReasoning")
                        .schema(schema)
                        .build()))
                .build();
        var chatResponse = chatProvider.chatCompletions().createStream(chatRequest).join();
        chatResponse.forEach(this::processResponseChunk);
        System.out.println();
    }

    public void demoCallChatWithAudioInputOutput() {
        var messages = new ArrayList<ChatMessage>();
        messages.add(SystemMessage.of("Respond in a short and concise way."));
        messages.add(UserMessage.of(List.of(ContentPartInputAudio.of(InputAudio.of(
                Base64Util.encode("src/demo/resources/question1.mp3", null), InputAudioFormat.MP3)))));
        chatRequest = ChatRequest.builder()
                .model(modelAudio)
                .modality(Modality.TEXT)
                .modality(Modality.AUDIO)
                .audio(Audio.of(Voice.ALLOY, AudioFormat.MP3))
                .messages(messages)
                .build();
        var chatResponse = chatProvider.chatCompletions().create(chatRequest).join();
        var audio = chatResponse.firstMessage().getAudio();
        Base64Util.decode(audio.getData(), "src/demo/resources/answer1.mp3");
        System.out.println("Answer 1: " + audio.getTranscript());

        messages.add(AssistantMessage.builder().audioId(audio.getId()).build());
        messages.add(UserMessage.of(List.of(ContentPartInputAudio.of(InputAudio.of(
                Base64Util.encode("src/demo/resources/question2.mp3", null), InputAudioFormat.MP3)))));
        chatRequest = ChatRequest.builder()
                .model(modelAudio)
                .modality(Modality.TEXT)
                .modality(Modality.AUDIO)
                .audio(Audio.of(Voice.ALLOY, AudioFormat.MP3))
                .messages(messages)
                .build();
        chatResponse = chatProvider.chatCompletions().create(chatRequest).join();
        audio = chatResponse.firstMessage().getAudio();
        Base64Util.decode(audio.getData(), "src/demo/resources/answer2.mp3");
        System.out.println("Answer 2: " + audio.getTranscript());
    }

    public void demoCallChatWithWebSearch() {
        chatRequest = ChatRequest.builder()
                .model("gpt-4o-mini-search-preview")
                .message(UserMessage.of("What is the most important news in Peruvian politics today?"))
                .webSearchOptions(WebSearchOptions.builder()
                        .searchContextSize(ContextSize.MEDIUM)
                        .userLocation(UserLocation.of(ApproxLocation.builder()
                                .country("PE")
                                .city("Lima")
                                .build()))
                        .build())
                .build();
        var chatResponse = chatProvider.chatCompletions().createStream(chatRequest).join();
        chatResponse.forEach(this::processResponseChunk);
        System.out.println();
    }

    protected void processResponseChunk(Chat responseChunk) {
        var choices = responseChunk.getChoices();
        if (!choices.isEmpty()) {
            var delta = choices.get(0).getMessage();
            if (delta.getContent() != null) {
                System.out.print(delta.getContent());
            }
            if (delta.getReasoningContent() != null) {  // For DeepSeek
                System.out.print(delta.getReasoningContent());
            }
        }
        var usage = responseChunk.getUsage();
        if (usage != null && usage.getCompletionTokens() != 0) {  // completionTokens condition for GeminiGoogle
            System.out.println("\n");
            System.out.println(usage);
        }
    }

    private void sleep() {
        if (this.sleepSeconds > 0) {
            try {
                TimeUnit.SECONDS.sleep(this.sleepSeconds);
            } catch (InterruptedException e) {
                java.lang.Thread.currentThread().interrupt();
            }
        }
    }

    public static class Weather implements Functional {

        @JsonPropertyDescription("City and state, for example: León, Guanajuato")
        @JsonProperty(required = true)
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

    public static class MathReasoning {

        public List<Step> steps;
        public String finalAnswer;

        public static class Step {

            public String explanation;
            public String output;

        }

    }

    public static void main(String[] args) {
        var demo = new ChatDemo("gpt-4o-mini", "gpt-4o-audio-preview");

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);
        demo.addTitleAction("Call Chat with Vision (External image)", demo::demoCallChatWithVisionExternalImage);
        demo.addTitleAction("Call Chat with Vision (Local image)", demo::demoCallChatWithVisionLocalImage);
        demo.addTitleAction("Call Chat with Structured Outputs", demo::demoCallChatWithStructuredOutputs);
        demo.addTitleAction("Call Chat with Structured Outputs 2", demo::demoCallChatWithStructuredOutputs2);
        demo.addTitleAction("Call Chat with Audio Input/Output", demo::demoCallChatWithAudioInputOutput);
        demo.addTitleAction("Call Chat with Web Search", demo::demoCallChatWithWebSearch);

        demo.run();
    }

}
