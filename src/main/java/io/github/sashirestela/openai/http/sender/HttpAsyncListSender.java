package io.github.sashirestela.openai.http.sender;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import io.github.sashirestela.openai.domain.OpenAIGeneric;
import io.github.sashirestela.openai.support.JsonUtil;

public class HttpAsyncListSender extends HttpSender {

  @Override
  @SuppressWarnings("unchecked")
  public <T> Object sendRequest(HttpClient httpClient, HttpRequest httpRequest, Class<T> responseClass) {
    CompletableFuture<HttpResponse<String>> httpResponseFuture = httpClient.sendAsync(httpRequest,
        BodyHandlers.ofString());
    CompletableFuture<List<T>> objResponseFuture = httpResponseFuture.thenApply(response -> {
      throwExceptionIfErrorIsPresent(response, false);
      LOGGER.debug("Response : {}", response.body());
      OpenAIGeneric<T> objResponse = JsonUtil.get().jsonToParametricObject(response.body(), OpenAIGeneric.class,
          responseClass);
      return objResponse.getData();
    });
    return objResponseFuture;
  }

}