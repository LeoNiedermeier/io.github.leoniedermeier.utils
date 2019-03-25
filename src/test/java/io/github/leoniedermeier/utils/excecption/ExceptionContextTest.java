package io.github.leoniedermeier.utils.excecption;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
            return entries;
        }

    }

    TestExceptionContext exceptionContext = new TestExceptionContext();

    @Nested
    @DisplayName("Method: findLastContextValue(String)")
    class FindLastContextValue {
        @Test
        void no_value() {
            TestExceptionContext context = new TestExceptionContext();
            assertFalse(context.findLastContextValue("myLabel").isPresent());
        }

        @Test
        void multiple_values() {
            TestExceptionContext context = new TestExceptionContext();
            context.addContextValue("myLabel", "1");
            context.addContextValue("myLabel", "2");
            assertEquals("2", context.findLastContextValue("myLabel").get());
        }
    }

    @Nested
    @DisplayName("Method: getContextLabels()")
    class GetContextLabels {
        @Test
        void multiple_values() {
            TestExceptionContext context = new TestExceptionContext();
            context.addContextValue("myLabel-1", "1");
            context.addContextValue("myLabel-2", "2");
            Set<String> result = context.getContextLabels();
            assertEquals(2, result.size());
            assertTrue(result.contains("myLabel-1"));
            assertTrue(result.contains("myLabel-2"));
        }

    }

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
                    return "text: " + text;
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
            exceptionContext.addContextValue("label", value);

            assertTrue(exceptionContext.findFirstContextValue("label").isPresent());
            assertSame(value, exceptionContext.findFirstContextValue("label").get());
        }

        @Test
        void multiple_values_exist() {
            Object value = new Object();
            exceptionContext.addContextValue("label", value);
            exceptionContext.addContextValue("label", "2");

            assertTrue(exceptionContext.findFirstContextValue("label").isPresent());
            assertSame(value, exceptionContext.findFirstContextValue("label").get());
        }

        @Test
        void no_value_exists() {
            assertFalse(exceptionContext.findFirstContextValue("label").isPresent());

            assertFalse(exceptionContext.findFirstContextValue("label").isPresent());
        }

        @Test
        void value_is_null() {
            assertFalse(exceptionContext.findFirstContextValue("label").isPresent());
            assertFalse(exceptionContext.findFirstContextValue(null).isPresent());
        }
    }

    @Nested
    @DisplayName("Method: getContextEntries()")
    class GetContextEntries {

        @Test
        void entries_empty() {
            assertThat(exceptionContext.getContextEntries(), empty());
            assertTrue(exceptionContext.getContextEntries().isEmpty());
        }

        @Test
        void entries_have_two_elements() {
            exceptionContext.addContextValue("label-1", "value-1");
            exceptionContext.addContextValue("label-2", "value-2");

            assertThat(exceptionContext.getContextEntries(), hasSize(2));
            assertEquals(2, exceptionContext.getContextEntries().size());
        }
    }
}
