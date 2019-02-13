package io.github.leoniedermeier.utils.misc;

import java.util.function.Supplier;

/**
 * 
 * see
 * http://hg.openjdk.java.net/code-tools/jcstress/file/9270b927e00f/tests-custom/src/main/java/org/openjdk/jcstress/tests/singletons/SafeDCL.java
 */
public class ThreadSafeSingelInstanceObjectFactory {

	private volatile Object instance;

	@SuppressWarnings("unchecked")
	public <T> T getInstance(Supplier<T> s) {
		// Type parameter T defined here makes usage easier.
		if (instance == null) {
			synchronized (this) {
				if (instance == null) {
					instance = s.get();
				}
			}
		}
		return (T) instance;
	}
}