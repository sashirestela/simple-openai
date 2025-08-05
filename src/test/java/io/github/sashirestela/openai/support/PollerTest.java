package io.github.sashirestela.openai.support;

import io.github.sashirestela.openai.exception.PollingAbortedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PollerTest {

    private AtomicInteger counter;

    @BeforeEach
    void setUp() {
        counter = new AtomicInteger();
    }

    @Test
    void executeStopsWhenContinueIfReturnsFalse() {
        Poller<Integer> poller = Poller.<Integer>builder()
                .interval(Duration.ofMillis(10))
                .pollFunction(i -> i + 1)
                .continueIf(i -> i < 3)
                .build();

        Integer result = poller.execute(0);

        assertEquals(3, result);
    }

    @Test
    void executeAppliesPollFunctionCorrectly() {
        Poller<String> poller = Poller.<String>builder()
                .interval(Duration.ofMillis(10))
                .pollFunction(s -> s + "x")
                .continueIf(s -> s.length() < 3)
                .build();

        String result = poller.execute("a");

        assertEquals("axx", result);
    }

    @Test
    void executeRespectsInterval() {
        long startTime = System.currentTimeMillis();

        Poller<Integer> poller = Poller.<Integer>builder()
                .interval(Duration.ofMillis(50))
                .pollFunction(i -> i + 1)
                .continueIf(i -> i < 2)
                .build();

        poller.execute(0);

        long elapsed = System.currentTimeMillis() - startTime;
        assertTrue(elapsed >= 100);
    }

    @Test
    void executeWithoutAbortCondition() {
        Poller<Integer> poller = Poller.<Integer>builder()
                .interval(Duration.ofMillis(10))
                .pollFunction(i -> i * 2)
                .continueIf(i -> i < 16)
                .build();

        Integer result = poller.execute(1);

        assertEquals(16, result);
    }

    @Test
    void executeThrowsPollingAbortedExceptionWhenAbortConditionMet() {
        Poller<Integer> poller = Poller.<Integer>builder()
                .interval(Duration.ofMillis(10))
                .pollFunction(i -> i + 1)
                .continueIf(i -> i < 10)
                .abortIf(i -> i == 2)
                .build();

        PollingAbortedException exception = assertThrows(
                PollingAbortedException.class,
                () -> poller.execute(0));

        assertEquals("Polling aborted due to abort condition.", exception.getMessage());
    }

    @Test
    void executeAbortConditionCheckedBeforePollFunction() {
        Poller<Integer> poller = Poller.<Integer>builder()
                .interval(Duration.ofMillis(10))
                .pollFunction(i -> {
                    counter.incrementAndGet();
                    return i + 1;
                })
                .continueIf(i -> i < 10)
                .abortIf(i -> i == 0)
                .build();

        assertThrows(PollingAbortedException.class, () -> poller.execute(0));
        assertEquals(0, counter.get());
    }

    @Test
    void executeWithNullAbortCondition() {
        Poller<Integer> poller = Poller.<Integer>builder()
                .interval(Duration.ofMillis(10))
                .pollFunction(i -> i + 1)
                .continueIf(i -> i < 3)
                .abortIf(null)
                .build();

        Integer result = poller.execute(0);

        assertEquals(3, result);
    }

    @Test
    void executeCountsPollFunctionInvocations() {
        Poller<Integer> poller = Poller.<Integer>builder()
                .interval(Duration.ofMillis(10))
                .pollFunction(i -> {
                    counter.incrementAndGet();
                    return i + 1;
                })
                .continueIf(i -> i < 5)
                .build();

        poller.execute(0);

        assertEquals(5, counter.get());
    }

    @Test
    void executeWithImmediateStop() {
        Poller<Integer> poller = Poller.<Integer>builder()
                .interval(Duration.ofMillis(10))
                .pollFunction(i -> i)
                .continueIf(i -> false)
                .build();

        Integer result = poller.execute(42);

        assertEquals(42, result);
    }

    @Test
    void executeAbortConditionCheckedMultipleTimes() {
        Poller<Integer> poller = Poller.<Integer>builder()
                .interval(Duration.ofMillis(10))
                .pollFunction(i -> i + 1)
                .continueIf(i -> i < 10)
                .abortIf(i -> {
                    counter.incrementAndGet();
                    return i == 3;
                })
                .build();

        assertThrows(PollingAbortedException.class, () -> poller.execute(0));
        assertEquals(4, counter.get());
    }

}
