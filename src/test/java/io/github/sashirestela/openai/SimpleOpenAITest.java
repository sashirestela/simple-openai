package io.github.sashirestela.openai;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.http.HttpClient;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

public class SimpleOpenAITest {

  @Mock
  HttpClient httpClient;

  @Test
  void shouldCreateAnInstanceOfModelServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = new SimpleOpenAI("apiKey", httpClient);
    OpenAI.Models modelService = simpleOpenAI.models();
    assertNotNull(modelService);
  }

  @Test
  void shouldCreateAnInstanceOfChatServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = new SimpleOpenAI("apiKey");
    OpenAI.ChatCompletions chatService = simpleOpenAI.chatCompletions();
    assertNotNull(chatService);
  }

  @Test
  void shouldCreateAnIstanceOfAudioServiceWhenCallingSimpleOpenAI() {
    SimpleOpenAI simpleOpenAI = new SimpleOpenAI("apiKey");
    OpenAI.Audios audioService = simpleOpenAI.audios();
    assertNotNull(audioService);
  }
}
