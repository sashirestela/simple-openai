package io.github.sashirestela.openai;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.http.HttpClient;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import io.github.sashirestela.openai.service.ChatService;
import io.github.sashirestela.openai.service.ModelService;

public class SimpleOpenAIApiTest {

  @Mock
  HttpClient httpClient;

  @Test
  void shouldCreateAnInstanceOfModelServiceWhenCallingSimpleOpenAIApi() {
    SimpleOpenAIApi simpleOpenAIApi = new SimpleOpenAIApi("apiKey", httpClient);
    ModelService modelService = simpleOpenAIApi.createModelService();
    assertNotNull(modelService);
  }

  @Test
  void shouldCreateAnInstanceOfChatServiceWhenCallingSimpleOpenAIApi() {
    SimpleOpenAIApi simpleOpenAIApi = new SimpleOpenAIApi("apiKey");
    ChatService chatService = simpleOpenAIApi.createChatService();
    assertNotNull(chatService);
  }
}
