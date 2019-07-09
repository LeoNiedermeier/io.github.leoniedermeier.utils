package io.github.leoniedermeier.utils.test.mockito;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MockitoArgumentCaptorTest {

    static class MyService {
        public String someMethod(String string) {
            throw new RuntimeException("Method 'someMethod' should not be called");
        }
    }

    @Test
    void argumentCaptoring(@Mock MyService myService) {

        // Setup
        Mockito.when(myService.someMethod(ArgumentMatchers.anyString())).thenReturn("1");

        // Execute
        String result = myService.someMethod("XX");

        // Verify
        assertEquals("1", result);

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(myService).someMethod(argumentCaptor.capture());
        assertEquals("XX", argumentCaptor.getValue());
    }

}
