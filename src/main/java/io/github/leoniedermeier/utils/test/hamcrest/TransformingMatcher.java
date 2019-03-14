package io.github.leoniedermeier.utils.test.hamcrest;

import java.util.function.Function;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * A {@link Matcher} which applies a {@link Function} to the initial value and
 * after that applies the given {@link Matcher} to the transformed value.
 *
 * @param <T> The type of the initial value.
 * @param <R> The type of the transformed value.
 */
public class TransformingMatcher<T, R> extends BaseMatcher<T> {
    private String text;
    private Matcher<? super R> matcherForTransformedValue;
    private Function<T, R> transformer;

    /**
     * 
     * @param transformer                The {@link Function} to transform the
     *                                   initial value.
     * @param text                       The description to use as text for
     *                                   expectation and mismatch.
     * @param matcherForTransformedValue The {@link Matcher} to apply to the
     *                                   transformed value.
     */
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
        try {
            // check for type
            description.appendText(this.text);
            this.matcherForTransformedValue.describeMismatch(this.transformer.apply((T) item), description);

        } catch (ClassCastException e) {
            description.appendText("wrong type: ").appendText(item.getClass().getName()).appendText(" (").appendValue(item)
                    .appendText(")");
        }
    }

    @Override
    public boolean matches(Object actual) {
        try {
            R transformed = this.transformer.apply((T) actual);
            return this.matcherForTransformedValue.matches(transformed);
        } catch (ClassCastException e) {
            return false;
        }
    }
}
