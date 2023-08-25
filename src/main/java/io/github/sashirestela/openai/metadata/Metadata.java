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
@Builder
public class Metadata {

  private String name;

  private List<Metadata.Annotation> annotations;

  private Map<String, Metadata.Method> methods;

  @AllArgsConstructor
  @Getter
  @ToString
  @Builder
  public static class Method {

    private String name;

    private String fullClassName;

    private List<Metadata.Parameter> parameters;

    private List<Metadata.Annotation> annotations;

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