package io.github.sashirestela.openai.support;

import io.github.sashirestela.openai.exception.PollingAbortedException;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@Builder
@Getter
public class Poller<T> {

    @NonNull
    private Duration interval;

    @NonNull
    private UnaryOperator<T> pollFunction;

    @NonNull
    private Predicate<T> continueIf;

    private Predicate<T> abortIf;

    public T execute(T startValue) {
        T object = startValue;
        do {
            try {
                java.lang.Thread.sleep(interval.toMillis());
            } catch (InterruptedException e) {
                java.lang.Thread.currentThread().interrupt();
            }
            if (abortIf != null && abortIf.test(object)) {
                throw new PollingAbortedException("Polling aborted due to abort condition.");
            }
            object = pollFunction.apply(object);
        } while (continueIf.test(object));
        return object;
    }

}
