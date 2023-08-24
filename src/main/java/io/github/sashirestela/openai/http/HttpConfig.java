package io.github.sashirestela.openai.http;

import java.net.http.HttpClient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class HttpConfig {

  @NonNull
  private String urlBase;

  @NonNull
  private String apiKey;

  @NonNull
  private HttpClient httpClient;

  private String[] headers;

}