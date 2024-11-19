package io.github.sashirestela.openai.common.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.sashirestela.cleverclient.support.Configurator;
import io.github.sashirestela.openai.SimpleUncheckedException;
import io.github.sashirestela.openai.common.tool.Tool;
import io.github.sashirestela.openai.common.tool.ToolChoice;
import io.github.sashirestela.openai.common.tool.ToolChoiceOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FunctionExecutorTest {

    private List<FunctionDef> functionList = Arrays.asList(
            FunctionDef.builder()
                    .name("convert_to_celsius")
                    .description("Convert Fahrenheit to Celsius degrees")
                    .functionalClass(ConvertToCelsius.class)
                    .build(),
            FunctionDef.builder()
                    .name("exponentiation")
                    .description("Multiply a base number a number n times")
                    .functionalClass(MathPower.class)
                    .build(),
            FunctionDef.builder()
                    .name("get_random_number")
                    .description("Get random number")
                    .functionalClass(RandomNumber.class)
                    .build());

    @BeforeAll
    static void setup() {
        Configurator.builder().objectMapper(new ObjectMapper()).build();
    }

    @Test
    void shouldReturnEmptyListWhenObjectWasNotInitialized() {
        var executor = new FunctionExecutor();
        var actualList = executor.getToolFunctions();
        List<Tool> expectedList = Collections.emptyList();
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
        var expectedList = functionList.stream()
                .map(func -> Tool.function(func))
                .collect(Collectors.toList());
        sortListFunction(actualList);
        sortListFunction(expectedList);
        assertEquals(expectedList.get(0).getFunction().toString(), actualList.get(0).getFunction().toString());
        assertEquals(expectedList.get(1).getFunction().toString(), actualList.get(1).getFunction().toString());
    }

    @Test
    void shouldReturnListOfEnrolledFunctionsWhenEnteredOneByOne() {
        var executor = new FunctionExecutor();
        executor.enrollFunction(functionList.get(0));
        executor.enrollFunction(functionList.get(1));
        var actualList = executor.getToolFunctions();
        var expectedList = functionList.stream()
                .map(func -> Tool.function(func))
                .collect(Collectors.toList());
        sortListFunction(actualList);
        sortListFunction(expectedList);
        assertEquals(expectedList.get(0).getFunction().toString(), actualList.get(0).getFunction().toString());
        assertEquals(expectedList.get(1).getFunction().toString(), actualList.get(1).getFunction().toString());
    }

    @Test
    void shouldThrownAnExceptionWhenTryingToExecuteWithBadArgument() {
        List<FunctionCall> testData = Arrays.asList(null,
                new FunctionCall(null, null),
                new FunctionCall("", ""));
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
        var functionToCall = new FunctionCall("send_email", null);
        var exception = assertThrows(SimpleUncheckedException.class,
                () -> executor.execute(functionToCall));
        var actualErrorMessage = exception.getMessage();
        var expectedErrorMessge = "The function send_email was not enrolled in the executor.";
        assertEquals(expectedErrorMessge, actualErrorMessage);
    }

    @Test
    void shouldThrowAnExceptionWhenTryingToExecuteFunctionArgumentsThatDoNotMatchItsClassStructure() {
        var executor = new FunctionExecutor(functionList);
        var functionToCall = new FunctionCall("exponentiation", "{\"base\":2.0,\"exponent\":\"ten\"}");
        var exception = assertThrows(SimpleUncheckedException.class,
                () -> executor.execute(functionToCall));
        var actualErrorMessage = exception.getMessage();
        var expectedErrorMessge = "Cannot execute the function exponentiation.";
        assertEquals(expectedErrorMessge, actualErrorMessage);
    }

    @Test
    void shouldReturnACorrectValueWhenExecutingRightFunctionArguments() {
        var executor = new FunctionExecutor(functionList);
        var functionToCall = new FunctionCall("exponentiation", "{\"base\":2.0,\"exponent\":10.0}");
        var actualResult = executor.execute(functionToCall);
        var expectedResult = 1024.0;
        assertEquals(actualResult, expectedResult);
    }

    @Test
    void shouldReturnListOfFunctionsWhenToolChoiceIsPassed() {
        Object[][] testData = {
                { ToolChoiceOption.NONE, 0 },
                { ToolChoiceOption.AUTO, 3, "convert_to_celsius", "exponentiation", "get_random_number" },
                { ToolChoiceOption.REQUIRED, 3, "convert_to_celsius", "exponentiation", "get_random_number" },
                { ToolChoice.function("exponentiation"), 1, "exponentiation" }
        };
        var executor = new FunctionExecutor(functionList);
        for (var data : testData) {
            var actualTools = executor.getToolFunctions(data[0]);
            var actualSize = actualTools.size();
            assertEquals(data[1], actualSize);
            if (actualSize > 0) {
                sortListFunction(actualTools);
                for (int i = 0, idx = 2; i < actualSize; i++, idx++) {
                    assertEquals(data[idx], actualTools.get(i).getFunction().getName());
                }
            }
        }
    }

    @Test
    void shouldThownExceptionWhenTryingToGetToolFunctionsWithWrongArgument() {
        Object[][] testData = {
                { ToolChoice.function("send_email"), "The function send_email was not enrolled in the executor." },
                { "CalculateWeather", "The object CalculateWeather is of an unexpected type." }
        };
        var executor = new FunctionExecutor(functionList);
        for (var data : testData) {
            var exception = assertThrows(SimpleUncheckedException.class, () -> executor.getToolFunctions(data[0]));
            assertEquals(exception.getMessage(), data[1]);
        }
    }

    @Test
    void shouldHandleBlankArgumentsStringGracefully() {
        var executor = new FunctionExecutor(functionList);
        var functionToCall = new FunctionCall("get_random_number", "");
        var actualResult = executor.execute(functionToCall);
        var expectedResult = 42;
        assertEquals(actualResult, expectedResult);
    }

    private void sortListFunction(List<Tool> list) {
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

    static class RandomNumber implements Functional {

        @Override
        public Object execute() {
            return 42;
        }

    }

}
