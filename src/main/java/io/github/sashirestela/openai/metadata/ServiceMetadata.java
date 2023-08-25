package io.github.sashirestela.openai.metadata;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class ServiceMetadata {

  private Map<String, ServiceMetadata.Method> methodsMap;

  @AllArgsConstructor
  @Getter
  @ToString
  @Builder
  public static class Method {

    private String name;

    private String fullClassName;

    private List<ServiceMetadata.Parameter> parameters;

    private List<ServiceMetadata.Annotation> annotations;

  }

  @AllArgsConstructor
  @Getter
  @ToString
  @Builder
  public static class Parameter {

    private int index;

    private Class<?> type;

    private Annotation annotation;

  }

  @AllArgsConstructor
  @Getter
  @ToString
  public static class Annotation {

    private String name;

    private Object value;

  }
}