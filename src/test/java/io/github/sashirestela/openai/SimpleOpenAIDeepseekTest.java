package io.github.sashirestela.openai;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class SimpleOpenAIDeepseekTest {

    @Test
    void shouldCreateEndpoints() {
        var openAI = SimpleOpenAIDeepseek.builder()
                .apiKey("apiKey")
                .baseUrl("baseUrl")
                .build();
        assertNotNull(openAI.chatCompletions());
        assertNotNull(openAI.models());
    }

}
