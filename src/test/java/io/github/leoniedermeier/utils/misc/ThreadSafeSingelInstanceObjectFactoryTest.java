package io.github.leoniedermeier.utils.misc;

import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class ThreadSafeSingelInstanceObjectFactoryTest {

	private final ThreadSafeSingelInstanceObjectFactory objectFactory = new ThreadSafeSingelInstanceObjectFactory();

	private MyBean getInstance() {
		return objectFactory.getInstance(() -> new MyBean());
	}

	@Test
	void test1() {
		MyBean instance_1 = getInstance();
		MyBean instance_2 = getInstance();
		assertSame(instance_1, instance_2);
	}

	private static class MyBean {

	}
}
