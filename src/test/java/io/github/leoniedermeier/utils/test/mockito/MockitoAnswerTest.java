package io.github.leoniedermeier.utils.test.mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class)
class MockitoAnswerTest {

    static class MyService {
        public String someMethod(String string) {
            throw new RuntimeException("Method 'someMethod' should not be called");
        }
    }

    @Test
    void mockWithAnswer(@Mock MyService mock) {
        Mockito.when(mock.someMethod(ArgumentMatchers.anyString())).then(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                return "Mock:" + args[0];
            }
        });
        assertEquals("Mock:foo", mock.someMethod("foo"));
    }

    @Test
    void mockWithAdditionalAnswers(@Mock MyService mock) {
        Mockito.when(mock.someMethod(ArgumentMatchers.anyString())).then(AdditionalAnswers.answer(s -> "Mock:" + s));
        assertEquals("Mock:foo", mock.someMethod("foo"));
    }

    @Test
    void mockWithAdditionalAnswersMethodReference(@Mock MyService mock) {
        Mockito.when(mock.someMethod(ArgumentMatchers.anyString())).then(AdditionalAnswers.answer("Mock:"::concat));
        assertEquals("Mock:foo", mock.someMethod("foo"));
    }

    @Test
    void mockWithLambdaAnswer(@Mock MyService mock) {
        Mockito.when(mock.someMethod("foo")).then(invocation -> {
            Object[] args = invocation.getArguments();
            return "Mock:" + args[0];
        });
        assertEquals("Mock:foo", mock.someMethod("foo"));
    }

    @Test
    void mockWithMethodReferenceAnswer(@Mock MyService mock) {
        Mockito.when(mock.someMethod("foo")).then(this::extracted);
        assertEquals("Mock:foo", mock.someMethod("foo"));
    }

    private String extracted(InvocationOnMock invocation) {
        Object[] args = invocation.getArguments();
        return "Mock:" + args[0];
    }
}
