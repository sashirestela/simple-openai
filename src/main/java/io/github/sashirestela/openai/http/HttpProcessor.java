package io.github.sashirestela.openai.http;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.http.annotation.Body;
import io.github.sashirestela.openai.http.annotation.Path;
import io.github.sashirestela.openai.metadata.Metadata;
import io.github.sashirestela.openai.metadata.MetadataCollector;
import io.github.sashirestela.openai.support.CommonUtil;
import io.github.sashirestela.openai.support.Constant;
import io.github.sashirestela.openai.support.ReflectUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;

@AllArgsConstructor
public class HttpProcessor {
  private static Logger LOGGER = LoggerFactory.getLogger(HttpProcessor.class);

  @NonNull
  private HttpClient httpClient;

  @NonNull
  private String urlBase;

  private List<String> headers;
  private Metadata metadata;
  private URLBuilder urlBuilder;

  @Builder
  public HttpProcessor(HttpClient httpClient, String urlBase, List<String> headers) {
    this.httpClient = httpClient;
    this.urlBase = urlBase;
    this.headers = headers;
  }

  /**
   * Creates a generic dynamic proxy with a new {@link HttpInvocationHandler HttpInvocationHandler}
   * object which will resolve the requests.
   * 
   * @param <T>            A generic interface.
   * @param interfaceClass Service of a generic interface
   * @param filter         Object that could modify the arguments passed to the
   *                       method invocation, before to be handled by
   *                       {@link HttpInvocationHandler HttpInvocationHandler}.
   * @return A "virtual" instance for the interface.
   */
  public <T> T create(Class<T> interfaceClass, InvocationFilter filter) {
    metadata = MetadataCollector.get().collect(interfaceClass);
    validateMetadata();
    urlBuilder = new URLBuilder(metadata);
    InvocationHandler httpInvocationHandler = new HttpInvocationHandler(this, filter);
    T proxy = ReflectUtil.get().createProxy(interfaceClass, httpInvocationHandler);
    LOGGER.debug("Created Instance : {}", interfaceClass.getSimpleName());
    return proxy;
  }

  public Object resolve(Method method, Object[] arguments) {
    String methodName = method.getName();
    Metadata.Method methodMetadata = metadata.getMethods().get(methodName);
    String url = urlBase + urlBuilder.build(methodName, arguments);
    String httpMethod = methodMetadata.getHttpAnnotation().getName();
    ReturnType returnType = methodMetadata.getReturnType();
    boolean isMultipart = methodMetadata.isMultipart();
    Object bodyObject = calculateBodyObject(methodMetadata, arguments);
    List<String> headers = new ArrayList<>(this.headers);
    headers.addAll(calculateHeaderContentType(bodyObject, isMultipart));
    String[] headersArray = headers.toArray(new String[0]);
    HttpConnector httpConnector = HttpConnector.builder()
        .httpClient(httpClient)
        .url(url)
        .httpMethod(httpMethod)
        .returnType(returnType)
        .bodyObject(bodyObject)
        .isMultipart(isMultipart)
        .headersArray(headersArray)
        .build();
    LOGGER.debug("Http Call : {} {}", httpMethod, url);
    Object responseObject = httpConnector.sendRequest();
    return responseObject;
  }

  private void validateMetadata() {
    metadata.getMethods().forEach((methodName, methodMetadata) -> Optional
        .ofNullable(methodMetadata.getHttpAnnotation())
        .orElseThrow(
            () -> new SimpleUncheckedException("Missing HTTP anotation for the method {0}.", methodName, null)));

    final String PATH = Path.class.getSimpleName();
    metadata.getMethods().forEach((methodName, methodMetadata) -> {
      String url = methodMetadata.getUrl();
      List<String> listPathParams = CommonUtil.get().findFullMatches(url, Constant.REGEX_PATH_PARAM_URL);
      if (!CommonUtil.get().isNullOrEmpty(listPathParams)) {
        listPathParams.forEach(pathParam -> methodMetadata.getParametersByType().get(PATH).stream()
            .filter(paramMetadata -> pathParam.equals(paramMetadata.getAnnotationValue())).findFirst()
            .orElseThrow(() -> new SimpleUncheckedException(
                "Path param {0} in the url cannot find an annotated argument in the method {1}.", pathParam, methodName,
                null)));
      }
    });
  }

  private Object calculateBodyObject(Metadata.Method methodMetadata, Object[] arguments) {
    final String BODY = Body.class.getSimpleName();
    Integer indexBody = methodMetadata.getParametersByType().get(BODY).stream()
        .map(paramMetadata -> paramMetadata.getIndex()).findFirst().orElse(-1);
    Object bodyObject = indexBody >= 0 ? arguments[indexBody] : null;
    return bodyObject;
  }

  private List<String> calculateHeaderContentType(Object bodyObject, boolean isMultipart) {
    List<String> headerContentType = new ArrayList<>();
    if (bodyObject != null) {
      String contentType = isMultipart
          ? Constant.TYPE_MULTIPART + Constant.BOUNDARY_TITLE + "\"" + Constant.BOUNDARY_VALUE + "\""
          : Constant.TYPE_APP_JSON;
      headerContentType.add(Constant.HEADER_CONTENT_TYPE);
      headerContentType.add(contentType);
    }
    return headerContentType;
  }
}