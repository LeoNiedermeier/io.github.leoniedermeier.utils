package io.github.leoniedermeier.utils.test.hamcrest;

import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

@FunctionalInterface
@SuppressWarnings("squid:S1711")
//We do not extend UnaryOperator because we do not want to UnaryOperator's default methods.
public interface Is<T, R> {

    static <T, R> Is<T, R> createFrom(Function<T, R> transformer, String description) {
        return (Matcher<? super R> matcherForTransformedValue) -> {
            TransformingMatcher<T, R> transformingMatcher = new TransformingMatcher<>(transformer, description,
                    matcherForTransformedValue);
            return new BaseMatcher<T>() {

                @Override
                public boolean matches(Object actual) {
                    return transformingMatcher.matches(actual);
                }

                @Override
                public void describeTo(Description description) {
                    transformingMatcher.describeTo(description);
                }
                @Override
                public void describeMismatch(Object item, Description description) {
                    description.appendText("was ").appendValue(item);
                    transformingMatcher.describeMismatch(item, description);
                }
            };

        };

    }

    static <T, R> Is<T, R> createFrom(Matcher<? super T> matcher, Function<T, R> transformer, String description) {
        return (Matcher<? super R> matcherForTransformedValue) -> {
            TransformingMatcher<T, R> transformingMatcher = new TransformingMatcher<>(transformer, description,
                    matcherForTransformedValue);
            return new BaseMatcher<T>() {

                @Override
                public boolean matches(Object actual) {
                    return matcher.matches(actual) && transformingMatcher.matches(actual);
                }

                @Override
                public void describeTo(Description description) {
                    matcher.describeTo(description);
                    transformingMatcher.describeTo(description);

                }
                @Override
                public void describeMismatch(Object item, Description description) {
                    description.appendText("was ").appendValue(item);
                    transformingMatcher.describeMismatch(item, description);
                }
            };

        };

    }

    Matcher<T> is(Matcher<? super R> matcher);
}
