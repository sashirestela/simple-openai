package io.github.sashirestela.openai.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.domain.chat.ChatFunction;
import io.github.sashirestela.openai.domain.chat.ChatFunctionCall;

public class SimpleFunctionExecutorTest {

  private List<ChatFunction> functionList = Arrays.asList(
      ChatFunction.builder()
          .name("convert_to_celsius")
          .description("Convert Fahrenheit to Celsius degrees")
          .functionalClass(ConvertToCelsius.class)
          .build(),
      ChatFunction.builder()
          .name("exponentiation")
          .description("Multiply a base number a number n times")
          .functionalClass(MathPower.class)
          .build());

  @Test
  void shouldReturnEmptyListWhenObjectWasNotInitialized() {
    FunctionExecutor executor = new FunctionExecutor();
    List<ChatFunction> actualList = executor.getFunctions();
    List<ChatFunction> expectedList = Collections.emptyList();
    assertEquals(expectedList, actualList);
  }

  @Test
  void shouldThrownAnExceptionWhenNullIsEnteredIntoConstructor() {
    SimpleUncheckedException exception = assertThrows(SimpleUncheckedException.class,
        () -> new FunctionExecutor(null));
    String actualErrorMessage = exception.getMessage();
    String expectedErrorMessge = "No functions were entered.";
    assertEquals(expectedErrorMessge, actualErrorMessage);
  }

  @Test
  void shouldReturnListOfEnrolledFunctionsWhenEnteredAllInOne() {
    FunctionExecutor executor = new FunctionExecutor();
    executor.enrollFunctions(functionList);
    List<ChatFunction> actualList = executor.getFunctions();
    List<ChatFunction> expectedList = functionList;
    sortListFunction(actualList);
    sortListFunction(expectedList);
    assertEquals(expectedList, actualList);
  }

  @Test
  void shouldReturnListOfEnrolledFunctionsWhenEnteredOneByOne() {
    FunctionExecutor executor = new FunctionExecutor();
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
    FunctionExecutor executor = new FunctionExecutor(functionList);
    for (ChatFunctionCall functionToCall : testData) {
      SimpleUncheckedException exception = assertThrows(SimpleUncheckedException.class,
          () -> executor.execute(functionToCall));
      String actualErrorMessage = exception.getMessage();
      String expectedErrorMessge = "No function was entered or it does not has a name.";
      assertEquals(expectedErrorMessge, actualErrorMessage);
    }
  }

  @Test
  void shouldThrownAnExceptionWhenTryingToExecuteANonEnrolledFunction() {
    FunctionExecutor executor = new FunctionExecutor(functionList);
    ChatFunctionCall functionToCall = new ChatFunctionCall("send_email", null);
    SimpleUncheckedException exception = assertThrows(SimpleUncheckedException.class,
        () -> executor.execute(functionToCall));
    String actualErrorMessage = exception.getMessage();
    String expectedErrorMessge = "The function send_email was not enrolled in the executor.";
    assertEquals(expectedErrorMessge, actualErrorMessage);
  }

  @Test
  void shouldThrowAnExceptionWhenTryingToExecuteFunctionArgumentsThatDoNotMatchItsClassStructure() {
    FunctionExecutor executor = new FunctionExecutor(functionList);
    ChatFunctionCall functionToCall = new ChatFunctionCall("exponentiation", "{\"base\":2.0,\"power\":10.0}");
    SimpleUncheckedException exception = assertThrows(SimpleUncheckedException.class,
        () -> executor.execute(functionToCall));
    String actualErrorMessage = exception.getMessage();
    String expectedErrorMessge = "Cannot execute the function exponentiation.";
    assertEquals(expectedErrorMessge, actualErrorMessage);
  }

  @Test
  void shouldReturnACorrectValueWhenExecutingRightFunctionArguments() {
    FunctionExecutor executor = new FunctionExecutor(functionList);
    ChatFunctionCall functionToCall = new ChatFunctionCall("exponentiation", "{\"base\":2.0,\"exponent\":10.0}");
    Double actualResult = executor.execute(functionToCall);
    Double expectedResult = 1024.0;
    assertEquals(actualResult, expectedResult);
  }

  private void sortListFunction(List<ChatFunction> list) {
    list.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
  }

  static class ConvertToCelsius implements Functional {
    public double fahrenheit;

    @Override
    public Object execute() {
      return (fahrenheit - 32) * 5 / 9;
    }
  }

  static class MathPower implements Functional {
    public double base;
    public double exponent;

    @Override
    public Object execute() {
      return Math.pow(base, exponent);
    }
  }
}