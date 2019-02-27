package io.github.leoniedermeier.utils.test.exception;

import static org.hamcrest.core.AllOf.allOf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsIterableContaining;

import io.github.leoniedermeier.utils.excecption.ErrorCode;

public class ErrorCodeMatchers {

    private static class PredicateMatcher<X, T extends X> extends TypeSafeMatcher<T> {

        public static <X, T extends X> Matcher<T> of(BiPredicate<X, X> predicate, T expected) {
            return new PredicateMatcher<>(predicate, expected);
        }

        private final T expected;

        private final BiPredicate<X, X> predicate;

        public PredicateMatcher(BiPredicate<X, X> predicate, T expected) {
            this.predicate = predicate;
            this.expected = expected;
        }

        @Override
        public void describeTo(Description description) {
            description.appendValue(this.expected);

        }

        @Override
        protected boolean matchesSafely(T item) {
            return this.predicate.test(this.expected, item);
        }
    }

    static class ErrorCodeMatcher extends TypeSafeDiagnosingMatcher<ErrorCode> {
        private final ErrorCode expected;

        public ErrorCodeMatcher(ErrorCode expected) {
            super(ErrorCode.class);
            this.expected = expected;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText((" a ErrorCode ")).appendValue(this.expected);

        }

        @Override
        protected boolean matchesSafely(ErrorCode item, Description mismatchDescription) {
            if (this.expected.equalsTo(item)) {
                return true;
            } else {
                mismatchDescription.appendText("available was: ").appendValue(item);
                return false;
            }
        }

    }

    public static <X, T extends X> Matcher<T> equalTo(BiPredicate<X, X> predicate, T expected) {
        return PredicateMatcher.of(predicate, expected);
    }

    @SafeVarargs
    public static <X, T extends X> Matcher<Iterable<T>> hasItems(BiPredicate<X, X> predicate, T... items) {
        List<Matcher<? super Iterable<T>>> all = new ArrayList<>(items.length);
        for (T item : items) {
            all.add(new IsIterableContaining<>(PredicateMatcher.of(predicate, item)));
        }

        return allOf(all);
    }

    private ErrorCodeMatchers() {
        throw new AssertionError("No ErrorCodeMatchers instances for you!");
    }
}
