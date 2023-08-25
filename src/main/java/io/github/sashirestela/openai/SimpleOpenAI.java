package io.github.sashirestela.openai;

import java.lang.reflect.InvocationHandler;
import java.net.http.HttpClient;
import java.util.Optional;

import io.github.sashirestela.openai.filter.AudioFilter;
import io.github.sashirestela.openai.filter.StreamFilter;
import io.github.sashirestela.openai.http.HttpConfig;
import io.github.sashirestela.openai.http.HttpHandler;
import io.github.sashirestela.openai.http.InvocationFilter;
import io.github.sashirestela.openai.support.ReflectUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * The factory that generates implementations of the {@link OpenAI OpenAI}
 * interfaces.
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
   * Constructor used to generate a builder.
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
   * Generates an implementation of the Audios interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Audios audios() {
    audioService = Optional.ofNullable(audioService)
        .orElse(create(OpenAI.Audios.class, new AudioFilter()));
    return audioService;
  }

  /**
   * Generates an implementation of the ChatCompletions interface to handle
   * requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.ChatCompletions chatCompletions() {
    chatCompletionService = Optional.ofNullable(chatCompletionService)
        .orElse(create(OpenAI.ChatCompletions.class, new StreamFilter()));
    return chatCompletionService;
  }

  /**
   * Generates an implementation of the Completions interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Completions completions() {
    completionService = Optional.ofNullable(completionService)
        .orElse(create(OpenAI.Completions.class, new StreamFilter()));
    return completionService;
  }

  /**
   * Generates an implementation of the Embeddings interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Embeddings embeddings() {
    embeddingService = Optional.ofNullable(embeddingService)
        .orElse(create(OpenAI.Embeddings.class, null));
    return embeddingService;
  }

  /**
   * Generates an implementation of the Files interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Files files() {
    fileService = Optional.ofNullable(fileService)
        .orElse(create(OpenAI.Files.class, null));
    return fileService;
  }

  /**
   * Generates an implementation of the Images interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Images images() {
    imageService = Optional.ofNullable(imageService)
        .orElse(create(OpenAI.Images.class, null));
    return imageService;
  }

  /**
   * Generates an implementation of the Models interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Models models() {
    modelService = Optional.ofNullable(modelService)
        .orElse(create(OpenAI.Models.class, null));
    return modelService;
  }

  /**
   * Generates an implementation of the Moderations interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Moderations moderations() {
    moderationService = Optional.ofNullable(moderationService)
        .orElse(create(OpenAI.Moderations.class, null));
    return moderationService;
  }

  /**
   * Generates an implementation of the FineTunes interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.FineTunes fineTunes() {
    fineTuneService = Optional.ofNullable(fineTuneService)
        .orElse(create(OpenAI.FineTunes.class, null));
    return fineTuneService;
  }

  /**
   * Creates a generic dynamic proxy with a new {@link HttpHandler HttpHandler}
   * object which will resolve the requests.
   * 
   * @param <T>            A generic interface.
   * @param interfaceClass Service of a generic interface
   * @param filter         Object that could modify the arguments passed to the
   *                       method invocation, before to be handled by
   *                       {@link HttpHandler HttpHandler}.
   * @return A "virtual" instance for the interface.
   */
  private <T> T create(Class<T> interfaceClass, InvocationFilter filter) {
    InvocationHandler httpHandler = new HttpHandler(httpConfig, filter);
    T aProxy = ReflectUtil.get().createProxy(interfaceClass, httpHandler);
    return aProxy;
  }

}