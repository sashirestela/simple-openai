package io.github.sashirestela.openai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

class SimpleUncheckedExceptionTest {

    @Test
    void shouldReplaceParametersInErrorMessageWhenAnExceptionIsCreated() {
        var exception = new SimpleUncheckedException("{0}, {1}", "parameter1", "parameter2", null);
        var actualExceptionMessage = exception.getMessage();
        var expectedExceptionMessage = "parameter1, parameter2";
        assertEquals(expectedExceptionMessage, actualExceptionMessage);
    }

    @Test
    void shouldSetCauseInErrorWhenItIsPassedAsLastArgumentAtExceptionCreation() {
        var exception = new SimpleUncheckedException("Message", null, new Exception());
        assertNotNull(exception.getCause());
    }
}
