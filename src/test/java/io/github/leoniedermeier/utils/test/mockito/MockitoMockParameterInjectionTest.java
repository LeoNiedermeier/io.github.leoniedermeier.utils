package io.github.leoniedermeier.utils.test.mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MockitoMockParameterInjectionTest {

    

    private static final Logger LOGGER = LogManager.getLogger(MockitoMockParameterInjectionTest.class);

    @Test
    public void test(@Mock List<Integer> mock) {
       assertNotNull(mock);
    }
    
    
    @Test
    public void test() {
        List<Integer> mock = Mockito.mock(List.class);
        assertNotNull(mock);
    }
}
