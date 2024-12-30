package io.github.sashirestela.openai;

import io.github.sashirestela.openai.exception.SimpleOpenAIException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SimpleOpenAIExceptionTest {

    @Test
    void shouldSetMessageWhenItIsPassedAsTheOnlyOneArgument() {
        var exception = new SimpleOpenAIException("Message");
        assertNotNull(exception.getMessage());
    }

    @Test
    void shouldReplaceParametersInErrorMessageWhenAnExceptionIsCreated() {
        var exception = new SimpleOpenAIException("{0}, {1}", "parameter1", "parameter2", null);
        var actualExceptionMessage = exception.getMessage();
        var expectedExceptionMessage = "parameter1, parameter2";
        assertEquals(expectedExceptionMessage, actualExceptionMessage);
    }

    @Test
    void shouldSetCauseInErrorWhenItIsPassedAsLastArgumentAtExceptionCreation() {
        var exception = new SimpleOpenAIException("Message", null, new Exception());
        assertNotNull(exception.getCause());
    }

}
