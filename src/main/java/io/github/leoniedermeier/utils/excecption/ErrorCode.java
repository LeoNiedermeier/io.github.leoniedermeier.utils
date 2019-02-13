package io.github.leoniedermeier.utils.excecption;

import java.util.Objects;

/**
 * Identifies an error code.
 */
public interface ErrorCode {

	/**
	 * Returns a string representation of the error code.
	 */
	String code();
	
	
	default boolean equalsTo(ErrorCode other) {
		return other != null && Objects.equals(code(), other.code());
	}
	
}
