package io.github.sashirestela.openai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.domain.chat.ChatFunction;
import io.github.sashirestela.openai.domain.chat.ChatFunctionCall;

public class SimpleFunctionExecutorTest {

  private List<ChatFunction> functionList = Arrays.asList(
      ChatFunction.builder().name("convert_to_celsius")
          .functionToExecute(ConvertToCelsiusArgs.class, conv -> (conv.fahrenheit - 32) * 5 / 9).build(),
      ChatFunction.builder().name("exponentiation")
          .functionToExecute(ExponentArgs.class, exp -> Math.pow(exp.base, exp.exponent)).build());

  @Test
  void shouldReturnEmptyListWhenObjectWasNotInitialized() {
    SimpleFunctionExecutor executor = new SimpleFunctionExecutor();
    List<ChatFunction> actualList = executor.getFunctions();
    List<ChatFunction> expectedList = Collections.emptyList();
    assertEquals(expectedList, actualList);
  }

  @Test
  void shouldThrownAnExceptionWhenNullIsEnteredIntoConstructor() {
    SimpleUncheckedException exception = assertThrows(SimpleUncheckedException.class,
        () -> new SimpleFunctionExecutor(null));
    String actualErrorMessage = exception.getMessage();
    String expectedErrorMessge = "No functions were entered.";
    assertEquals(expectedErrorMessge, actualErrorMessage);
  }

  @Test
  void shouldReturnListOfEnrolledFunctionsWhenEnteredAllInOne() {
    SimpleFunctionExecutor executor = new SimpleFunctionExecutor();
    executor.enrollFunctions(functionList);
    List<ChatFunction> actualList = executor.getFunctions();
    List<ChatFunction> expectedList = functionList;
    sortListFunction(actualList);
    sortListFunction(expectedList);
    assertEquals(expectedList, actualList);
  }

  @Test
  void shouldReturnListOfEnrolledFunctionsWhenEnteredOneByOne() {
    SimpleFunctionExecutor executor = new SimpleFunctionExecutor();
    executor.enrollFunction(functionList.get(0));
    executor.enrollFunction(functionList.get(1));
    List<ChatFunction> actualList = executor.getFunctions();
    List<ChatFunction> expectedList = functionList;
    sortListFunction(actualList);
    sortListFunction(expectedList);
    assertEquals(expectedList, actualList);
  }

  @Test
  void shouldThrownAnExceptionWhenTryingToExecuteWithBadArgument() {
    List<ChatFunctionCall> testData = Arrays.asList(null,
        new ChatFunctionCall(null, null),
        new ChatFunctionCall("", ""));
    SimpleFunctionExecutor executor = new SimpleFunctionExecutor(functionList);
    for (ChatFunctionCall functionToCall : testData) {
      SimpleUncheckedException exception = assertThrows(SimpleUncheckedException.class, () -> executor.execute(functionToCall));
      String actualErrorMessage = exception.getMessage();
      String expectedErrorMessge = "No function was entered or it does not has a name.";
      assertEquals(expectedErrorMessge, actualErrorMessage);
    }
  }

  @Test
  void shouldThrownAnExceptionWhenTryingToExecuteANonEnrolledFunction() {
    SimpleFunctionExecutor executor = new SimpleFunctionExecutor(functionList);
    ChatFunctionCall functionToCall = new ChatFunctionCall("send_email", null);
    SimpleUncheckedException exception = assertThrows(SimpleUncheckedException.class, () -> executor.execute(functionToCall));
    String actualErrorMessage = exception.getMessage();
    String expectedErrorMessge = "The function send_email was not enrolled in the executor.";
    assertEquals(expectedErrorMessge, actualErrorMessage);
  }

  @Test
  void shouldThrowAnExceptionWhenTryingToExecuteFunctionArgumentsThatDoNotMatchItsClassStructure() {
    SimpleFunctionExecutor executor = new SimpleFunctionExecutor(functionList);
    ChatFunctionCall functionToCall = new ChatFunctionCall("exponentiation", "{\"base\":2.0,\"power\":10.0}");
    SimpleUncheckedException exception = assertThrows(SimpleUncheckedException.class, () -> executor.execute(functionToCall));
    String actualErrorMessage = exception.getMessage();
    String expectedErrorMessge = "Cannot execute the function exponentiation.";
    assertEquals(expectedErrorMessge, actualErrorMessage);
  }

  @Test
  void shouldReturnACorrectValueWhenExecutingRightFunctionArguments() {
    SimpleFunctionExecutor executor = new SimpleFunctionExecutor(functionList);
    ChatFunctionCall functionToCall = new ChatFunctionCall("exponentiation", "{\"base\":2.0,\"exponent\":10.0}");
    Double actualResult = executor.execute(functionToCall);
    Double expectedResult = 1024.0;
    assertEquals(actualResult, expectedResult);
  }

  private void sortListFunction(List<ChatFunction> list) {
    list.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
  }

  static class ConvertToCelsiusArgs {
    public double fahrenheit;
  }

  static class ExponentArgs {
    public double base;
    public double exponent;
  }
}
