package io.github.sashirestela.openai.exception;

public class PollingAbortedException extends SimpleOpenAIException {

    public PollingAbortedException(String message) {
        super(message);
    }

}
