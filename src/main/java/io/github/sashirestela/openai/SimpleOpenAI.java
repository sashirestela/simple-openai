package io.github.sashirestela.openai;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import io.github.sashirestela.openai.filter.AudioFilter;
import io.github.sashirestela.openai.filter.StreamFilter;
import io.github.sashirestela.openai.http.HttpProcessor;
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
  public final String AUTHORIZATION_HEADER = "Authorization";
  private final String ORGANIZATION_HEADER = "OpenAI-Organization";
  public final String BEARER_AUTHORIZATION = "Bearer ";

  @NonNull
  private String apiKey;

  private String organizationId;
  private String urlBase;
  private HttpClient httpClient;
  private HttpProcessor httpProcessor;

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

    List<String> headers = new ArrayList<>();
    headers.add(AUTHORIZATION_HEADER);
    headers.add(BEARER_AUTHORIZATION + apiKey);
    if (organizationId != null) {
      headers.add(ORGANIZATION_HEADER);
      headers.add(organizationId);
    }
    this.httpProcessor = HttpProcessor.builder()
        .httpClient(this.httpClient)
        .urlBase(this.urlBase)
        .headers(headers)
        .build();
  }

  /**
   * Generates an implementation of the Audios interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Audios audios() {
    if (audioService == null) {
      audioService = httpProcessor.create(OpenAI.Audios.class, new AudioFilter());
    }
    return audioService;
  }

  /**
   * Generates an implementation of the ChatCompletions interface to handle
   * requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.ChatCompletions chatCompletions() {
    if (chatCompletionService == null) {
      chatCompletionService = httpProcessor.create(OpenAI.ChatCompletions.class, new StreamFilter());
    }
    return chatCompletionService;
  }

  /**
   * Generates an implementation of the Completions interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Completions completions() {
    if (completionService == null) {
      completionService = httpProcessor.create(OpenAI.Completions.class, new StreamFilter());
    }
    return completionService;
  }

  /**
   * Generates an implementation of the Embeddings interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Embeddings embeddings() {
    if (embeddingService == null) {
      embeddingService = httpProcessor.create(OpenAI.Embeddings.class, null);
    }
    return embeddingService;
  }

  /**
   * Generates an implementation of the Files interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Files files() {
    if (fileService == null) {
      fileService = httpProcessor.create(OpenAI.Files.class, null);
    }
    return fileService;
  }

  /**
   * Generates an implementation of the Images interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Images images() {
    if (imageService == null) {
      imageService = httpProcessor.create(OpenAI.Images.class, null);
    }
    return imageService;
  }

  /**
   * Generates an implementation of the Models interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Models models() {
    if (modelService == null) {
      modelService = httpProcessor.create(OpenAI.Models.class, null);
    }
    return modelService;
  }

  /**
   * Generates an implementation of the Moderations interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.Moderations moderations() {
    if (moderationService == null) {
      moderationService = httpProcessor.create(OpenAI.Moderations.class, null);
    }
    return moderationService;
  }

  /**
   * Generates an implementation of the FineTunes interface to handle requests.
   * 
   * @return An instance of the interface. It is created only once.
   */
  public OpenAI.FineTunes fineTunes() {
    if (fineTuneService == null) {
      fineTuneService = httpProcessor.create(OpenAI.FineTunes.class, null);
    }
    return fineTuneService;
  }
}