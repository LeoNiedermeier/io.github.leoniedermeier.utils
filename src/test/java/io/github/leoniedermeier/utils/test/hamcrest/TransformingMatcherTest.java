package io.github.leoniedermeier.utils.test.hamcrest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.hamcrest.Matchers;
import org.hamcrest.StringDescription;
import org.junit.jupiter.api.Test;

class TransformingMatcherTest {

    @Test
    void wrong_input_type() {
        TransformingMatcher<String, String> matcher = new TransformingMatcher<>(String::trim, "String trim",
                Matchers.anything());
        assertFalse(matcher.matches(BigInteger.ONE));
        StringDescription description = new StringDescription();
        matcher.describeMismatch(BigInteger.ONE, description);
        assertTrue(description.toString().contains("wrong type"));
    }

    @Test
    void matches() {
        TransformingMatcher<String, String> matcher = new TransformingMatcher<>(String::toUpperCase, "String toUpperCase",
                Matchers.equalTo("ABCD"));
        assertTrue(matcher.matches("abcd"));
        assertThat("abcd", new TransformingMatcher<String, Integer>(String::length, "String length", Matchers.greaterThan(2)));

        assertThat("abcd", PropertyAccess.property(String::length, "String length").is(Matchers.greaterThan(2)));
    }

}
