package io.github.leoniedermeier.utils.stream;

import static io.github.leoniedermeier.utils.stream.Step.done;
import static io.github.leoniedermeier.utils.stream.Step.execute;
import static io.github.leoniedermeier.utils.stream.Step.next;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class StepTest {
    private final long MAX = 10_000;

    @Test()
    public void simple() {
        long result = execute(recursiveFunction(0, 0));

        assertEquals(MAX * (MAX + 1) / 2, result);
    }

    @Test()
    public void simple_alternative() {
        long result = execute(recursiveFunctionAlternative(0, 0));

        assertEquals(MAX * (MAX + 1) / 2, result);
    }

    private Step<Long> recursiveFunction(int n, long acc) {
        if (n > MAX)
            return done(acc);
        else
            return next(() -> recursiveFunction(n + 1, acc + n));
    }

    private Step<Long> recursiveFunctionAlternative(int n, long acc) {
        return next(() -> {
            if (n > MAX) {
                return done(acc);
            } else {
                return recursiveFunctionAlternative(n + 1, acc + n);
            }
        });
    }
}