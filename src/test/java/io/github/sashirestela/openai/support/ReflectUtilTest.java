package io.github.sashirestela.openai.support;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.http.ResponseType;
import io.github.sashirestela.openai.http.annotation.Body;
import io.github.sashirestela.openai.http.annotation.GET;
import io.github.sashirestela.openai.http.annotation.POST;
import io.github.sashirestela.openai.http.annotation.PUT;
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
  void shouldReturnAnnotationInMethodWhenItExistsInList() {
    Method method = getMethod(TestInterface.class, "testMethod");
    Class<?> actualAnnotClass = ReflectUtil.get().getFirstAnnotTypeInList(method, Arrays.asList(GET.class, POST.class));
    Class<?> expectedAnnotClass = GET.class;
    assertEquals(expectedAnnotClass, actualAnnotClass);
  }

  @Test
  void shouldReturnNullWhenAnnotationInMethodDoesNotExistsInList() {
    Method method = getMethod(TestInterface.class, "testMethod");
    Class<?> annotClass = ReflectUtil.get().getFirstAnnotTypeInList(method, Arrays.asList(POST.class, PUT.class, null));
    assertNull(annotClass);
  }

  @Test
  void shouldReturnAnnotAttribValueWhenItExists() {
    Method method = getMethod(TestInterface.class, "testMethod");
    String actualAnnotAttribValue = (String) ReflectUtil.get().getAnnotAttribValue(method, GET.class,
        Constant.DEF_ANNOT_ATTRIB);
    String expectedAnnotAttribValue = "/api/test/url";
    assertEquals(expectedAnnotAttribValue, actualAnnotAttribValue);
  }

  @Test
  void shouldReturnNullAttribValueWhenAnnotationIsNotInTheMethod() {
    Method method = getMethod(TestInterface.class, "testMethod");
    String annotAttribValue = (String) ReflectUtil.get().getAnnotAttribValue(method, PUT.class,
        Constant.DEF_ANNOT_ATTRIB);
    assertNull(annotAttribValue);
  }

  @Test
  void shouldReturnExceptionWhenAnnotAttribDoesNotExistInAnnotation() {
    Method method = getMethod(TestInterface.class, "testMethod");
    assertThrows(SimpleUncheckedException.class,
        () -> ReflectUtil.get().getAnnotAttribValue(method, GET.class, "otherAttribute"));
  }

  @Test
  void shouldReturnMethodElementWhenAnnotationIsInTheMethod() {
    Method method = getMethod(TestInterface.class, "testMethod");
    MethodElement actualElement = ReflectUtil.get().getMethodElementAnnotatedWith(method, new Object[] { "example" },
        Path.class);
    MethodElement expectedElement = new MethodElement(method.getParameters()[0], "arg", "example");
    assertEquals(expectedElement, actualElement);
  }

  @Test
  void shouldReturnNullMethodElementWhenAnnotationIsNotInTheMethod() {
    Method method = getMethod(TestInterface.class, "testMethod");
    MethodElement element = ReflectUtil.get().getMethodElementAnnotatedWith(method, new Object[] { "example" },
        Body.class);
    assertNull(element);
  }

  @Test
  void shouldReturnMethodElementsWhenAnnotationsAreInTheMethod() {
    Method method = getMethod(TestInterface.class, "streamMethod");
    List<MethodElement> actualElements = ReflectUtil.get().getMethodElementsAnnotatedWith(method,
        new Object[] { 10, "Text", false }, Path.class);
    List<MethodElement> expectedElements = Arrays.asList(
        new MethodElement(method.getParameters()[0], "argInt", 10),
        new MethodElement(method.getParameters()[1], "argStr", "Text"));
    assertEquals(expectedElements, actualElements);
  }

  @Test
  void shouldReturnEmptyMethodElementsWhenAnnotationsAreNotInTheMethod() {
    Method method = getMethod(TestInterface.class, "streamMethod");
    List<MethodElement> elements = ReflectUtil.get().getMethodElementsAnnotatedWith(method,
        new Object[] { 10, "Text", false }, Body.class);
    int actualSize = elements.size();
    int expectedSize = 0;
    assertEquals(expectedSize, actualSize);
  }

  @Test
  void shouldReturnTheMethodClassWhenItIsNotGeneric() {
    Method method = getMethod(TestInterface.class, "testMethod");
    Class<?> actualClass = ReflectUtil.get().getBaseClass(method);
    Class<?> expectedClass = String.class;
    assertEquals(expectedClass, actualClass);
  }

  @Test
  void shouldReturnTheInternalMethodClassWhenItIsGenericAtFirstLevel() {
    Method method = getMethod(TestInterface.class, "streamMethod");
    Class<?> actualClass = ReflectUtil.get().getBaseClass(method);
    Class<?> expectedClass = Integer.class;
    assertEquals(expectedClass, actualClass);
  }

  @Test
  void shouldReturnTheInternalMethodClassWhenItIsGenericAtSecondLevel() {
    Method method = getMethod(TestInterface.class, "futureMethod");
    Class<?> actualClass = ReflectUtil.get().getBaseClass(method);
    Class<?> expectedClass = Double.class;
    assertEquals(expectedClass, actualClass);
  }

  @Test
  void shouldReturnTheRightResponseTypeWhenAMethodIsRequested() {
    Object[][] testData = { { "methodObject", ResponseType.OBJECT }, { "methodList", ResponseType.LIST },
        { "methodStream", ResponseType.STREAM }, { "methodUnknown", ResponseType.UNKNOWN } };
    for (Object[] data : testData) {
      Method method = this.getMethod(TestInterface.class, (String) data[0]);
      ResponseType actualResponseType = ReflectUtil.get().getResponseType(method);
      ResponseType expectedResponseType = (ResponseType) data[1];
      assertEquals(expectedResponseType, actualResponseType);
    }
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
    assertThrows(SimpleUncheckedException.class, () -> ReflectUtil.get().executeSetMethod(TestClass.class, "setAttribute",
        new Class<?>[] { Integer.class }, object, 7));
  }

  @Test
  void shouldReturnExceptionWhenExecutingSetMethodWithInvalidArgument() {
    TestClass object = new TestClass();
    assertThrows(SimpleUncheckedException.class, () -> ReflectUtil.get().executeSetMethod(TestClass.class, "setProperty",
        new Class<?>[] { Integer.class }, object, "seven"));
  }

  private Method getMethod(Class<?> clazz, String methodName) {
    return Stream.of(clazz.getDeclaredMethods())
        .filter(m -> m.getName().equals(methodName))
        .findFirst()
        .orElse(null);
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