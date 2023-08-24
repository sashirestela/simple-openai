package io.github.sashirestela.openai;

import java.lang.reflect.InvocationHandler;
import java.net.http.HttpClient;
import java.util.Optional;

import io.github.sashirestela.openai.filter.AudioFilter;
import io.github.sashirestela.openai.filter.FilterInvocation;
import io.github.sashirestela.openai.filter.StreamFilter;
import io.github.sashirestela.openai.http.HttpConfig;
import io.github.sashirestela.openai.http.HttpHandler;
import io.github.sashirestela.openai.support.ReflectUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Getter
public class SimpleOpenAI {

  private final String OPENAI_URL_BASE = "https://api.openai.com";
  private final String ORGANIZATION_HEADER = "OpenAI-Organization";

  @NonNull
  private String apiKey;

  private String organizationId;
  private String urlBase;
  private HttpClient httpClient;

  private HttpConfig httpConfig;

  private OpenAI.Audios audioService;
  private OpenAI.ChatCompletions chatCompletionService;
  private OpenAI.Completions completionService;
  private OpenAI.Embeddings embeddingService;
  private OpenAI.Files fileService;
  private OpenAI.Images imageService;
  private OpenAI.Models modelService;
  private OpenAI.Moderations moderationService;
  private OpenAI.FineTunes fineTuneService;

  @Builder
  public SimpleOpenAI(String apiKey, String organizationId, String urlBase, HttpClient httpClient) {
    this.apiKey = apiKey;
    this.organizationId = organizationId;
    this.urlBase = Optional.ofNullable(urlBase).orElse(OPENAI_URL_BASE);
    this.httpClient = Optional.ofNullable(httpClient).orElse(HttpClient.newHttpClient());

    String[] headers = organizationId == null
        ? new String[] {}
        : new String[] { ORGANIZATION_HEADER, organizationId };
    this.httpConfig = HttpConfig.builder()
        .apiKey(this.apiKey)
        .urlBase(this.urlBase)
        .httpClient(this.httpClient)
        .headers(headers)
        .build();
  }

  public OpenAI.Audios audios() {
    audioService = Optional.ofNullable(audioService)
        .orElse(createServiceProxy(OpenAI.Audios.class, new AudioFilter()));
    return audioService;
  }

  public OpenAI.ChatCompletions chatCompletions() {
    chatCompletionService = Optional.ofNullable(chatCompletionService)
        .orElse(createServiceProxy(OpenAI.ChatCompletions.class, new StreamFilter()));
    return chatCompletionService;
  }

  public OpenAI.Completions completions() {
    completionService = Optional.ofNullable(completionService)
        .orElse(createServiceProxy(OpenAI.Completions.class, new StreamFilter()));
    return completionService;
  }

  public OpenAI.Embeddings embeddings() {
    embeddingService = Optional.ofNullable(embeddingService)
        .orElse(createServiceProxy(OpenAI.Embeddings.class, null));
    return embeddingService;
  }

  public OpenAI.Files files() {
    fileService = Optional.ofNullable(fileService)
        .orElse(createServiceProxy(OpenAI.Files.class, null));
    return fileService;
  }

  public OpenAI.Images images() {
    imageService = Optional.ofNullable(imageService)
        .orElse(createServiceProxy(OpenAI.Images.class, null));
    return imageService;
  }

  public OpenAI.Models models() {
    modelService = Optional.ofNullable(modelService)
        .orElse(createServiceProxy(OpenAI.Models.class, null));
    return modelService;
  }

  public OpenAI.Moderations moderations() {
    moderationService = Optional.ofNullable(moderationService)
        .orElse(createServiceProxy(OpenAI.Moderations.class, null));
    return moderationService;
  }

  public OpenAI.FineTunes fineTunes() {
    fineTuneService = Optional.ofNullable(fineTuneService)
        .orElse(createServiceProxy(OpenAI.FineTunes.class, null));
    return fineTuneService;
  }

  private <T> T createServiceProxy(Class<T> serviceClass, FilterInvocation filter) {
    InvocationHandler httpHandler = new HttpHandler(httpConfig, filter);
    T serviceProxy = ReflectUtil.get().createProxy(serviceClass, httpHandler);
    return serviceProxy;
  }

}