package io.github.sashirestela.openai;

import java.text.MessageFormat;
import java.util.Arrays;

public class SimpleUncheckedException extends RuntimeException {

    public SimpleUncheckedException(String message) {
        super(message);
    }

    public SimpleUncheckedException(String message, Object... parameters) {
        super(MessageFormat.format(message, Arrays.copyOfRange(parameters, 0, parameters.length - 1)),
                (Throwable) parameters[parameters.length - 1]);
    }

}
