package io.github.sashirestela.openai.http.sender;

import java.net.HttpURLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.domain.OpenAIError;
import io.github.sashirestela.openai.support.JsonUtil;

public abstract class HttpSender {
  protected static Logger LOGGER = LoggerFactory.getLogger(HttpSender.class);

  public abstract <T> Object sendRequest(HttpClient httpClient, HttpRequest httpRequest, Class<T> responseClass);

  @SuppressWarnings("unchecked")
  protected void throwExceptionIfErrorIsPresent(HttpResponse<?> response, boolean isStream) {
    if (response.statusCode() != HttpURLConnection.HTTP_OK) {
      String data = null;
      if (isStream) {
        data = ((Stream<String>) response.body())
            .peek(error -> LOGGER.debug("Response : {}", error))
            .collect(Collectors.joining());
      } else {
        data = (String) response.body();
        LOGGER.debug("Response : {}", data);
      }
      OpenAIError openAIError = JsonUtil.get().jsonToObject(data, OpenAIError.class);
      throw new SimpleUncheckedException("ERROR : {0}", openAIError.getError(), null);
    }
  }

}