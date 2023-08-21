package io.github.sashirestela.openai;

import java.lang.reflect.InvocationHandler;
import java.net.http.HttpClient;
import java.util.Optional;

import io.github.sashirestela.openai.http.HttpHandler;
import io.github.sashirestela.openai.support.ReflectUtil;

public class SimpleOpenAI {

  private final String OPENAI_URL_BASE = "https://api.openai.com";
  private HttpClient httpClient;
  private String apiKey;

  private OpenAI.Models modelService;
  private OpenAI.ChatCompletions chatCompletionService;
  private OpenAI.Completions completionService;
  private OpenAI.Images imageService;
  private OpenAI.Embeddings embeddingService;
  private OpenAI.Audios audioService;
  private OpenAI.Files fileService;
  private OpenAI.FineTunes fineTuneService;

  public SimpleOpenAI(String apiKey) {
    this.httpClient = HttpClient.newHttpClient();
    this.apiKey = apiKey;
  }

  public SimpleOpenAI(String apiKey, HttpClient httpClient) {
    this.httpClient = httpClient;
    this.apiKey = apiKey;
  }

  public OpenAI.Models models() {
    modelService = Optional.ofNullable(modelService)
        .orElse(createService(OpenAI.Models.class, httpClient, apiKey));
    return modelService;
  }

  public OpenAI.ChatCompletions chatCompletions() {
    chatCompletionService = Optional.ofNullable(chatCompletionService)
        .orElse(createService(OpenAI.ChatCompletions.class, httpClient, apiKey));
    return chatCompletionService;
  }

  public OpenAI.Completions completions() {
    completionService = Optional.ofNullable(completionService)
        .orElse(createService(OpenAI.Completions.class, httpClient, apiKey));
    return completionService;
  }

  public OpenAI.Images images() {
    imageService = Optional.ofNullable(imageService)
        .orElse(createService(OpenAI.Images.class, httpClient, apiKey));
    return imageService;
  }

  public OpenAI.Embeddings embeddings() {
    embeddingService = Optional.ofNullable(embeddingService)
        .orElse(createService(OpenAI.Embeddings.class, httpClient, apiKey));
    return embeddingService;
  }

  public OpenAI.Audios audios() {
    audioService = Optional.ofNullable(audioService)
        .orElse(createService(OpenAI.Audios.class, httpClient, apiKey));
    return audioService;
  }

  public OpenAI.Files files() {
    fileService = Optional.ofNullable(fileService)
        .orElse(createService(OpenAI.Files.class, httpClient, apiKey));
    return fileService;
  }

  public OpenAI.FineTunes fineTuneService() {
    fineTuneService = Optional.ofNullable(fineTuneService)
        .orElse(createService(OpenAI.FineTunes.class, httpClient, apiKey));
    return fineTuneService;
  }

  private <T> T createService(Class<T> serviceClass, HttpClient httpClient, String apiKey) {
    InvocationHandler httpHandler = new HttpHandler(httpClient, apiKey, OPENAI_URL_BASE);
    T service = ReflectUtil.get().createProxy(serviceClass, httpHandler);
    return service;
  }

}