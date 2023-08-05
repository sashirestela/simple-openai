package io.github.sashirestela.openai.http;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import io.github.sashirestela.openai.domain.OpenAIError;
import io.github.sashirestela.openai.exception.SimpleUncheckedException;
import io.github.sashirestela.openai.exception.UncheckedException;
import io.github.sashirestela.openai.http.annotation.Body;
import io.github.sashirestela.openai.http.annotation.DELETE;
import io.github.sashirestela.openai.http.annotation.GET;
import io.github.sashirestela.openai.http.annotation.POST;
import io.github.sashirestela.openai.http.annotation.PUT;
import io.github.sashirestela.openai.http.annotation.Path;
import io.github.sashirestela.openai.http.annotation.Streaming;
import io.github.sashirestela.openai.support.Constant;
import io.github.sashirestela.openai.support.JsonUtil;
import io.github.sashirestela.openai.support.Pair;
import io.github.sashirestela.openai.support.ReflectUtil;

public class HttpHandler implements InvocationHandler {
  private final static List<Class<? extends Annotation>> HTTP_METHODS = Arrays.asList(GET.class, POST.class, PUT.class,
      DELETE.class);

  private HttpClient httpClient;
  private String apiKey;
  private String urlBase;

  public HttpHandler(HttpClient httpClient, String apiKey, String urlBase) {
    this.httpClient = httpClient;
    this.apiKey = apiKey;
    this.urlBase = urlBase;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] arguments) throws Throwable {
    try {
      Class<? extends Annotation> httpMethod = calculateHttpMethod(method);
      String url = calculateUrl(method, arguments, httpMethod);
      Pair<Parameter, Object> pairBody = ReflectUtil.get().getArgumentAnnotatedWith(method, arguments, Body.class);
      setStreamInBodyIfApplicable(method, pairBody);
      HttpRequest.BodyPublisher bodyPublisher = calculateBodyPublisher(method, pairBody);
      Class<?> responseClass = ReflectUtil.get().getReturnClassOf(method);

      HttpRequest.Builder builder = HttpRequest.newBuilder();
      builder = builder.uri(URI.create(urlBase + url));
      builder = builder.header(Constant.HEADER_ACCEPT, Constant.APP_JSON);
      builder = builder.header(Constant.HEADER_CONTENT_TYPE, Constant.APP_JSON);
      builder = builder.header(Constant.HEADER_AUTHORIZATION, Constant.AUTH_BEARER + apiKey);
      builder = setHttpMethodForRequet(builder, httpMethod, bodyPublisher);
      HttpRequest httpRequest = builder.build();

      Object responseObject = null;
      if (method.isAnnotationPresent(Streaming.class)) {
        responseObject = calculateResponseStream(httpRequest, responseClass);
      } else {
        responseObject = calculateResponse(httpRequest, responseClass);
      }
      return responseObject;
    } catch (RuntimeException e) {
      throw new SimpleUncheckedException("Error trying to execute the method {0} of the class {1}.", method.getName(),
          method.getDeclaringClass().getSimpleName(), e);
    }
  }

  private Class<? extends Annotation> calculateHttpMethod(Method method) {
    Class<? extends Annotation> httpMethod = ReflectUtil.get().getFirstAnnotationTypeInList(method, HTTP_METHODS);
    if (httpMethod == null) {
      throw new UncheckedException("Missing HTTP anotation for the method {0}.", method.getName(), null);
    }
    return httpMethod;
  }

  private String calculateUrl(Method method, Object[] arguments, Class<? extends Annotation> httpMethod) {
    String url = (String) ReflectUtil.get().getAnnotationAttribute(method, httpMethod, "value");
    if (arguments == null || arguments.length == 0) {
      return url;
    }
    Pair<Parameter, Object> pairPath = ReflectUtil.get().getArgumentAnnotatedWith(method, arguments, Path.class);
    if (pairPath == null) {
      return url;
    }
    Parameter parameter = pairPath.getFirst();
    Object argument = pairPath.getSecond();
    String paramName = (String) ReflectUtil.get().getAnnotationAttribute(parameter, Path.class, "value");
    String argumentValue = argument.toString();
    String pattern = "{" + paramName + "}";
    return url.replace(pattern, argumentValue);
  }

  private void setStreamInBodyIfApplicable(Method method, Pair<Parameter, Object> pairBody) {
    if (pairBody == null) {
      return;
    }
    Parameter parameter = pairBody.getFirst();
    Object object = pairBody.getSecond();
    String setStreamMethodName = "setStream";
    boolean streamValue = method.isAnnotationPresent(Streaming.class);
    try {
      Method setStreamMethod = parameter.getType().getMethod(setStreamMethodName, boolean.class);
      setStreamMethod.invoke(object, streamValue);
      pairBody.setSecond(object);
    } catch (Exception e) {
      // 'setStream' method does not exist
      return;
    }
  }

  private HttpRequest.BodyPublisher calculateBodyPublisher(Method method, Pair<Parameter, Object> pairBody) {
    if (pairBody == null) {
      return null;
    }
    Object object = pairBody.getSecond();
    String requestJson = JsonUtil.get().objectToJson(object);
    return BodyPublishers.ofString(requestJson);
  }

  private HttpRequest.Builder setHttpMethodForRequet(HttpRequest.Builder builder,
      Class<? extends Annotation> httpMethod, HttpRequest.BodyPublisher publisher) {
    String httpMethodName = httpMethod.getSimpleName();
    switch (httpMethodName) {
      case "GET":
        return builder.GET();
      case "POST":
        return builder.POST(publisher);
      case "PUT":
        return builder.PUT(publisher);
      case "DELETE":
        return builder.DELETE();
      default:
        return null;
    }
  }

  private <T> Stream<T> calculateResponseStream(HttpRequest httpRequest, Class<T> responseClass) {
    HttpResponse<Stream<String>> httpResponse;
    try {
      httpResponse = httpClient.send(httpRequest, BodyHandlers.ofLines());
    } catch (IOException | InterruptedException e) {
      throw new UncheckedException("Error trying to communicate to {0}.", httpRequest.uri(), e);
    }
    throwExceptionIfErrorIsPresent(httpResponse);
    final String CONSUMABLE_TEXT = "\"content\":";
    final int CONSUMABLE_INDEX = 6;
    Stream<T> responseObject = httpResponse.body()
        .filter(data -> data.contains(CONSUMABLE_TEXT))
        .map(data -> data.substring(CONSUMABLE_INDEX))
        .map(data -> JsonUtil.get().jsonToObject(data, responseClass));
    return responseObject;
  }

  private <T> T calculateResponse(HttpRequest httpRequest, Class<T> responseClass) {
    HttpResponse<String> httpResponse;
    try {
      httpResponse = httpClient.send(httpRequest, BodyHandlers.ofString());
    } catch (IOException | InterruptedException e) {
      throw new UncheckedException("Error trying to communicate to {0}.", httpRequest.uri(), e);
    }
    throwExceptionIfErrorIsPresent(httpResponse);
    String data = httpResponse.body();
    T responseObject = JsonUtil.get().jsonToObject(data, responseClass);
    return responseObject;
  }

  private void throwExceptionIfErrorIsPresent(HttpResponse<?> httpResponse) {
    if (httpResponse.statusCode() != HttpURLConnection.HTTP_OK) {
      String data = (String) httpResponse.body();
      OpenAIError error = JsonUtil.get().jsonToObject(data, OpenAIError.class);
      throw new UncheckedException("Error from server: {0}.", error.getError(), null);
    }
  }
}