package io.github.sashirestela.openai.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.github.sashirestela.openai.support.Constant;

public class MetadataCollector {

  public Metadata collect(Class<?> clazz) {
    List<Metadata.Annotation> classAnnotsList = new ArrayList<>();
    Annotation[] classAnnotations = clazz.getDeclaredAnnotations();
    for (Annotation annotation : classAnnotations) {
      String annotName = annotation.annotationType().getSimpleName();
      Object annotValue = getAnnotValue(annotation);
      classAnnotsList.add(new Metadata.Annotation(annotName, annotValue));
    }
    Map<String, Metadata.Method> methodsMap = new HashMap<>();
    Method[] methods = clazz.getMethods();
    for (Method method : methods) {
      List<Metadata.Parameter> paramsList = new ArrayList<>();
      Parameter[] parameters = method.getParameters();
      int index = 0;
      for (Parameter parameter : parameters) {
        Annotation annotation = Optional.ofNullable(parameter.getDeclaredAnnotations()[0]).orElse(null);
        String annotName = Optional.ofNullable(annotation.annotationType().getSimpleName()).orElse(null);
        Object annotValue = Optional.ofNullable(getAnnotValue(annotation)).orElse(null);
        Metadata.Parameter paramMetadata = Metadata.Parameter.builder()
            .index(index)
            .type(parameter.getType())
            .annotation(new Metadata.Annotation(annotName, annotValue))
            .build();
        paramsList.add(paramMetadata);
        index++;
      }
      List<Metadata.Annotation> methodAnnotsList = new ArrayList<>();
      Annotation[] methodAnnotations = method.getDeclaredAnnotations();
      for (Annotation annotation : methodAnnotations) {
        String annotName = annotation.annotationType().getSimpleName();
        Object annotValue = getAnnotValue(annotation);
        methodAnnotsList.add(new Metadata.Annotation(annotName, annotValue));
      }
      Metadata.Method methodMetadata = Metadata.Method.builder()
          .name(method.getName())
          .fullClassName(method.getGenericReturnType().getTypeName())
          .parameters(paramsList)
          .annotations(methodAnnotsList)
          .build();
      methodsMap.put(method.getName(), methodMetadata);
    }
    Metadata metadata = Metadata.builder()
        .name(clazz.getSimpleName())
        .annotations(classAnnotsList)
        .methods(methodsMap)
        .build();
    return metadata;
  }

  private Object getAnnotValue(Annotation annotation) {
    if (annotation == null) {
      return null;
    }
    Class<? extends Annotation> annotType = annotation.annotationType();
    Object value;
    try {
      Method annotAttrib = annotType.getMethod(Constant.DEF_ANNOT_ATTRIB);
      value = annotAttrib.invoke(annotation, (Object[]) null);
    } catch (Exception e) {
      value = null;
    }
    return value;
  }
}