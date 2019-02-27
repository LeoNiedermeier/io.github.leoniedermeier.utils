package io.github.leoniedermeier.utils.test.hamcrest;

import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class TransformingMatcher<T, R> extends BaseMatcher<T> {
    private String text;
    private Matcher<? super R> matcherForTransformedValue;
    private Function<T, R> transformer;

    public TransformingMatcher(Function<T, R> transformer, String text, Matcher<? super R> matcherForTransformedValue) {
        super();
        this.transformer = transformer;
        this.text = text;
        this.matcherForTransformedValue = matcherForTransformedValue;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(this.text);
        this.matcherForTransformedValue.describeTo(description);
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description.appendText(this.text);
        this.matcherForTransformedValue.describeMismatch(item, description);
    }

    @Override
    public boolean matches(Object actual) {
        R transformed = this.transformer.apply((T) actual);
        return this.matcherForTransformedValue.matches(transformed);
    }
}
