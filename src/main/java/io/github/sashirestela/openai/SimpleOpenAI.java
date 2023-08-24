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

/**
 * The factory class that implements the {@link OpenAI OpenAI} inner interfaces.
 * Actually it provides dynamic proxies that intercepts methods invocations to
 * be resolved by the {@link HttpHandler HttpHandler} class.
 */
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

  /**
   * Base constructor for a builder to create an object of this class.
   * 
   * @param apiKey         Identifier to be used for authentication. Mandatory.
   * @param organizationId Organization's id to be charged for usage. Optional.
   * @param urlBase        Host's url, If not provided, it'll be
   *                       https://api.openai.com. Optional.
   * @param httpClient     A {@link java.net.http.HttpClient HttpClient} object.
   *                       One is created by default if not provided. Optional.
   */
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

  /**
   * Creates a dynamic proxy to handle requests to the Audios interface.
   * 
   * @return A "virtual" instance for the interface. It is created only once.
   */
  public OpenAI.Audios audios() {
    audioService = Optional.ofNullable(audioService)
        .orElse(createServiceProxy(OpenAI.Audios.class, new AudioFilter()));
    return audioService;
  }

  /**
   * Creates a dynamic proxy to handle requests to the ChatCompletions interface.
   * 
   * @return A "virtual" instance for the interface. It is created only once.
   */
  public OpenAI.ChatCompletions chatCompletions() {
    chatCompletionService = Optional.ofNullable(chatCompletionService)
        .orElse(createServiceProxy(OpenAI.ChatCompletions.class, new StreamFilter()));
    return chatCompletionService;
  }

  /**
   * Creates a dynamic proxy to handle requests to the Completions interface.
   * 
   * @return A "virtual" instance for the interface. It is created only once.
   */
  public OpenAI.Completions completions() {
    completionService = Optional.ofNullable(completionService)
        .orElse(createServiceProxy(OpenAI.Completions.class, new StreamFilter()));
    return completionService;
  }

  /**
   * Creates a dynamic proxy to handle requests to the Embeddings interface.
   * 
   * @return A "virtual" instance for the interface. It is created only once.
   */
  public OpenAI.Embeddings embeddings() {
    embeddingService = Optional.ofNullable(embeddingService)
        .orElse(createServiceProxy(OpenAI.Embeddings.class, null));
    return embeddingService;
  }

  /**
   * Creates a dynamic proxy to handle requests to the Files interface.
   * 
   * @return A "virtual" instance for the interface. It is created only once.
   */
  public OpenAI.Files files() {
    fileService = Optional.ofNullable(fileService)
        .orElse(createServiceProxy(OpenAI.Files.class, null));
    return fileService;
  }

  /**
   * Creates a dynamic proxy to handle requests to the Images interface.
   * 
   * @return A "virtual" instance for the interface. It is created only once.
   */
  public OpenAI.Images images() {
    imageService = Optional.ofNullable(imageService)
        .orElse(createServiceProxy(OpenAI.Images.class, null));
    return imageService;
  }

  /**
   * Creates a dynamic proxy to handle requests to the Models interface.
   * 
   * @return A "virtual" instance for the interface. It is created only once.
   */
  public OpenAI.Models models() {
    modelService = Optional.ofNullable(modelService)
        .orElse(createServiceProxy(OpenAI.Models.class, null));
    return modelService;
  }

  /**
   * Creates a dynamic proxy to handle requests to the Moderations interface.
   * 
   * @return A "virtual" instance for the interface. It is created only once.
   */
  public OpenAI.Moderations moderations() {
    moderationService = Optional.ofNullable(moderationService)
        .orElse(createServiceProxy(OpenAI.Moderations.class, null));
    return moderationService;
  }

  /**
   * Creates a dynamic proxy to handle requests to the FineTunes interface.
   * 
   * @return A "virtual" instance for the interface. It is created only once.
   */
  public OpenAI.FineTunes fineTunes() {
    fineTuneService = Optional.ofNullable(fineTuneService)
        .orElse(createServiceProxy(OpenAI.FineTunes.class, null));
    return fineTuneService;
  }

  /**
   * Creates a generic dynamic proxy with a new {@link HttpHandler HttpHandler}
   * object which will resolve the requests.
   * 
   * @param <T>              A generic interface.
   * @param serviceInterface Service of a generic interface
   * @param filter           Object that could modify the arguments passed to the
   *                         method invocation, before to be handled by
   *                         {@link HttpHandler HttpHandler}.
   * @return A "virtual" instance for the interface.
   */
  private <T> T createServiceProxy(Class<T> serviceInterface, FilterInvocation filter) {
    InvocationHandler httpHandler = new HttpHandler(httpConfig, filter);
    T serviceProxy = ReflectUtil.get().createProxy(serviceInterface, httpHandler);
    return serviceProxy;
  }

}