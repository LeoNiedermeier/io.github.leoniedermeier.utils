package io.github.leoniedermeier.utils.beans;

import java.util.function.Function;

public class PropertyAccessor {

	public static <T, T1, T2, U> U get(T value, Function<? super T, ? extends T1> mapper1,
			Function<? super T1, ? extends T2> mapper2, Function<? super T2, ? extends U> mapper3) {
		return get(get(value, mapper1, mapper2), mapper3);
	}

	public static <T, T1, U> U get(T value, Function<? super T, ? extends T1> mapper1,
			Function<? super T1, ? extends U> mapper2) {
		return get(get(value, mapper1), mapper2);
	}

	public static <T, U> U get(T value, Function<? super T, ? extends U> mapper1) {
		return value == null ? null : mapper1.apply(value);
	}
}
