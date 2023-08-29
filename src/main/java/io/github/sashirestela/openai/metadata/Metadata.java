package io.github.sashirestela.openai.metadata;

import java.util.List;
import java.util.Map;

import io.github.sashirestela.openai.http.ReturnType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
@Builder
public class Metadata {

  private String name;
  private Map<String, Metadata.Method> methods;

  @AllArgsConstructor
  @Getter
  @ToString
  @Builder
  public static class Method {

    private String name;
    private ReturnType returnType;
    private Metadata.Annotation httpAnnotation;
    private boolean isMultipart;
    private String url;
    private Map<String, List<Metadata.Parameter>> parametersByType;

  }

  @AllArgsConstructor
  @Getter
  @ToString
  @Builder
  public static class Parameter {

    private int index;
    private Class<?> type;
    private String annotationValue;

  }

  @AllArgsConstructor
  @Getter
  @ToString
  public static class Annotation {

    private String name;
    private String value;

  }
}