package io.github.sashirestela.openai.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MetadataCollector {

  public ServiceMetadata collect(Class<?> clazz) {
    Map<String, ServiceMetadata.Method> methodsMap = new HashMap<>();
    Method[] methods = clazz.getMethods();
    for (Method method : methods) {
      List<ServiceMetadata.Parameter> paramsList = new ArrayList<>();
      Parameter[] parameters = method.getParameters();
      int index = 0;
      for (Parameter parameter : parameters) {
        Annotation annotation = Optional.ofNullable(parameter.getDeclaredAnnotations()[0]).orElse(null);
        String annotName = Optional.ofNullable(annotation.annotationType().getSimpleName()).orElse(null);
        Object annotValue = Optional.ofNullable(getAnnotValue(annotation)).orElse(null);
        ServiceMetadata.Parameter paramMetadata = ServiceMetadata.Parameter.builder()
            .index(index)
            .type(parameter.getType())
            .annotation(new ServiceMetadata.Annotation(annotName, annotValue))
            .build();
        paramsList.add(paramMetadata);
        index++;
      }
      List<ServiceMetadata.Annotation> annotsList = new ArrayList<>();
      Annotation[] annotations = method.getDeclaredAnnotations();
      for (Annotation annotation : annotations) {
        String annotName = Optional.ofNullable(annotation.annotationType().getSimpleName()).orElse(null);
        Object annotValue = Optional.ofNullable(getAnnotValue(annotation)).orElse(null);
        annotsList.add(new ServiceMetadata.Annotation(annotName, annotValue));
      }
      ServiceMetadata.Method methodMetadata = ServiceMetadata.Method.builder()
          .name(method.getName())
          .fullClassName(method.getGenericReturnType().getTypeName())
          .parameters(paramsList)
          .annotations(annotsList)
          .build();
      methodsMap.put(method.getName(), methodMetadata);
    }
    ServiceMetadata serviceMetadata = new ServiceMetadata(methodsMap);
    return serviceMetadata;
  }

  private Object getAnnotValue(Annotation annotation) {
    if (annotation == null) {
      return null;
    }
    Class<? extends Annotation> annotType = annotation.annotationType();
    Object value;
    try {
      Method annotAttrib = annotType.getMethod("value");
      value = annotAttrib.invoke(annotation, (Object[]) null);
    } catch (Exception e) {
      value = null;
    }
    return value;
  }
}