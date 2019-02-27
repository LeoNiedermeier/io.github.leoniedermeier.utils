package io.github.leoniedermeier.utils.misc;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ThreadSafeSingelInstanceObjectFactoryTest {

    private final ThreadSafeSingelInstanceObjectFactory objectFactory = new ThreadSafeSingelInstanceObjectFactory();

    private MyBean getInstance() {
        return this.objectFactory.getInstance(() -> new MyBean());
    }

    @Test
    @DisplayName("Same instance is returned by multiple calls to getInstance method")
    void same_instance_returned_from_multiple_calls() {
        MyBean instance_1 = getInstance();
        MyBean instance_2 = getInstance();
        assertSame(instance_1, instance_2);
    }

    private static class MyBean {

    }
}
