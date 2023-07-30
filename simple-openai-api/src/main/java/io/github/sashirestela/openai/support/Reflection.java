package io.github.sashirestela.openai.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Reflection {
  private static Reflection reflection = null;

  private Reflection() {}

  public static Reflection one() {
    if (reflection == null) {
      reflection = new Reflection();
    }
    return reflection;
  }

  public Optional<Class<? extends Annotation>>
    getAnnotationFromList(Method method,
                          List<Class<? extends Annotation>> listTypes) {
    Optional<Class<? extends Annotation>> optType = listTypes.stream()
      .filter(type -> method.isAnnotationPresent(type))
      .findFirst();
    return optType;
  }

  public Object getAnnotationAttribute(AnnotatedElement element,
                                       Class<? extends Annotation> type,
                                       String annotationMethodName) throws Exception {
    Object value = null;
    Annotation annotation = element.getAnnotation(type);
    if (annotation != null) {
      Method annotationMethod = type.getMethod(annotationMethodName);
      value = annotationMethod.invoke(annotation, (Object[])null);
    }
    return value;
  }

  public Pair<Parameter, Object> getAnnotatedArgument(Method method,
                                                      Object[] args,
                                                      Class<? extends Annotation> type) {
    int i = 0;
    Parameter[] parameters = method.getParameters();
    for (Parameter parameter : parameters) {
      if (parameter.isAnnotationPresent(type)) {
        return new Pair(parameter, args[i]);
      }
      i++;
    }
    return null;
  }

  public Class<?> getMethodClass(Method method) throws Exception {
    String className = method.getGenericReturnType().getTypeName();
    Matcher matcher = Pattern.compile("<(.*?)>").matcher(className);
    if (matcher.find()) {
      className = matcher.group(1);
    }
    return Class.forName(className);
  }
}