package io.github.sashirestela.openai.demo;


import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgSystem;
import io.github.sashirestela.openai.domain.chat.message.ChatMsgUser;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AzureOpenAIChatServiceDemo extends AbstractDemo {

    private final ChatRequest chatRequest;

    public AzureOpenAIChatServiceDemo(String baseUrl, String apiKey, String model) {
        super(baseUrl, apiKey, url -> {
            // add a query parameter to url (current api version for AzureOpenAI)
            url += (url.contains("?") ? "&" : "?") + "api-version=2023-05-15";
            // remove '/vN' or '/vN.M' from url
            url = url.replaceFirst("/v\\d+(\\.\\d+)?", "");
            return url;
        });

        chatRequest = ChatRequest.builder()
            .model(model)
            .message(new ChatMsgSystem("You are an expert in AI."))
            .message(
                new ChatMsgUser("Write a technical article about ChatGPT, no more than 100 words."))
            .temperature(0.0)
            .maxTokens(300)
            .build();
    }

    public void demoCallChatBlocking() {
        var futureChat = openAI.chatCompletions().create(chatRequest);
        var chatResponse = futureChat.join();
        System.out.println(chatResponse.firstContent());
    }

    public static void main(String[] args) {
        var baseUrl = System.getenv("CUSTOM_OPENAI_BASE_URL");
        var apiKey = System.getenv("CUSTOM_OPENAI_API_KEY");
        // Services like Azure OpenAI don't require a model (endpoints have built-in model)
        var model = Optional.ofNullable(System.getenv("CUSTOM_OPENAI_MODEL"))
            .orElse("N/A");
        var demo = new AzureOpenAIChatServiceDemo(baseUrl, apiKey, model);

        demo.addTitleAction("Call Completion (Blocking Approach)", demo::demoCallChatBlocking);

        demo.run();
    }
}