package io.github.sashirestela.openai.http;

import java.lang.reflect.Method;

public class ReturnType {
  private static final String ASYNC = "java.util.concurrent.CompletableFuture";
  private static final String STREAM = "java.util.stream.Stream";
  private static final String LIST = "java.util.List";
  private static final String STRING = "java.lang.String";

  private String fullClassName;
  private String[] returnTypeArray;
  private int size;
  private int lastIndex;
  private int prevLastIndex;

  public ReturnType(String fullClassName) {
    this.fullClassName = fullClassName;
    returnTypeArray = fullClassName.split("<|>", 0);
    size = returnTypeArray.length;
    lastIndex = size - 1;
    prevLastIndex = size > 1 ? lastIndex - 1 : -1;
  }

  public ReturnType(Method method) {
    this(method.getGenericReturnType().getTypeName());
  }

  public String getFullClassName() {
    return fullClassName;
  }

  public Class<?> getBaseClass() {
    Class<?> baseClass = null;
    try {
      baseClass = Class.forName(returnTypeArray[lastIndex]);
    } catch (ClassNotFoundException e) {
      // This shouldn't happen
    }
    return baseClass;
  }

  public Category category() {
    if (isAsync()) {
      if (isStream()) {
        return Category.ASYNC_STREAM;
      } else if (isList()) {
        return Category.ASYNC_LIST;
      } else if (isObject()) {
        return Category.ASYNC_OBJECT;
      } else if (isPlainText()) {
        return Category.ASYNC_PLAIN_TEXT;
      } else {
        return null;
      }
    } else {
      if (isStream()) {
        return Category.SYNC_STREAM;
      } else if (isList()) {
        return Category.SYNC_LIST;
      } else if (isObject()) {
        return Category.SYNC_OBJECT;
      } else if (isPlainText()) {
        return Category.SYNC_PLAIN_TEXT;
      } else {
        return null;
      }
    }
  }

  public boolean isAsync() {
    return size > 1 && ASYNC.equals(returnTypeArray[0]);
  }

  public boolean isStream() {
    return size == 1 ? false : STREAM.equals(returnTypeArray[prevLastIndex]);
  }

  public boolean isList() {
    return size == 1 ? false : LIST.equals(returnTypeArray[prevLastIndex]);
  }

  public boolean isObject() {
    return !isString() && (size == 1 || (size == 2 && isAsync()));
  }

  public boolean isPlainText() {
    return isString() && (size == 1 || (size == 2 && isAsync()));
  }

  private boolean isString() {
    return STRING.equals(returnTypeArray[lastIndex]);
  }

  public static enum Category {
    ASYNC_STREAM,
    ASYNC_LIST,
    ASYNC_OBJECT,
    ASYNC_PLAIN_TEXT,
    SYNC_STREAM,
    SYNC_LIST,
    SYNC_OBJECT,
    SYNC_PLAIN_TEXT;
  }
}