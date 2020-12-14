package io.github.leoniedermeier.utils.test.exception;

import static org.hamcrest.core.AllOf.allOf;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsIterableContaining;

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

    public static <X, T extends X> Matcher<T> equalTo(BiPredicate<X, X> predicate, T expected) {
        return PredicateMatcher.of(predicate, expected);
    }

    @SafeVarargs
    public static <X, T extends X> Matcher<Iterable<T>> hasItems(BiPredicate<X, X> predicate, T... items) {
        List<Matcher<? super Iterable<T>>> all = Stream.of(items)
                .map(item -> new IsIterableContaining<>(PredicateMatcher.of(predicate, item)))
                .collect(Collectors.toList());
        return allOf(all);
    }

    private ErrorCodeMatchers() {
        throw new AssertionError("No ErrorCodeMatchers instances for you!");
    }
}
