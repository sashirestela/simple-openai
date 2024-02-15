package io.github.sashirestela.openai.demo;


import io.github.sashirestela.openai.SimpleOpenAIAnyscale;
import io.github.sashirestela.openai.demo.ChatServiceDemo.Product;
import io.github.sashirestela.openai.demo.ChatServiceDemo.RunAlarm;
import io.github.sashirestela.openai.demo.ChatServiceDemo.Weather;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatResponse;
import io.github.sashirestela.openai.domain.chat.message.ChatMsg;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgSystem;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgTool;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgUser;
import io.github.sashirestela.openai.domain.chat.tool.ChatFunction;
import io.github.sashirestela.openai.function.FunctionExecutor;
import java.util.ArrayList;

public class AnyscaleChatServiceDemo extends AbstractDemo {

    public static final String BASE_URL = "https://api.endpoints.anyscale.com";

    public static final String MODEL = "mistralai/Mixtral-8x7B-Instruct-v0.1";


    private ChatRequest chatRequest;


    @SuppressWarnings("unchecked")
    public AnyscaleChatServiceDemo(String baseUrl, String apiKey, String model) {
        super(SimpleOpenAIAnyscale.builder().apiKey(apiKey).baseUrl(baseUrl).build());
        chatRequest = ChatRequest.builder()
            .model(model)
            .message(new ChatMsgSystem("You are an expert in AI."))
            .message(
                new ChatMsgUser("Write a technical article about ChatGPT, no more than 100 words."))
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
        var chatRequest = ChatRequest.builder()
            .model(MODEL)
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
            .model(MODEL)
            .messages(messages)
            .tools(functionExecutor.getToolFunctions())
            .build();
        futureChat = openAI.chatCompletions().create(chatRequest);
        chatResponse = futureChat.join();
        System.out.println(chatResponse.firstContent());
    }

    public static void main(String[] args) {
        var apiKey = System.getenv("ANYSCALE_API_KEY");
        // Services like Azure OpenAI don't require a model (endpoints have built-in model)
        var demo = new AnyscaleChatServiceDemo(BASE_URL, apiKey, MODEL);

        demo.addTitleAction("Call Chat (Streaming Approach)", demo::demoCallChatStreaming);
        demo.addTitleAction("Call Chat (Blocking Approach)", demo::demoCallChatBlocking);
        demo.addTitleAction("Call Chat with Functions", demo::demoCallChatWithFunctions);

        demo.run();
    }
}
