package io.github.sashirestela.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.openai.OpenAI.ChatCompletions;
import io.github.sashirestela.openai.OpenAI.Models;
import io.github.sashirestela.openai.base.ClientConfig;
import io.github.sashirestela.openai.base.OpenAIConfigurator;
import io.github.sashirestela.openai.base.OpenAIProvider;
import io.github.sashirestela.openai.service.ChatCompletionServices;
import io.github.sashirestela.openai.service.ModelServices;
import io.github.sashirestela.openai.support.Constant;
import lombok.Builder;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.net.http.HttpClient;
import java.util.Map;
import java.util.Optional;

public class SimpleOpenAIDeepseek extends OpenAIProvider implements
        ChatCompletionServices,
        ModelServices {

    @Builder
    public SimpleOpenAIDeepseek(@NonNull String apiKey, String baseUrl, HttpClient httpClient,
            ObjectMapper objectMapper) {
        super(DeepseekConfigurator.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .httpClient(httpClient)
                .objectMapper(objectMapper)
                .build());
    }

    @Override
    public ChatCompletions chatCompletions() {
        return getOrCreateService(OpenAI.ChatCompletions.class);
    }

    @Override
    public Models models() {
        return getOrCreateService(OpenAI.Models.class);
    }

    @SuperBuilder
    static class DeepseekConfigurator extends OpenAIConfigurator {

        @Override
        public ClientConfig buildConfig() {
            return ClientConfig.builder()
                    .baseUrl(Optional.ofNullable(baseUrl).orElse(Constant.DEEPSEEK_BASE_URL))
                    .headers(Map.of(Constant.AUTHORIZATION_HEADER, Constant.BEARER_AUTHORIZATION + apiKey))
                    .httpClient(httpClient)
                    .objectMapper(objectMapper)
                    .build();
        }

    }

}
