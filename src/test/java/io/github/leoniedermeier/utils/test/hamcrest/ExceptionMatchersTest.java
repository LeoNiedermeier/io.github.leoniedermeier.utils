package io.github.leoniedermeier.utils.test.hamcrest;

import static io.github.leoniedermeier.utils.test.hamcrest.ExceptionMatchers.throwsA;
import static io.github.leoniedermeier.utils.test.hamcrest.PropertyAccess.property;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.SQLException;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.leoniedermeier.utils.test.hamcrest.ExceptionMatchers.Executable;

class ExceptionMatchersTest {
    @Nested
    class ThrowsA {

        @Test
        void matches() {
            assertThat(doThrow(new SQLDataException()), throwsA(SQLException.class));
        }

        @Test
        void no_matches() {
            assertThrows(AssertionError.class,
                    () -> assertThat(doThrow(new IOException()), throwsA(SQLException.class)));

        }

        @Test
        void no_matches_no_exception() {
            assertThrows(AssertionError.class, () -> assertThat(() -> {
            }, ExceptionMatchers.throwsA(Exception.class)));
        }
    }

    @Nested
    class ThrowsAWith {
        @Test
        void matches() {
            SQLException exception = new SQLException();
            assertThat(doThrow(exception), throwsA(SQLException.class).with(Matchers.anything()));
        }

        @Test
        void not_matches() {
            SQLException exception = new SQLException();
            assertThrows(AssertionError.class,
                    () -> assertThat(doThrow(exception), throwsA(SQLException.class).with(Matchers.nullValue())));
        }

        @Test
        void proper_exception_thrown_but_mismatch_in_property() {
            assertThrows(AssertionError.class, () -> assertThat(() -> {
                throw new SQLException("Reason", "SQL-STATE");
            }, throwsA(SQLException.class)
                    .with(property(SQLException::getSQLState, " sql-state ").is(equalTo("Expected-SQL-STATE")))));
        }

        @Test
        void proper_exception_thrown_AND_MATCH_in_property() {
            assertThat(() -> {
                throw new SQLException("Reason", "SQL-STATE");
            }, throwsA(SQLException.class)
                    .with(property(SQLException::getSQLState, " sql-state ").is(equalTo("SQL-STATE"))));
        }
    }

    private static Executable doThrow(Exception e) {
        return () -> {
            throw e;
        };
    }
}
