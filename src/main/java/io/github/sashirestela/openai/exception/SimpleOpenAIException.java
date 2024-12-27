package io.github.sashirestela.openai.exception;

import java.text.MessageFormat;
import java.util.Arrays;

public class SimpleOpenAIException extends RuntimeException {

    public SimpleOpenAIException(String message) {
        super(message);
    }

    public SimpleOpenAIException(String message, Object... parameters) {
        super(MessageFormat.format(message, Arrays.copyOfRange(parameters, 0, parameters.length - 1)),
                (Throwable) parameters[parameters.length - 1]);
    }

}
