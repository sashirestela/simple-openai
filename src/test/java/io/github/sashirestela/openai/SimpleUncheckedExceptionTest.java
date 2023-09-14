package io.github.sashirestela.openai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class SimpleUncheckedExceptionTest {

  @Test
  void shouldReplaceParametersInErrorMessageWhenAnExceptionIsCreated() {
    SimpleUncheckedException exception = new SimpleUncheckedException("{0}, {1}", "parameter1", "parameter2", null);
    String actualExceptionMessage = exception.getMessage();
    String expectedExceptionMessage = "parameter1, parameter2";
    assertEquals(expectedExceptionMessage, actualExceptionMessage);
  }

  @Test
  void shouldSetCauseInErrorWhenItIsPassedAsLastArgumentAtExceptionCreation() {
    SimpleUncheckedException exception = new SimpleUncheckedException("Message", null, new Exception());
    assertNotNull(exception.getCause());
  }
}
