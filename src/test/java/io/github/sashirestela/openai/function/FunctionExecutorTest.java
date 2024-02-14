package io.github.sashirestela.openai.function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.domain.chat.tool.ChatFunction;
import io.github.sashirestela.openai.domain.chat.tool.ChatFunctionCall;
import io.github.sashirestela.openai.domain.chat.tool.ChatTool;
import io.github.sashirestela.openai.domain.chat.tool.ChatToolType;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class FunctionExecutorTest {

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
        var executor = new FunctionExecutor();
        var actualList = executor.getToolFunctions();
        List<ChatTool> expectedList = Collections.emptyList();
        assertEquals(expectedList, actualList);
    }

    @Test
    void shouldThrownAnExceptionWhenNullIsEnteredIntoConstructor() {
        var exception = assertThrows(SimpleUncheckedException.class,
                () -> new FunctionExecutor(null));
        var actualErrorMessage = exception.getMessage();
        var expectedErrorMessge = "No functions were entered.";
        assertEquals(expectedErrorMessge, actualErrorMessage);
    }

    @Test
    void shouldReturnListOfEnrolledFunctionsWhenEnteredAllInOne() {
        var executor = new FunctionExecutor();
        executor.enrollFunctions(functionList);
        var actualList = executor.getToolFunctions();
        var expectedList = functionList.stream().map(func -> new ChatTool(ChatToolType.FUNCTION, func))
                .collect(Collectors.toList());
        sortListFunction(actualList);
        sortListFunction(expectedList);
        assertEquals(expectedList.get(0).getFunction(), actualList.get(0).getFunction());
        assertEquals(expectedList.get(1).getFunction(), actualList.get(1).getFunction());
    }

    @Test
    void shouldReturnListOfEnrolledFunctionsWhenEnteredOneByOne() {
        var executor = new FunctionExecutor();
        executor.enrollFunction(functionList.get(0));
        executor.enrollFunction(functionList.get(1));
        var actualList = executor.getToolFunctions();
        var expectedList = functionList.stream().map(func -> new ChatTool(ChatToolType.FUNCTION, func))
                .collect(Collectors.toList());
        sortListFunction(actualList);
        sortListFunction(expectedList);
        assertEquals(expectedList.get(0).getFunction(), actualList.get(0).getFunction());
        assertEquals(expectedList.get(1).getFunction(), actualList.get(1).getFunction());
    }

    @Test
    void shouldThrownAnExceptionWhenTryingToExecuteWithBadArgument() {
        List<ChatFunctionCall> testData = Arrays.asList(null,
                new ChatFunctionCall(null, null),
                new ChatFunctionCall("", ""));
        var executor = new FunctionExecutor(functionList);
        for (var functionToCall : testData) {
            var exception = assertThrows(SimpleUncheckedException.class,
                    () -> executor.execute(functionToCall));
            var actualErrorMessage = exception.getMessage();
            var expectedErrorMessge = "No function was entered or it does not has a name.";
            assertEquals(expectedErrorMessge, actualErrorMessage);
        }
    }

    @Test
    void shouldThrownAnExceptionWhenTryingToExecuteANonEnrolledFunction() {
        var executor = new FunctionExecutor(functionList);
        var functionToCall = new ChatFunctionCall("send_email", null);
        var exception = assertThrows(SimpleUncheckedException.class,
                () -> executor.execute(functionToCall));
        var actualErrorMessage = exception.getMessage();
        var expectedErrorMessge = "The function send_email was not enrolled in the executor.";
        assertEquals(expectedErrorMessge, actualErrorMessage);
    }

    @Test
    void shouldThrowAnExceptionWhenTryingToExecuteFunctionArgumentsThatDoNotMatchItsClassStructure() {
        var executor = new FunctionExecutor(functionList);
        var functionToCall = new ChatFunctionCall("exponentiation", "{\"base\":2.0,\"exponent\":\"ten\"}");
        var exception = assertThrows(SimpleUncheckedException.class,
                () -> executor.execute(functionToCall));
        var actualErrorMessage = exception.getMessage();
        var expectedErrorMessge = "Cannot execute the function exponentiation.";
        assertEquals(expectedErrorMessge, actualErrorMessage);
    }

    @Test
    void shouldReturnACorrectValueWhenExecutingRightFunctionArguments() {
        var executor = new FunctionExecutor(functionList);
        var functionToCall = new ChatFunctionCall("exponentiation", "{\"base\":2.0,\"exponent\":10.0}");
        var actualResult = executor.execute(functionToCall);
        var expectedResult = 1024.0;
        assertEquals(actualResult, expectedResult);
    }

    private void sortListFunction(List<ChatTool> list) {
        list.sort((o1, o2) -> o1.getFunction().getName().compareTo(o2.getFunction().getName()));
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