package io.github.leoniedermeier.utils.test.junit.jupiter;

import org.junit.jupiter.api.Nested;

class UtilClassConventionsTesterTest {

    @Nested
    class MyUtilsUtilClassConventionsTest implements UtilClassConventionsTester<MyUtils> {
    }

    public static final class MyUtils {
        private MyUtils() {
            throw new AssertionError("No MyUtils instances for you!");
        }
    }

}
