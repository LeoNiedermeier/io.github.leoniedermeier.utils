package io.github.leoniedermeier.utils.test.exception;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.stream.Stream;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import io.github.leoniedermeier.utils.excecption.ContextedRuntimeException;
import io.github.leoniedermeier.utils.excecption.ErrorCode;

public class ContextedRuntimeExceptionHamcrestMatchers {

    private static class ContextedRuntimeExceptionErrorCodeMatcher
            extends TypeSafeDiagnosingMatcher<ContextedRuntimeException> {
        private final ErrorCode[] expected;

        public ContextedRuntimeExceptionErrorCodeMatcher(ErrorCode... expected) {
            super(ContextedRuntimeException.class);
            this.expected = expected;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText((" a ContextedRuntimeException containing error codes ")).appendValueList("[", ", ",
                    "]", expected);
        }

        @Override
        protected boolean matchesSafely(ContextedRuntimeException exception, Description mismatchDescription) {

            List<ErrorCode> mismatches = Stream.of(expected)
                    .filter(ec -> exception.getErrorCodes().noneMatch(ec::equalsTo)).collect(toList());

            if (mismatches.isEmpty()) {
                return true;
            }
            mismatchDescription.appendText("actual: ").appendValueList("[", ", ", "]",
                    exception.getErrorCodes().toArray());
            mismatchDescription.appendText(",\n     mismatches were: ").appendValueList("[", ", ", "]", mismatches);
            return false;
        }
    }

    public static Matcher<ContextedRuntimeException> hasErrorCode(ErrorCode expected) {
        return new ContextedRuntimeExceptionErrorCodeMatcher(expected);
    }

    public static Matcher<ContextedRuntimeException> hasErrorCodes(ErrorCode... expected) {
        return new ContextedRuntimeExceptionErrorCodeMatcher(expected);
    }

    private ContextedRuntimeExceptionHamcrestMatchers() {
        throw new AssertionError("No ContextedRuntimeExceptionHamcrestMatchers instances for you!");
    }
}
