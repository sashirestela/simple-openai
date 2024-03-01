package io.github.sashirestela.openai.domain;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class OpenAIGenericTest {

    @Test
    void testCreateNewObject() {
        var openAIGeneric = new OpenAIGeneric<String>("object", 123456789L, Arrays.asList("one", "two"),
                "nextStartingAfter", false);
        System.out.println(openAIGeneric);
        assertNotNull(openAIGeneric);
    }

}
