package io.github.sashirestela.openai;

import java.lang.reflect.Proxy;
import java.net.http.HttpClient;

import io.github.sashirestela.openai.http.HttpHandler;
import io.github.sashirestela.openai.service.ChatService;
import io.github.sashirestela.openai.service.ModelService;

public final class OpenAIApi {

  private final String OPENAI_URL_BASE = "https://api.openai.com";
  private HttpClient httpClient;
  private String apiKey;

  public OpenAIApi(String apiKey) {
    this.httpClient = HttpClient.newHttpClient();
    this.apiKey = apiKey;
  }

  public ChatService createChatService() {
    ChatService service = createService(ChatService.class, httpClient, apiKey);
    return service;
  }

  public ModelService createModelService() {
    ModelService service = createService(ModelService.class, httpClient, apiKey);
    return service;
  }

  private <T> T createService(Class<T> serviceClass, HttpClient httpClient, String apiKey) {
    T service = (T) Proxy.newProxyInstance(
      serviceClass.getClassLoader(),
      new Class<?>[] {serviceClass},
      new HttpHandler(httpClient, apiKey, OPENAI_URL_BASE)
    );
    return service;
  }
}