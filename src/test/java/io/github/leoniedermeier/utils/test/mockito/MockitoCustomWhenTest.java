package io.github.leoniedermeier.utils.test.mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Function;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MockitoCustomWhenTest {

    static class MyService {
        public String someMethod(String string) {
            throw new RuntimeException("Method 'someMethod' should not be called");
        }
    }

    interface When<T, R> {
        interface With<T, R> {
            interface Then<T, R> {
                void then(Function<T, R> mockFunction);
            }

            Then<T, R> with(T t);
        }

        static <T, R> With<T, R> when(Function<T, R> function) {
            return (T t) -> {
                return (Function<T, R> mockFunction) -> Mockito.when(function.apply(t))
                        .then(invocation -> mockFunction.apply(invocation.getArgument(0)));
            };
        }

    }

    @Test
    void customWhen(@Mock MyService mock) {
        When.when(mock::someMethod).with(ArgumentMatchers.anyString()).then(s -> "called-" + s);
        assertEquals("called-foo", mock.someMethod("foo"));
    }

    @Test
    @Disabled
    void customWhenWithNotMatchingParameter(@Mock MyService mock) {
        When.when(mock::someMethod).with("XXX").then(s -> "called-" + s);
        assertThrows(Exception.class, () -> mock.someMethod("foo"));
    }
}
