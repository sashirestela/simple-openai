package io.github.sashirestela.openai.http.sender;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.support.JsonUtil;

public class HttpAsyncObjectSender extends HttpSender {

  @Override
  public <T> Object sendRequest(HttpClient httpClient, HttpRequest httpRequest, Class<T> responseClass) {
    CompletableFuture<HttpResponse<String>> httpResponseFuture = httpClient.sendAsync(httpRequest,
        BodyHandlers.ofString());
    CompletableFuture<T> objResponseFuture = httpResponseFuture.thenApply(response -> {
      throwExceptionIfErrorIsPresent(response, false);
      LOGGER.debug("Response : {}", response.body());
      T objResponse = JsonUtil.get().jsonToObject(response.body(), responseClass);
      return objResponse;
    });
    return objResponseFuture;
  }
  
}