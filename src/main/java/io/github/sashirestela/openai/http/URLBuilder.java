package io.github.sashirestela.openai.http;

import java.util.List;

import io.github.sashirestela.openai.http.annotation.Path;
import io.github.sashirestela.openai.http.annotation.Query;
import io.github.sashirestela.openai.metadata.Metadata;
import io.github.sashirestela.openai.support.CommonUtil;
import io.github.sashirestela.openai.support.Constant;

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
    boolean urlContainsPathParam = CommonUtil.get().matches(url, Constant.REGEX_PATH_PARAM_URL);
    if (!urlContainsPathParam) {
      return url;
    }
    url = replacePathParams(url, methodMetadata.getParametersByType().get(PATH), arguments);
    url = includeQueryParams(url, methodMetadata.getParametersByType().get(QUERY), arguments);
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
      String prefix;
      if (first) {
        prefix = "?";
        first = false;
      } else {
        prefix = "&";
      }
      int index = paramMetadata.getIndex();
      String queryParam = paramMetadata.getAnnotationValue();
      url = url + prefix + queryParam + "=" + arguments[index].toString();
    }
    return url;
  }
}