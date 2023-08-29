package io.github.sashirestela.openai.http.sender;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import io.github.sashirestela.openai.domain.OpenAIEvent;
import io.github.sashirestela.openai.support.JsonUtil;

public class HttpAsyncStreamSender extends HttpSender {

  @Override
  public <T> Object sendRequest(HttpClient httpClient, HttpRequest httpRequest, Class<T> responseClass) {
    CompletableFuture<HttpResponse<Stream<String>>> httpResponseFuture = httpClient.sendAsync(httpRequest,
        BodyHandlers.ofLines());
    CompletableFuture<Stream<T>> objResponseFuture = httpResponseFuture.thenApply(response -> {
      throwExceptionIfErrorIsPresent(response, true);
      Stream<T> objResponse = response.body()
          .peek(rawData -> LOGGER.debug("Response : {}", rawData))
          .map(rawData -> new OpenAIEvent(rawData))
          .filter(event -> event.isActualData())
          .map(event -> JsonUtil.get().jsonToObject(event.getActualData(), responseClass));
      return objResponse;
    });
    return objResponseFuture;
  }

}