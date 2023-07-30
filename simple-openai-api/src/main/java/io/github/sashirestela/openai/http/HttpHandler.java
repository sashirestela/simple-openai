package io.github.sashirestela.openai.http;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

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
import io.github.sashirestela.openai.support.Reflection;

public class HttpHandler implements InvocationHandler {

  private final List<Class<? extends Annotation>> HTTP_METHODS =
    Arrays.asList(GET.class, POST.class, PUT.class, DELETE.class);

  private HttpClient httpClient;
  private String apiKey;
  private String urlBase;

  public HttpHandler(HttpClient httpClient,
                     String apiKey,
                     String urlBase) {
    this.httpClient = httpClient;
    this.apiKey = apiKey;
    this.urlBase = urlBase;
  }

  @Override
  public Object invoke(Object proxy,
                       Method method,
                       Object[] args) throws Throwable {
    Class<? extends Annotation> httpMethodClass = setHttpMethodClass(method);
    String url = setUrl(method, args, httpMethodClass);
    Pair<Parameter, Object> pair = Reflection.one().getAnnotatedArgument(method, args, Body.class);
    setStreamIfApplicable(method, pair);
    HttpRequest.BodyPublisher bodyPublisher = setBodyPublisher(method, pair);
    Class<?> responseClass = Reflection.one().getMethodClass(method);

    HttpRequest.Builder builder = HttpRequest.newBuilder();
    builder = builder.uri(URI.create(urlBase + url));
    builder = builder.header(Constant.HEADER_CONTENT_TYPE, Constant.CONTENT_JSON);
    builder = builder.header(Constant.HEADER_AUTHORIZATION, Constant.AUTH_BEARER + apiKey);
    builder = setHttpMethod(builder, httpMethodClass, bodyPublisher);
    HttpRequest httpRequest = builder.build();

    if (method.isAnnotationPresent(Streaming.class)) {
      HttpResponse<Stream<String>> httpResponse = httpClient.send(httpRequest, BodyHandlers.ofLines());
      final String CONSUMABLE_TEXT = "\"content\":";
      final int CONSUMABLE_INDEX = 6;
      return (Stream<?>) httpResponse.body()
        .filter(data -> data.contains(CONSUMABLE_TEXT))
        .map(data -> data.substring(CONSUMABLE_INDEX))
        .map(data -> JsonUtil.one().jsonToObject(data, responseClass));
    } else {
      HttpResponse<String> httpResponse = httpClient.send(httpRequest, BodyHandlers.ofString());
      return JsonUtil.one().jsonToObject(httpResponse.body(), responseClass);
    }
  }

  private Class<? extends Annotation> setHttpMethodClass(Method method) {
    Optional<Class<? extends Annotation>> optHttpMethodClass = Reflection.one().getAnnotationFromList(method, HTTP_METHODS);
    if (optHttpMethodClass.isEmpty()) {
      throw new RuntimeException("The method '" + method.getName() + "' is not annotated as a Http Method.");
    }
    return optHttpMethodClass.get();
  }

  private String setUrl(Method method, Object[] args, Class<? extends Annotation> httpMethodClass) throws Exception {
    String url = (String) Reflection.one().getAnnotationAttribute(method, httpMethodClass, "value");
    if (args == null || args.length == 0) {
      return url;
    }
    Pair<Parameter, Object> pair = Reflection.one().getAnnotatedArgument(method, args, Path.class);
    if (pair == null) {
      return url;
    }
    String paramName = (String) Reflection.one().getAnnotationAttribute(pair.getFirst(), Path.class, "value");
    String paramaValue = pair.getSecond().toString();
    String pattern = "{" + paramName + "}";
    return url.replace(pattern, paramaValue);
  }

  private void setStreamIfApplicable(Method method, Pair<Parameter, Object> pair) throws Exception {
    if (pair == null) {
      return;
    }
    try {
      Parameter parameter = pair.getFirst();
      Object object = pair.getSecond();
      String setStreamMethodName = "setStream";
		  Method setStreamMethod = parameter.getType().getMethod(setStreamMethodName, boolean.class);
      boolean streamValue = method.isAnnotationPresent(Streaming.class);
      setStreamMethod.invoke(object, streamValue);
      pair.setSecond(object);
    } catch (Exception e) {
      return;
    }
  }

  private HttpRequest.BodyPublisher setBodyPublisher(Method method, Pair<Parameter, Object> pair) {
    if (pair == null) {
      return null;
    }
    String requestJson = JsonUtil.one().objectToJson(pair.getSecond());
    return BodyPublishers.ofString(requestJson);
  }

  private HttpRequest.Builder setHttpMethod(HttpRequest.Builder builder, Class<?> httpMethodClass, HttpRequest.BodyPublisher publisher) throws Exception {
    String httpMethodName = httpMethodClass.getSimpleName();
    switch (httpMethodName) {
      case "GET": return builder.GET();
      case "POST": return builder.POST(publisher);
      case "PUT": return builder.PUT(publisher);
      case "DELETE": return builder.DELETE();
      default: return null;
    }
  }
}