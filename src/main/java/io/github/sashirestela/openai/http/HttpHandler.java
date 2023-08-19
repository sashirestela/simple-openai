package io.github.sashirestela.openai.http;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.domain.OpenAIError;
import io.github.sashirestela.openai.domain.OpenAIEvent;
import io.github.sashirestela.openai.domain.OpenAIGeneric;
import io.github.sashirestela.openai.http.annotation.Body;
import io.github.sashirestela.openai.http.annotation.DELETE;
import io.github.sashirestela.openai.http.annotation.GET;
import io.github.sashirestela.openai.http.annotation.Multipart;
import io.github.sashirestela.openai.http.annotation.POST;
import io.github.sashirestela.openai.http.annotation.PUT;
import io.github.sashirestela.openai.http.annotation.Path;
import io.github.sashirestela.openai.support.CommonUtil;
import io.github.sashirestela.openai.support.Constant;
import io.github.sashirestela.openai.support.JsonUtil;
import io.github.sashirestela.openai.support.MethodElement;
import io.github.sashirestela.openai.support.ReflectUtil;

public class HttpHandler implements InvocationHandler {
  private static Logger LOGGER = LoggerFactory.getLogger(HttpHandler.class);

  private final static List<Class<? extends Annotation>> HTTP_METHODS = Arrays.asList(
      GET.class, POST.class, PUT.class, DELETE.class);

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
    LOGGER.debug("Invoking {}.{}()", method.getDeclaringClass().getSimpleName(), method.getName());
    try {
      Class<? extends Annotation> httpMethod = this.calculateHttpMethod(method);
      String url = this.calculateUrl(method, arguments, httpMethod);
      MethodElement elementBody = ReflectUtil.get().getMethodElementAnnotatedWith(method, arguments, Body.class);
      ResponseType responseType = ReflectUtil.get().getResponseType(method);
      boolean isStreaming = (responseType == ResponseType.STREAM);
      this.setStreamInBodyIfApplicable(method, elementBody, isStreaming);
      boolean isMultipart = method.isAnnotationPresent(Multipart.class);
      BodyPublisher bodyPublisher = this.calculateBodyPublisher(elementBody, isMultipart);
      Class<?> responseClass = ReflectUtil.get().getBaseClass(method);

      HttpRequest.Builder builder = HttpRequest.newBuilder();
      builder = builder.uri(URI.create(urlBase + url));
      builder = builder.header(Constant.HEADER_CONTENT_TYPE, this.calculateContentType(isMultipart));
      builder = builder.header(Constant.HEADER_AUTHORIZATION, Constant.AUTH_BEARER + apiKey);
      builder = builder.method(httpMethod.getSimpleName(), bodyPublisher);
      HttpRequest httpRequest = builder.build();

      CompletableFuture<?> responseObject = null;
      switch (responseType) {
        case OBJECT:
          responseObject = this.callToReceiveFutureObject(httpRequest, responseClass);
          break;
        case LIST:
          responseObject = this.callToReceiveFutureList(httpRequest, responseClass);
          break;
        case STREAM:
          responseObject = this.callToReceiveFutureStream(httpRequest, responseClass);
          break;
        default:
          throw new SimpleUncheckedException("Unsupported return type for method {0} of the class {1}.",
              method.getName(), method.getDeclaringClass().getSimpleName(), null);
      }
      return responseObject;
    } catch (Throwable e) {
      LOGGER.error("Cannot complete http request.", e);
      throw new SimpleUncheckedException("Error trying to execute the method {0} of the class {1}.", method.getName(),
          method.getDeclaringClass().getSimpleName(), e);
    }
  }

  private Class<? extends Annotation> calculateHttpMethod(Method method) {
    Class<? extends Annotation> httpMethod = Optional
        .ofNullable(ReflectUtil.get().getFirstAnnotTypeInList(method, HTTP_METHODS)).orElseThrow(
            () -> new SimpleUncheckedException("Missing HTTP anotation for the method {0}.", method.getName(), null));
    return httpMethod;
  }

  private String calculateUrl(Method method, Object[] arguments, Class<? extends Annotation> httpMethod) {
    String url = (String) ReflectUtil.get().getAnnotAttribValue(method, httpMethod, Constant.DEF_ANNOT_ATTRIB);
    boolean urlContainsPathParam = CommonUtil.get().matches(url, Constant.REGEX_PATH_PARAM_URL);

    if (!urlContainsPathParam) {
      return url;
    } else if (CommonUtil.get().isNullOrEmpty(arguments)) {
      throw new SimpleUncheckedException("Path param in the url requires at least an argument in the method {0}.",
          method.getName(), null);
    }

    List<CommonUtil.Match> listPathParams = CommonUtil.get().findAllMatches(url, Constant.REGEX_PATH_PARAM_URL);
    List<MethodElement> listMethodElement = ReflectUtil.get().getMethodElementsAnnotatedWith(method, arguments,
        Path.class);
    if (CommonUtil.get().isNullOrEmpty(listMethodElement)) {
      throw new SimpleUncheckedException(
          "Path param in the url requires at least an annotated argument in the method {0}.",
          method.getName(), null);
    }

    for (CommonUtil.Match pathParam : listPathParams) {
      MethodElement methodElement = listMethodElement.stream()
          .filter(methElem -> methElem.getDefAnnotValue().equals(pathParam.getTextToSearch()))
          .findFirst()
          .orElseThrow(() -> new SimpleUncheckedException(
              "Path param {0} in the url cannot find an annotated argument in the method {1}.",
              pathParam.getTextToSearch(), method.getName(), null));
      url = url.replace(pathParam.getTextToReplace(), methodElement.getArgumentValue().toString());
    }
    return url;
  }

  private void setStreamInBodyIfApplicable(Method method, MethodElement elementBody, boolean isStreaming) {
    if (elementBody == null) {
      return;
    }
    final String SET_STREAM_METHOD = "setStream";
    Parameter parameter = elementBody.getParameter();
    Object object = elementBody.getArgumentValue();
    try {
      ReflectUtil.get().executeSetMethod(parameter.getType(), SET_STREAM_METHOD, new Class<?>[] { boolean.class },
          object, isStreaming);
      elementBody.setArgumentValue(object);
    } catch (SimpleUncheckedException e) {
      // 'setStream' method does not exist
      return;
    }
  }

  private String calculateContentType(boolean isMultipart) {
    String contentType = null;
    if (isMultipart) {
      contentType = Constant.TYPE_MULTIPART + Constant.BOUNDARY_TITLE + "\"" + Constant.BOUNDARY_VALUE + "\"";
    } else {
      contentType = Constant.TYPE_APP_JSON;
    }
    return contentType;
  }

  private BodyPublisher calculateBodyPublisher(MethodElement elementBody, boolean isMultipart) {
    if (elementBody == null) {
      return BodyPublishers.noBody();
    }
    Object object = elementBody.getArgumentValue();
    if (isMultipart) {
      Map<String, Object> data = ReflectUtil.get().getMapFields(object);
      List<byte[]> requestBytes = MultipartFormData.get().toByteArrays(data);
      LOGGER.debug("Request: {}", data);
      return BodyPublishers.ofByteArrays(requestBytes);
    } else {
      String requestString = JsonUtil.get().objectToJson(object);
      LOGGER.debug("Request: {}", requestString);
      return BodyPublishers.ofString(requestString);
    }
  }

  private <T> CompletableFuture<T> callToReceiveFutureObject(HttpRequest httpRequest, Class<T> responseClass) {
    CompletableFuture<HttpResponse<String>> httpResponseFuture = httpClient.sendAsync(httpRequest,
        BodyHandlers.ofString());
    CompletableFuture<T> objResponseFuture = httpResponseFuture.thenApply(response -> {
      throwExceptionIfErrorIsPresent(response, false);
      LOGGER.debug("Response: {}", response.body());
      T objResponse = JsonUtil.get().jsonToObject(response.body(), responseClass);
      return objResponse;
    });
    return objResponseFuture;
  }

  @SuppressWarnings("unchecked")
  private <T> CompletableFuture<List<T>> callToReceiveFutureList(HttpRequest httpRequest, Class<T> responseClass) {
    CompletableFuture<HttpResponse<String>> httpResponseFuture = httpClient.sendAsync(httpRequest,
        BodyHandlers.ofString());
    CompletableFuture<List<T>> objResponseFuture = httpResponseFuture.thenApply(response -> {
      throwExceptionIfErrorIsPresent(response, false);
      LOGGER.debug("Response: {}", response.body());
      OpenAIGeneric<T> objResponse = JsonUtil.get().jsonToParametricObject(response.body(), OpenAIGeneric.class,
          responseClass);
      return objResponse.getData();
    });
    return objResponseFuture;
  }

  private <T> CompletableFuture<Stream<T>> callToReceiveFutureStream(HttpRequest httpRequest, Class<T> responseClass) {
    CompletableFuture<HttpResponse<Stream<String>>> httpResponseFuture = httpClient.sendAsync(httpRequest,
        BodyHandlers.ofLines());
    CompletableFuture<Stream<T>> objResponseFuture = httpResponseFuture.thenApply(response -> {
      throwExceptionIfErrorIsPresent(response, true);
      Stream<T> objResponse = response.body()
          .peek(rawData -> LOGGER.debug("Response: {}", rawData))
          .map(rawData -> new OpenAIEvent(rawData))
          .filter(event -> event.isActualData())
          .map(event -> JsonUtil.get().jsonToObject(event.getActualData(), responseClass));
      return objResponse;
    });
    return objResponseFuture;
  }

  @SuppressWarnings("unchecked")
  private void throwExceptionIfErrorIsPresent(HttpResponse<?> response, boolean isStream) {
    if (response.statusCode() != HttpURLConnection.HTTP_OK) {
      String data = null;
      if (isStream) {
        data = ((Stream<String>) response.body())
            .peek(error -> LOGGER.debug("Response: {}", error))
            .collect(Collectors.joining());
      } else {
        data = (String) response.body();
        LOGGER.debug("Response: {}", data);
      }
      OpenAIError openAIError = JsonUtil.get().jsonToObject(data, OpenAIError.class);
      throw new SimpleUncheckedException("Error from server: {0}.", openAIError.getError(), null);
    }
  }
}