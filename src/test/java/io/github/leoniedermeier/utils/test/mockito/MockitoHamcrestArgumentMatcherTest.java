package io.github.leoniedermeier.utils.test.mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.hamcrest.MockitoHamcrest;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MockitoHamcrestArgumentMatcherTest {

    static class MyService {
        public String someMethod(String string) {
            throw new RuntimeException("Method 'someMethod' should not be called");
        }
    }

    @Test
    void hamcrestArgumentMatcher(@Mock MyService myService) {

        Mockito.when(myService.someMethod(MockitoHamcrest.argThat(Matchers.startsWith("ABC")))).thenReturn("123");
 
        String result = myService.someMethod("ABCDE");

        assertEquals("123", result);
        Mockito.verify(myService).someMethod(MockitoHamcrest.argThat(Matchers.startsWith("ABC")));
    }

}
