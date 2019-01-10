package io.github.leoniedermeier.utils.beans;

import java.util.function.Function;

/**
 * 
 * @param <T> The type of the bean.
 */
public class PropertyAccessor<T> {

	public static <T, T1, T2, U> U get(T value, Function<? super T, ? extends T1> mapper1,
			Function<? super T1, ? extends T2> mapper2, Function<? super T2, ? extends U> mapper3) {
		return get(get(value, mapper1, mapper2), mapper3);
	}

	public static <T, T1, U> U get(T value, Function<? super T, ? extends T1> mapper1,
			Function<? super T1, ? extends U> mapper2) {
		return get(get(value, mapper1), mapper2);
	}

	public static <T, U> U get(T value, Function<? super T, ? extends U> mapper1) {
		return PropertyAccessor.of(value).then(mapper1).get();
	}

	public static <T> PropertyAccessor<T> of(T value) {
		return new PropertyAccessor<>(value);
	}

	private final T value;

	private PropertyAccessor(T value) {
		this.value = value;
	}

	/**
	 * For properties a returning {@code null} is nothing special.
	 */
	public T get() {
		return value;
	}

	public T orElse(T other) {
		return value != null ? value : other;
	}

	public <U> PropertyAccessor<U> then(Function<? super T, ? extends U> mapper) {
		if (value == null) {
			return new PropertyAccessor<>(null);
		} else {
			return new PropertyAccessor<>(mapper.apply(value));
		}
	}
}
