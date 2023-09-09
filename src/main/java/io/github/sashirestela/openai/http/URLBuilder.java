package io.github.sashirestela.openai.http;

import java.util.List;

import io.github.sashirestela.openai.http.annotation.Path;
import io.github.sashirestela.openai.http.annotation.Query;
import io.github.sashirestela.openai.metadata.Metadata;

public class URLBuilder {

  private Metadata metadata;

  public URLBuilder(Metadata metadata) {
    this.metadata = metadata;
  }

  public String build(String methodName, Object[] arguments) {
    final String PATH = Path.class.getSimpleName();
    final String QUERY = Query.class.getSimpleName();

    Metadata.Method methodMetadata = metadata.getMethods().get(methodName);
    String url = methodMetadata.getUrl();
    var pathParamList = methodMetadata.getParametersByType().get(PATH);
    var queryParamList = methodMetadata.getParametersByType().get(QUERY);
    if (pathParamList.size() < 1 && queryParamList.size() < 1) {
      return url;
    }
    url = replacePathParams(url, pathParamList, arguments);
    url = includeQueryParams(url, queryParamList, arguments);
    return url;
  }

  private String replacePathParams(String url, List<Metadata.Parameter> paramList, Object[] arguments) {
    for (Metadata.Parameter paramMetadata : paramList) {
      int index = paramMetadata.getIndex();
      String pathParam = "{" + paramMetadata.getAnnotationValue() + "}";
      url = url.replace(pathParam, arguments[index].toString());
    }
    return url;
  }

  private String includeQueryParams(String url, List<Metadata.Parameter> paramList, Object[] arguments) {
    boolean first = true;
    for (Metadata.Parameter paramMetadata : paramList) {
      int index = paramMetadata.getIndex();
      Object value = arguments[index];
      if (value == null) {
        continue;
      }
      String prefix;
      if (first) {
        prefix = "?";
        first = false;
      } else {
        prefix = "&";
      }
      String queryParam = paramMetadata.getAnnotationValue();
      url = url + prefix + queryParam + "=" + value.toString();
    }
    return url;
  }
}