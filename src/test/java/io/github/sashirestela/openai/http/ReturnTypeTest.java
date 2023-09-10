package io.github.sashirestela.openai.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

public class ReturnTypeTest {

  @Test
  void shouldReturnBaseClassForAMethod() throws NoSuchMethodException, SecurityException {
    var method = TestInterface.class.getMethod("asyncStreamMethod", new Class[] {});
    var returnType = new ReturnType(method);
    var actualClass = returnType.getBaseClass();
    var expectedClass = Object.class;
    assertEquals(expectedClass, actualClass);
  }

  @Test
  void shouldReturnCategoryAccordingToTheMethodType() throws NoSuchMethodException, SecurityException {
    var testData = Map.of(
        "asyncStreamMethod", ReturnType.Category.ASYNC_STREAM,
        "asyncListMethod", ReturnType.Category.ASYNC_LIST,
        "asyncObjectMethod", ReturnType.Category.ASYNC_OBJECT,
        "asyncStringMethod", ReturnType.Category.ASYNC_PLAIN_TEXT,
        "syncStreamMethod", ReturnType.Category.SYNC_STREAM,
        "syncListMethod", ReturnType.Category.SYNC_LIST,
        "syncObjectMethod", ReturnType.Category.SYNC_OBJECT,
        "syncStringMethod", ReturnType.Category.SYNC_PLAIN_TEXT);
    for (String methodName : testData.keySet()) {
      var method = TestInterface.class.getMethod(methodName, new Class[] {});
      var returnType = new ReturnType(method);
      var actualCategory = returnType.category();
      var expectedCategory = testData.get(methodName);
      assertEquals(expectedCategory, actualCategory);
    }
  }

  @Test
  void shouldReturnNullCategoryWhenMethodReturnTypeIsNotExpected() throws NoSuchMethodException, SecurityException {
    var method = TestInterface.class.getMethod("asyncSetMethod", new Class[] {});
    var returnType = new ReturnType(method);
    assertNull(returnType.category());
    method = TestInterface.class.getMethod("syncSetMethod", new Class[] {});
    returnType = new ReturnType(method);
    assertNull(returnType.category());
  }

  static interface TestInterface {

    CompletableFuture<Stream<Object>> asyncStreamMethod();

    CompletableFuture<List<Object>> asyncListMethod();

    CompletableFuture<Object> asyncObjectMethod();

    CompletableFuture<String> asyncStringMethod();

    CompletableFuture<Set<Object>> asyncSetMethod();

    Stream<Object> syncStreamMethod();

    List<Object> syncListMethod();

    Object syncObjectMethod();

    String syncStringMethod();

    Set<Object> syncSetMethod();
  }
}