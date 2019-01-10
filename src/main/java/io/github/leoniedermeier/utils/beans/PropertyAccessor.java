package io.github.leoniedermeier.utils.beans;

import java.util.NoSuchElementException;
import java.util.function.Function;

/**
 * 
 * @param <T> The type of the bean.
 */
public class PropertyAccessor<T> {

	public static <T> PropertyAccessor<T> of(T value) {
		return new PropertyAccessor<>(value);
	}

	public static <T, T1, U> PropertyAccessor<U> of(T value, Function<? super T, ? extends T1> mapper1,
			Function<? super T1, ? extends U> mapper2) {
		if (value == null) {
			return new PropertyAccessor<>(null);
		} else {
			return PropertyAccessor.of(value).then(mapper1).then(mapper2);
		}
	}

	public static <T, U> PropertyAccessor<U> of(T value, Function<? super T, ? extends U> mapper1) {
		if (value == null) {
			return new PropertyAccessor<>(null);
		} else {
			return PropertyAccessor.of(value).then(mapper1);
		}
	}

	private final T value;

	private PropertyAccessor(T value) {
		this.value = value;
	}
	public T get() {
		if (value == null) {
			throw new NoSuchElementException("No value present");
		}
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
