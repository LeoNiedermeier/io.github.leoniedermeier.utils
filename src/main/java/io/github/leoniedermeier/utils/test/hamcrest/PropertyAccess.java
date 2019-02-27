package io.github.leoniedermeier.utils.test.hamcrest;

import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public final class PropertyAccess {

    public static <T, R> Is<T, R> property(Function<T, R> transformer, String text) {
        return Is.<T, R>createFrom(transformer, text);
    }

    public static <T, R> Matcher<T> property(Function<T, R> transformer, String text, Matcher<? super R> matcher) {
        return new BaseMatcher<T>() {

            @Override
            public void describeTo(Description description) {
                description.appendText(text);
                matcher.describeTo(description);
            }

            @Override
            public void describeMismatch(Object item, Description description) {
                description.appendText(text);
                matcher.describeMismatch(item, description);
            }

            @Override
            public boolean matches(Object actual) {
                return matcher.matches(transformer.apply((T) actual));
            }
        };
    }

    private PropertyAccess() {
        throw new AssertionError("No PropertyAccess instances for you!");
    }
}
