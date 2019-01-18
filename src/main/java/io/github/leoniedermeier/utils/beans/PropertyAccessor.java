package io.github.leoniedermeier.utils.beans;

import java.util.function.Function;

public final class PropertyAccessor {

	public static <T, T1, T2, U> U get(T value, Function<? super T, ? extends T1> getter1,
			Function<? super T1, ? extends T2> getter2, Function<? super T2, ? extends U> getter3) {
		return get(get(value, getter1, getter2), getter3);
	}
	public static <T, T1, U> U get(T value, Function<? super T, ? extends T1> getter1,
			Function<? super T1, ? extends U> getter2) {
		return get(get(value, getter1), getter2);
	}

	public static <T, U> U get(T value, Function<? super T, ? extends U> getter) {
		return value == null ? null : getter.apply(value);
	}

	private PropertyAccessor() {
		throw new AssertionError("No PropertyAccessor instances for you!");
	}
}
