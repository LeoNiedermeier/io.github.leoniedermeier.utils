package io.github.leoniedermeier.utils.test.hamcrest;

import org.hamcrest.BaseMatcher;
import org.hamcrest.CustomMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

/**
 * <h1>NOTE (from javadoc):</h1> An {@code Error} is a subclass of
 * {@code Throwable} that indicates serious problems that a reasonable
 * application should not try to catch. Most such errors are abnormal
 * conditions.
 **/
public final class ExceptionMatchers {

    @FunctionalInterface
    public interface Executable {

        @SuppressWarnings("squid:S00112")
        // throw generic exception
        void execute() throws Exception;

    }

    public static class ExecutableThrowsMatcher<T extends Exception> extends BaseMatcher<Executable> {

        private static Exception execute(Executable actual) {
            try {
                actual.execute();
            } catch (Exception e) {
                return e;
            }
            return null;
        }

        private Matcher<Exception> exceptionOfType;

        private Class<T> expected;

        public ExecutableThrowsMatcher(Class<T> expected, Matcher<Exception> exceptionOfType) {
            super();
            this.expected = expected;
            this.exceptionOfType = exceptionOfType;
        }

        @Override
        public void describeMismatch(Object item, Description description) {
            exceptionOfType.describeMismatch(item, description);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("execution throws");
            this.exceptionOfType.describeTo(description);
        }

        @Override
        public boolean matches(Object actual) {
            Exception exception = execute((Executable) actual);
            return this.exceptionOfType.matches(exception);
        }

        public Matcher<Executable> with(Matcher<? super T> matcher) {
            return PropertyAccess.<Executable, Exception>property(ExecutableThrowsMatcher::execute, "throws a ")
                    .is(Matchers.allOf(this.exceptionOfType,
                            PropertyAccess.property(this.expected::cast, " with ").is(matcher)));
        }

    }

    /**
     * <em>Asserts</em> that execution of the {@link Executable} throws
     * an exception of the {@code expectedType}.
     *
     * <p>If no exception is thrown, or if an exception of a different type is
     * thrown, this method will fail.
     */
    public static <T extends Exception> ExecutableThrowsMatcher<T> throwsA(Class<T> expectedType) {
        return new ExecutableThrowsMatcher<>(expectedType, isExceptionOfType(expectedType));
    }

    public static <T extends Exception> Matcher<Executable> throwsA(Class<T> expected, Matcher<? super T> matcher) {

        return new ExecutableThrowsMatcher<>(expected, isExceptionOfType(expected)).with(matcher);
    }

    private static <T extends Exception, R> Matcher<T> isExceptionOfType(Class<R> expected) {
        return new CustomMatcher<T>(" exception of type " + expected) {

            private String mismatch;

            @Override
            public void describeMismatch(Object item, Description description) {
                // attention: item is of type Executable
                description.appendText(mismatch);
            }

            @Override
            public boolean matches(Object actual) {
                if (expected.isInstance(actual)) {
                    return true;
                }
                if (actual == null) {
                    mismatch = "nothing";
                } else {
                    mismatch = String.format("<%s> is not an exception of type <%s>", actual.getClass(), expected);
                }
                return false;
            }

        };
    }

    private ExceptionMatchers() {
        throw new AssertionError("No ExceptionMatchers instances for you!");
    }
}
