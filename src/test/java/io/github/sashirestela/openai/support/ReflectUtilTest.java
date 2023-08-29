package io.github.sashirestela.openai.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.http.annotation.GET;
import io.github.sashirestela.openai.http.annotation.Path;
import lombok.Getter;
import lombok.Setter;

public class ReflectUtilTest {

  @Test
  void shouldExecuteHandlerWhenInterfaceMethodIsCalled() {
    TestInterface test = ReflectUtil.get().createProxy(TestInterface.class, new InvocationHandler() {
      @Override
      public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return "Text from handler.";
      }
    });
    String actualValue = test.testMethod("example");
    String expectedValue = "Text from handler.";
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shouldExecuteSetMethodWhenItExistsInClass() {
    TestClass object = new TestClass();
    ReflectUtil.get().executeSetMethod(TestClass.class, "setProperty", new Class<?>[] { Integer.class }, object, 7);
    Integer actualValue = object.getProperty();
    Integer expectedValue = 7;
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shouldReturnExceptionWhenExecutingSetMethodThatDoesNotExist() {
    TestClass object = new TestClass();
    assertThrows(SimpleUncheckedException.class,
        () -> ReflectUtil.get().executeSetMethod(TestClass.class, "setAttribute",
            new Class<?>[] { Integer.class }, object, 7));
  }

  @Test
  void shouldReturnExceptionWhenExecutingSetMethodWithInvalidArgument() {
    TestClass object = new TestClass();
    assertThrows(SimpleUncheckedException.class,
        () -> ReflectUtil.get().executeSetMethod(TestClass.class, "setProperty",
            new Class<?>[] { Integer.class }, object, "seven"));
  }

  static interface TestInterface {

    @GET("/api/test/url")
    String testMethod(@Path("arg") String argument);

    Stream<Integer> streamMethod(@Path("argInt") Integer argInt, @Path("argStr") String argStr, boolean argBol);

    CompletableFuture<Stream<Double>> futureMethod();

    CompletableFuture<TestClass> methodObject();

    CompletableFuture<List<TestClass>> methodList();

    CompletableFuture<Stream<TestClass>> methodStream();

    CompletableFuture<Set<TestClass>> methodUnknown();
  }

  @Getter
  @Setter
  static class TestClass {

    private Integer property;

  }

}