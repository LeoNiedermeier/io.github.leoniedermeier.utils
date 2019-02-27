package io.github.leoniedermeier.utils.excecption;

import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresent;
import static com.github.npathai.hamcrestopt.OptionalMatchers.isPresentAndIs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.leoniedermeier.utils.excecption.ExceptionContext.Entry;

class ExceptionContextTest {

    static class TestExceptionContext implements ExceptionContext<TestExceptionContext> {

        final List<Entry> entries = new ArrayList<>();

        @Override
        public List<Entry> getContextEntries() {
            return this.entries;
        }

    }

    TestExceptionContext exceptionContext = new TestExceptionContext();

    @Nested
    @DisplayName("Serialization of Entry")
    class EntrySerialization {
        @Test
        void serializeable_value() {
            Entry entry = new Entry("label", "value");
            Entry result = SerializationUtils.roundtrip(entry);
            assertEquals(entry.getLabel(), result.getLabel());
            assertEquals(entry.getValue(), result.getValue());
        }

        @Test
        void not_serializeable_value() {
            class MyValue {
                private final String text;

                public MyValue(String text) {
                    this.text = text;
                }

                @Override
                public String toString() {
                    return "text: " + this.text;
                }
            }
            Entry entry = new Entry("label", new MyValue("someText"));
            Entry result = SerializationUtils.roundtrip(entry);
            assertEquals(entry.getLabel(), result.getLabel());
            assertTrue(result.getLabel() instanceof String);
            assertTrue(((String) result.getValue()).contains("someText"));
        }

        @Test
        void not_serializeable_value_to_string_throws_exception() {
            class MyValue {
                private final String text;

                public MyValue(String text) {
                    this.text = text;
                }

                @Override
                public String toString() {
                    throw new NullPointerException();
                }
            }
            Entry entry = new Entry("label", new MyValue("someText"));
            Entry result = SerializationUtils.roundtrip(entry);
            assertEquals(entry.getLabel(), result.getLabel());
            assertTrue(result.getLabel() instanceof String);
            assertEquals(">> Value Object not serializeable <<", result.getValue());
        }
    }

    @Nested
    @DisplayName("Method: findFirstContextValue(String)")
    class FindFirstContextValue {

        @Test
        void value_exists() {

            Object value = new Object();
            ExceptionContextTest.this.exceptionContext.addContextValue("label", value);

            assertThat(ExceptionContextTest.this.exceptionContext.findFirstContextValue("label"),
                    isPresentAndIs(value));
            assertSame(value, ExceptionContextTest.this.exceptionContext.findFirstContextValue("label").get());
        }

        @Test
        void multiple_values_exist() {
            Object value = new Object();
            ExceptionContextTest.this.exceptionContext.addContextValue("label", value);
            ExceptionContextTest.this.exceptionContext.addContextValue("label", "2");

            assertThat(ExceptionContextTest.this.exceptionContext.findFirstContextValue("label"),
                    isPresentAndIs(value));
            assertSame(value, ExceptionContextTest.this.exceptionContext.findFirstContextValue("label").get());
        }

        @Test
        void no_value_exists() {
            assertThat(ExceptionContextTest.this.exceptionContext.findFirstContextValue("label"), not(isPresent()));

            assertFalse(ExceptionContextTest.this.exceptionContext.findFirstContextValue("label").isPresent());
        }

        @Test
        void value_is_null() {
            assertThat(ExceptionContextTest.this.exceptionContext.findFirstContextValue("label"), not(isPresent()));
            assertFalse(ExceptionContextTest.this.exceptionContext.findFirstContextValue(null).isPresent());
        }
    }

    @Nested
    @DisplayName("Method: getContextEntries()")
    class GetContextEntries {

        @Test
        void entries_empty() {
            assertThat(ExceptionContextTest.this.exceptionContext.getContextEntries(), empty());
            assertTrue(ExceptionContextTest.this.exceptionContext.getContextEntries().isEmpty());
        }

        @Test
        void entries_have_two_elements() {
            ExceptionContextTest.this.exceptionContext.addContextValue("label-1", "value-1");
            ExceptionContextTest.this.exceptionContext.addContextValue("label-2", "value-2");

            assertThat(ExceptionContextTest.this.exceptionContext.getContextEntries(), hasSize(2));
            assertEquals(2, ExceptionContextTest.this.exceptionContext.getContextEntries().size());
        }
    }
}
