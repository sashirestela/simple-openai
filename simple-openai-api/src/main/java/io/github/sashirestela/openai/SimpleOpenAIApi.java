package io.github.sashirestela.openai;

import java.lang.reflect.InvocationHandler;
import java.net.http.HttpClient;

import io.github.sashirestela.openai.http.HttpHandler;
import io.github.sashirestela.openai.service.ChatService;
import io.github.sashirestela.openai.service.ModelService;
import io.github.sashirestela.openai.support.ReflectUtil;

public final class SimpleOpenAIApi {

  private final String OPENAI_URL_BASE = "https://api.openai.com";
  private HttpClient httpClient;
  private String apiKey;

  public SimpleOpenAIApi(String apiKey) {
    this.httpClient = HttpClient.newHttpClient();
    this.apiKey = apiKey;
  }

  public SimpleOpenAIApi(String apiKey, HttpClient httpClient) {
    this.httpClient = httpClient;
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
    InvocationHandler httpHandler = new HttpHandler(httpClient, apiKey, OPENAI_URL_BASE);
    T service = ReflectUtil.get().createProxy(serviceClass, httpHandler);
    return service;
  }
}