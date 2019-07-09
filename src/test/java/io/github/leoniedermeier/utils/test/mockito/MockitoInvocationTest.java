package io.github.leoniedermeier.utils.test.mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.exceptions.misusing.PotentialStubbingProblem;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class MockitoInvocationTest {

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
                        .thenAnswer(invocation -> mockFunction.apply(invocation.getArgument(0)));
            };
        }

    }

    @Test
    void customWhen(@Mock MyService mock) {
        When.when(mock::someMethod).with(ArgumentMatchers.anyString()).then(s -> "called-" + s);
        assertEquals("called-foo", mock.someMethod("foo"));
    }

    @Test
    void customWhenWithNotMatchingParameter(@Mock MyService mock) {
        When.when(mock::someMethod).with("XXX").then(s -> "called-" + s);
        assertThrows(PotentialStubbingProblem.class, () -> mock.someMethod("foo"));
    }

    @Test
    void mockWithAnswer(@Mock MyService mock) {
        Mockito.when(mock.someMethod(ArgumentMatchers.anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return "Mock:" + args[0];
            }
        });
        assertEquals("Mock:foo", mock.someMethod("foo"));
    }

    @Test
    void mockWithLambdaAnswer(@Mock MyService mock) {
        Mockito.when(mock.someMethod("foo")).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            return "Mock:" + args[0];
        });
        assertEquals("Mock:foo", mock.someMethod("foo"));
    }

    @Test
    void mockWithMethodReferenceAnswer(@Mock MyService mock) {
        Mockito.when(mock.someMethod("foo")).thenAnswer(this::extracted);
        assertEquals("Mock:foo", mock.someMethod("foo"));
    }

    private String extracted(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        return "Mock:" + args[0];
    }
}
