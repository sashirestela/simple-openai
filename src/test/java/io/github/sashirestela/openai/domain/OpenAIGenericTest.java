package io.github.sashirestela.openai.domain;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

public class OpenAIGenericTest {

  @Test
  void testCreateNewObject() {
    var openAIGeneric = new OpenAIGeneric<String>("object", 123456789L, Arrays.asList("one", "two"),
        "nextStartingAfter", false);
    System.out.println(openAIGeneric);
    assertNotNull(openAIGeneric);
  }
}
