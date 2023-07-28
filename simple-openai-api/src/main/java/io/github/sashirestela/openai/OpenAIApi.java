git package io.github.sashirestela.openai;

import java.net.http.HttpClient;

import io.github.sashirestela.openai.chat.ChatService;
import io.github.sashirestela.openai.model.ModelService;

public final class OpenAIApi {

  private HttpClient httpClient;
  private String apiKey;

  public OpenAIApi(String apiKey) {
    this.httpClient = HttpClient.newHttpClient();
    this.apiKey = apiKey;
  }

  public ChatService createChatService() {
    ChatService service = new ChatServiceImpl(this.httpClient, this.apiKey);
    return service;
  }

  public ModelService createModelService() {
    ModelService service = new ModelServiceImpl(this.httpClient, this.apiKey);
    return service;
  }
}