package io.github.sashirestela.openai.demo;

import io.github.sashirestela.openai.SimpleOpenAIAzure;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;

public class AzureDemo {

    public static void main(String[] args) {
        var openAI = SimpleOpenAIAzure.builder()
                .apiKey(System.getenv("AZURE_OPENAI_API_KEY"))
                .apiVersion(System.getenv("AZURE_OPENAI_API_VERSION"))
                .baseUrl(System.getenv("AZURE_OPENAI_BASE_URL"))
                .build();
        var response = openAI.chatCompletions().create(ChatRequest.builder()
                .model("N/A")
                .message(SystemMessage.of("You are an expert in AI."))
                .message(UserMessage.of("Write a technical article about ChatGPT, no more than 100 words."))
                .temperature(0.0)
                .maxTokens(300)
                .build()).join();
        System.out.println(response.firstContent());
    }
}
