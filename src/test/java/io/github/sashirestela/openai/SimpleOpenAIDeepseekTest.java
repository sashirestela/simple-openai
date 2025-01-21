package io.github.sashirestela.openai;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
