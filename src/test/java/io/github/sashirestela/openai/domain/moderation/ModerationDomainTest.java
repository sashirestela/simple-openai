package io.github.sashirestela.openai.domain.moderation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.DomainTestingHelper;

public class ModerationDomainTest {

  static HttpClient httpClient;
  static SimpleOpenAI openAI;

  @BeforeAll
  static void setup() {
    httpClient = mock(HttpClient.class);
    openAI = SimpleOpenAI.builder()
        .apiKey("apiKey")
        .httpClient(httpClient)
        .build();
  }

  @Test
  void testModelsGetList() throws IOException {
    DomainTestingHelper.get().mockForObject(httpClient, "src/test/resources/moderations_create.json");
    var moderationRequest = ModerationRequest.builder()
        .input(Arrays.asList(
            "I want to kill them.",
            "I am not sure what to think about them."))
        .model("text-moderation-latest")
        .build();
    var moderationResponse = openAI.moderations().create(moderationRequest).join();
    System.out.println(moderationResponse);
    assertNotNull(moderationResponse);
  }
}