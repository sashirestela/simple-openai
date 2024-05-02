package io.github.sashirestela.openai.support;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@Builder
@Getter
public class Poller<T> {

    @NonNull
    private TimeUnit timeUnit;

    @NonNull
    private Integer timeValue;
    
    @NonNull
    private UnaryOperator<T> queryMethod;
    
    @NonNull
    private Predicate<T> whileMethod;

    public T execute(T startValue) {
        T object = startValue;
        do {
            try {
                timeUnit.sleep(timeValue.longValue());
            } catch (InterruptedException e) {
                java.lang.Thread.currentThread().interrupt();
            }
            object = queryMethod.apply(object);
        } while (whileMethod.test(object));
        return object;
    }

}
