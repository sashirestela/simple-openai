package io.github.sashirestela.openai.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.http.annotation.GET;
import io.github.sashirestela.openai.http.annotation.Path;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

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

  @Test
  void shouldReturnMapOfAllFieldsWhenObjectHasNotNullFields() {
    TestClass object = TestClass.builder()
        .integerField(10)
        .stringField("text")
        .doubleField(3.1416)
        .property(20)
        .testEnumField(TestEnum.ENUM1)
        .build();
    Map<String, Object> actualMapFields = ReflectUtil.get().getMapFields(object);
    Map<String, Object> expectedMapFields = Map.of(
        "integer", 10,
        "string", "text",
        "real", 3.1416,
        "property", 20,
        "enumerator", "enum1");
    assertEquals(expectedMapFields, actualMapFields);
  }

  @Test
  void shouldReturnMapOfNotNullFieldsWhenObjectHasNullFields() {
    TestClass object = TestClass.builder()
        .integerField(10)
        .stringField("text")
        .doubleField(3.1416)
        .property(null)
        .testEnumField(TestEnum.ENUM1)
        .build();
    Map<String, Object> actualMapFields = ReflectUtil.get().getMapFields(object);
    Map<String, Object> expectedMapFields = Map.of(
        "integer", 10,
        "string", "text",
        "real", 3.1416,
        "enumerator", "enum1");
    assertEquals(expectedMapFields, actualMapFields);
  }

  static interface TestInterface {
    @GET("/api/test/url")
    String testMethod(@Path("arg") String argument);
  }

  static enum TestEnum {
    @JsonProperty("enum1")
    ENUM1,
    @JsonProperty("enum2")
    ENUM2;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @SuperBuilder
  @Getter
  static class SuperTestClass {
    @JsonProperty("integer")
    protected Integer integerField;
    @JsonProperty("string")
    protected String stringField;
    @JsonProperty("real")
    protected Double doubleField;
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @SuperBuilder
  @Getter
  @Setter
  static class TestClass extends SuperTestClass {
    private Integer property;
    @JsonProperty("enumerator")
    private TestEnum testEnumField;
  }
}