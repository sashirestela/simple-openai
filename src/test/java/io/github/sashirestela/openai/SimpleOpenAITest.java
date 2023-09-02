package io.github.sashirestela.openai;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.http.HttpClient;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class SimpleOpenAITest {

  @Mock
  HttpClient httpClient;

  @Test
  void shouldCreateAnIstanceOfAudioServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.Audios audioService = simpleOpenAI.audios();
    assertNotNull(audioService);
  }

  @Test
  void shouldCreateAnInstanceOfChatServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.ChatCompletions chatService = simpleOpenAI.chatCompletions();
    assertNotNull(chatService);
  }

  @Test
  void shouldCreateAnInstanceOfCompletionServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.Completions completionService = simpleOpenAI.completions();
    assertNotNull(completionService);
  }

  @Test
  void shouldCreateAnInstanceOfEmbeddingServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.Embeddings embeddingService = simpleOpenAI.embeddings();
    assertNotNull(embeddingService);
  }

  @Test
  void shouldCreateAnInstanceOfFileServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.Files fileService = simpleOpenAI.files();
    assertNotNull(fileService);
  }

  @Test
  void shouldCreateAnInstanceOfImageServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.Images imageService = simpleOpenAI.images();
    assertNotNull(imageService);
  }

  @Test
  void shouldCreateAnInstanceOfFineTuningServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").build();
    OpenAI.FineTunings fineTuningService = simpleOpenAI.fineTunings();
    assertNotNull(fineTuningService);
  }

  @Test
  void shouldCreateAnInstanceOfModelServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").httpClient(httpClient).build();
    OpenAI.Models modelService = simpleOpenAI.models();
    assertNotNull(modelService);
  }

  @Test
  void shouldCreateAnInstanceOfModerationServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = SimpleOpenAI.builder().apiKey("apiKey").httpClient(httpClient).build();
    OpenAI.Moderations moderationService = simpleOpenAI.moderations();
    assertNotNull(moderationService);
  }
}
