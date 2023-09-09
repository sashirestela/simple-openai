package io.github.sashirestela.openai.support;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.sashirestela.openai.SimpleUncheckedException;

public class ReflectUtil {
  private static ReflectUtil reflection = null;

  private ReflectUtil() {
  }

  public static ReflectUtil get() {
    if (reflection == null) {
      reflection = new ReflectUtil();
    }
    return reflection;
  }

  @SuppressWarnings("unchecked")
  public <T> T createProxy(Class<T> interfaceClass, InvocationHandler handler) {
    T proxy = (T) Proxy.newProxyInstance(
        interfaceClass.getClassLoader(),
        new Class<?>[] { interfaceClass },
        handler);
    return proxy;
  }

  public Map<String, Object> getMapFields(Object object) {
    final String GET_PREFIX = "get";
    Map<String, Object> structure = new HashMap<>();
    Class<?> clazz = object.getClass();
    Field[] fields = getFields(clazz);
    for (Field field : fields) {
      String fieldName = field.getName();
      String methodName = GET_PREFIX + CommonUtil.get().capitalize(fieldName);
      Object fieldValue;
      try {
        Method getMethod = clazz.getMethod(methodName, new Class<?>[] {});
        fieldValue = getMethod.invoke(object);
      } catch (Exception e) {
        throw new SimpleUncheckedException("Cannot find the method {0} in the class {1}.", methodName,
            clazz.getSimpleName(), e);
      }
      if (fieldValue != null) {
        structure.put(getFieldName(field), getFieldValue(fieldValue));
      }
    }
    return structure;
  }

  private Field[] getFields(Class<?> clazz) {
    final String CLASS_OBJECT = "Object";
    Field[] fields = new Field[] {};
    Class<?> nextClazz = clazz;
    while (!nextClazz.getSimpleName().equals(CLASS_OBJECT)) {
      fields = CommonUtil.get().concatArrays(fields, nextClazz.getDeclaredFields());
      nextClazz = nextClazz.getSuperclass();
    }
    return fields;
  }

  private String getFieldName(Field field) {
    final String JSON_PROPERTY_METHOD_NAME = "value";
    String fieldName = field.getName();
    if (field.isAnnotationPresent(JsonProperty.class)) {
      fieldName = (String) getAnnotAttribValue(field, JsonProperty.class, JSON_PROPERTY_METHOD_NAME);
    }
    return fieldName;
  }

  private Object getFieldValue(Object fieldValue) {
    if (fieldValue.getClass().isEnum()) {
      String enumConstantName = ((Enum<?>) fieldValue).name();
      try {
        fieldValue = fieldValue.getClass().getField(enumConstantName).getAnnotation(JsonProperty.class).value();
      } catch (NoSuchFieldException | SecurityException e) {
        throw new SimpleUncheckedException("Cannot find the enum constant {0}.", enumConstantName, e);
      }
    }
    return fieldValue;
  }

  private Object getAnnotAttribValue(AnnotatedElement element, Class<? extends Annotation> annotType,
      String annotAttribName) {
    Object value = null;
    Annotation annotation = element.getAnnotation(annotType);
    if (annotation != null) {
      Method annotAttrib = null;
      try {
        annotAttrib = annotType.getMethod(annotAttribName);
      } catch (NoSuchMethodException | SecurityException e) {
        throw new SimpleUncheckedException("Cannot find the method {0} in the annotation {1}.", annotAttribName,
            annotType.getName(), e);
      }
      try {
        value = annotAttrib.invoke(annotation, (Object[]) null);
      } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
        throw new SimpleUncheckedException("Cannot execute the method {0} in the annotation {1}.", annotAttribName,
            annotType.getName(), e);
      }
    }
    return value;
  }
}