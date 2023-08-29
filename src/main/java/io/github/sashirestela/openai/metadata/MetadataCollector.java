package io.github.sashirestela.openai.metadata;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.sashirestela.openai.http.ReturnType;
import io.github.sashirestela.openai.http.annotation.Resource;
import io.github.sashirestela.openai.support.Constant;

public class MetadataCollector {
  private static Logger LOGGER = LoggerFactory.getLogger(MetadataCollector.class);

  private static MetadataCollector collector;

  private MetadataCollector() {
  }

  public static MetadataCollector get() {
    if (collector == null) {
      collector = new MetadataCollector();
    }
    return collector;
  }

  public Metadata collect(Class<?> clazz) {
    String urlFromResource = Optional
        .ofNullable(getAnnotValue(clazz.getAnnotation(Resource.class)))
        .orElse("");
    Map<String, Metadata.Method> methodsMap = new HashMap<>();
    Method[] methods = clazz.getMethods();
    for (Method method : methods) {
      String fullClassName = method.getGenericReturnType().getTypeName();
      List<Metadata.Annotation> methodAnnotsList = getAnnotationsMetadata(method.getDeclaredAnnotations());
      Metadata.Annotation httpAnnotation = getAnnotIfIsInList(methodAnnotsList, Constant.HTTP_METHODS);
      boolean isMultipart = (getAnnotIfIsInList(methodAnnotsList, Constant.MULTIPART_AS_LIST) != null);
      String urlFromHttp = httpAnnotation != null ? httpAnnotation.getValue() : "";
      Map<String, List<Metadata.Parameter>> parametersByType = getParametersByType(method.getParameters());
      Metadata.Method methodMetadata = Metadata.Method.builder()
          .name(method.getName())
          .returnType(new ReturnType(fullClassName))
          .httpAnnotation(httpAnnotation)
          .isMultipart(isMultipart)
          .url(urlFromResource + urlFromHttp)
          .parametersByType(parametersByType)
          .build();
      methodsMap.put(method.getName(), methodMetadata);
    }
    Metadata metadata = Metadata.builder()
        .name(clazz.getSimpleName())
        .methods(methodsMap)
        .build();
    LOGGER.debug("Collected Metadata");
    return metadata;
  }

  private Map<String, List<Metadata.Parameter>> getParametersByType(Parameter[] parameters) {
    Map<String, List<Metadata.Parameter>> parametersByType = new HashMap<>();
    for (String paramType : Constant.PARAMETER_TYPES) {
      parametersByType.put(paramType, new ArrayList<>());
    }
    int index = 0;
    for (Parameter parameter : parameters) {
      Annotation[] annotations = parameter.getDeclaredAnnotations();
      if (annotations.length > 0) {
        Annotation annotation = annotations[0];
        String annotationValue = getAnnotValue(annotation);
        Metadata.Parameter paramMetadata = Metadata.Parameter.builder()
            .index(index)
            .type(parameter.getType())
            .annotationValue(annotationValue)
            .build();
        String annotationName = annotation.annotationType().getSimpleName();
        List<Metadata.Parameter> paramList = parametersByType.get(annotationName);
        if (paramList != null) {
          paramList.add(paramMetadata);
          parametersByType.put(annotationName, paramList);
        }
      }
      index++;
    }
    return parametersByType;
  }

  private List<Metadata.Annotation> getAnnotationsMetadata(Annotation[] annotations) {
    List<Metadata.Annotation> annotationsMetadata = new ArrayList<>();
    for (Annotation annotation : annotations) {
      String annotName = annotation.annotationType().getSimpleName();
      String annotValue = getAnnotValue(annotation);
      annotationsMetadata.add(new Metadata.Annotation(annotName, annotValue));
    }
    return annotationsMetadata;
  }

  private String getAnnotValue(Annotation annotation) {
    if (annotation == null) {
      return null;
    }
    Object value;
    Class<? extends Annotation> annotType = annotation.annotationType();
    try {
      Method annotAttrib = annotType.getMethod(Constant.DEF_ANNOT_ATTRIB);
      value = annotAttrib.invoke(annotation, (Object[]) null);
    } catch (Exception e) {
      value = null;
    }
    return (String) value;
  }

  private Metadata.Annotation getAnnotIfIsInList(List<Metadata.Annotation> annotations, List<String> annotationNames) {
    if (annotations.size() == 0) {
      return null;
    } else {
      return annotations.stream()
          .filter(annot -> annotationNames.contains(annot.getName()))
          .findFirst().orElse(null);
    }
  }
}