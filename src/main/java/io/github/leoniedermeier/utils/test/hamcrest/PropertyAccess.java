package io.github.leoniedermeier.utils.test.hamcrest;

import java.util.function.Function;

import org.hamcrest.Matcher;

@FunctionalInterface
@SuppressWarnings("squid:S1711")
//We do not extend UnaryOperator because we do not want to UnaryOperator's default methods.
public interface PropertyAccess<T, R> {

    public static <T, R> PropertyAccess<T, R> property(Function<T, R> transformer, String description) {
        return (Matcher<? super R> matcherForTransformedValue) -> new TransformingMatcher<>(transformer, description,
                matcherForTransformedValue);
    }

    Matcher<T> is(Matcher<? super R> matcher);
}
