package io.github.sashirestela.openai.http;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.sashirestela.openai.http.sender.HttpSender;
import io.github.sashirestela.openai.http.sender.HttpSenderFactory;
import io.github.sashirestela.openai.support.JsonUtil;
import io.github.sashirestela.openai.support.ReflectUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class HttpConnector {
  private static Logger LOGGER = LoggerFactory.getLogger(HttpConnector.class);

  private HttpClient httpClient;
  private String url;
  private String httpMethod;
  private ReturnType returnType;
  private Object bodyObject;
  private boolean isMultipart;
  private String[] headersArray;

  public Object sendRequest() {
    BodyPublisher bodyPublisher = createBodyPublisher(bodyObject, isMultipart);
    Class<?> responseClass = returnType.getBaseClass();
    HttpRequest httpRequest = HttpRequest.newBuilder()
        .uri(URI.create(url))
        .headers(headersArray)
        .method(httpMethod, bodyPublisher)
        .build();
    HttpSender httpSender = HttpSenderFactory.get().createSender(returnType);
    Object responseObject = httpSender.sendRequest(httpClient, httpRequest, responseClass);
    return responseObject;
  }

  private BodyPublisher createBodyPublisher(Object bodyObject, boolean isMultipart) {
    BodyPublisher bodyPublisher = null;
    if (bodyObject == null) {
      LOGGER.debug("Body Request: (Empty)");
      bodyPublisher = BodyPublishers.noBody();
    } else if (isMultipart) {
      Map<String, Object> data = ReflectUtil.get().getMapFields(bodyObject);
      List<byte[]> requestBytes = HttpMultipart.get().toByteArrays(data);
      LOGGER.debug("Body Request: {}", data);
      bodyPublisher = BodyPublishers.ofByteArrays(requestBytes);
    } else {
      String requestString = JsonUtil.get().objectToJson(bodyObject);
      LOGGER.debug("Body Request: {}", requestString);
      bodyPublisher = BodyPublishers.ofString(requestString);
    }
    return bodyPublisher;
  }
}