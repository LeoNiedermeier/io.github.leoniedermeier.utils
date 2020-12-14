package io.github.leoniedermeier.utils.test.junit.jupiter;

import org.junit.jupiter.api.Nested;

class UtilClassConventionsTester2Test {

    @Nested
    class MyUtilsUtilClassConventionsTest implements UtilClassConventionsTester2<MyUtils> {
    }

    public static final class MyUtils {
        private MyUtils() {
            throw new AssertionError("No MyUtils instances for you!");
        }
    }

}
