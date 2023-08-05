package io.github.sashirestela.openai.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.sashirestela.openai.exception.UncheckedException;

public class ReflectUtil {
  private static ReflectUtil reflection = null;

  private ReflectUtil() {
  }

  public static ReflectUtil one() {
    if (reflection == null) {
      reflection = new ReflectUtil();
    }
    return reflection;
  }

  public Class<? extends Annotation> getFirstAnnotationTypeInList(Method method,
      List<Class<? extends Annotation>> listAnnotationType) {
    Class<? extends Annotation> annotationType = listAnnotationType
        .stream()
        .filter(annotType -> annotType != null && method.isAnnotationPresent(annotType))
        .findFirst()
        .orElse(null);
    return annotationType;
  }

  public Object getAnnotationAttribute(AnnotatedElement element, Class<? extends Annotation> annotationType,
      String annotationMethodName) {
    Object value = null;
    Annotation annotation = element.getAnnotation(annotationType);
    if (annotation != null) {
      Method annotationMethod = null;
      try {
        annotationMethod = annotationType.getMethod(annotationMethodName);
      } catch (NoSuchMethodException | SecurityException e) {
        throw new UncheckedException("Cannot found the method {0} in the annotation {1}.", annotationMethodName,
            annotationType.getName(), e);
      }
      try {
        value = annotationMethod.invoke(annotation, (Object[]) null);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new UncheckedException("Cannot execute the method {0} in the annotation {1}.", annotationMethodName,
            annotationType.getName(), e);
      }
    }
    return value;
  }

  public Pair<Parameter, Object> getArgumentAnnotatedWith(Method method, Object[] arguments,
      Class<? extends Annotation> annotationType) {
    Pair<Parameter, Object> pairParameterArgument = null;
    int i = 0;
    Parameter[] parameters = method.getParameters();
    for (Parameter parameter : parameters) {
      if (parameter.isAnnotationPresent(annotationType)) {
        pairParameterArgument = new Pair<>(parameter, arguments[i]);
        break;
      }
      i++;
    }
    return pairParameterArgument;
  }

  public Class<?> getReturnClassOf(Method method) {
    String className = method.getGenericReturnType().getTypeName();
    Matcher matcher = Pattern.compile("<(.*?)>").matcher(className);
    className = matcher.find() ? matcher.group(1) : className;
    Class<?> methodReturnClass = null;
    try {
      methodReturnClass = Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new UncheckedException("Cannot found the return class {0} for the method {1}.", className,
          method.getName(), e);
    }
    return methodReturnClass;
  }
}