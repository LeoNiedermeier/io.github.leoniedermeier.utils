package io.github.leoniedermeier.utils.test.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.function.Executable;

import io.github.leoniedermeier.utils.excecption.ContextedRuntimeException;
import io.github.leoniedermeier.utils.excecption.ErrorCode;

public final class ContextedRuntimeExceptionAssertions {

	/**
	 * Custom assert method checking for {@link ContextedRuntimeException} and
	 * {@link ErrorCode}.
	 * 
	 * @param errorCode  The expected {@link ErrorCode}.
	 * @param executable The {@link Executable}.
	 * @return The thrown {@link ContextedRuntimeException}.
	 * @see org.junit.jupiter.api.Assertions#assertThrows
	 */
	public static ContextedRuntimeException assertThrowsContextedRuntimeException(ErrorCode errorCode,
			Executable executable) {
		ContextedRuntimeException exception = assertThrows(ContextedRuntimeException.class, executable);
		assertEquals(errorCode, exception.findLastErrorCode().get(), "Errorcode does not match.");
		return exception;
	}

	private ContextedRuntimeExceptionAssertions() {
		throw new AssertionError("No ContextedRuntimeExceptionAssertions instances for you!");
	}
}
