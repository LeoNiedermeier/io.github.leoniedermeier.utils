package io.github.leoniedermeier.utils.excecption;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import io.github.leoniedermeier.utils.excecption.ContextedRuntimeException;

public class ContextedRuntimeExceptionTest {

	enum MyErrorCodes implements EnumErrorCode {
		CODE_1, CODE_2;
	}

	static class ThrowsException {

		void doSomething(String x) {
			throw new ContextedRuntimeException(MyErrorCodes.CODE_1).addContextValue("label", "MyValue")
					.addErrorCode(MyErrorCodes.CODE_2);
		}
	}

	@Test
	void exceptionTesting() {

		ThrowsException throwsException = new ThrowsException();
		ContextedRuntimeException exception = assertThrows(ContextedRuntimeException.class,
				() -> throwsException.doSomething("d"));
		assertEquals(MyErrorCodes.CODE_2, exception.findLastErrorCode().get());
	}

	@Test
	void exceptionTesting2() {
		// same as exceptionTesting() but with custom assert method.
		ThrowsException throwsException = new ThrowsException();
		assertThrowsContextedRuntimeException(MyErrorCodes.CODE_2, () -> throwsException.doSomething("d"));
	}

	/**
	 * Custom assert method checking for {@link ContextedRuntimeException} and {@link ErrorCode}.
	 * @param errorCode The expected {@link ErrorCode}.
	 * @param executable The {@link Executable}.
	 * @return 
	 * @see org.junit.jupiter.api.Assertions#assertThrows
	 */
	public static ContextedRuntimeException assertThrowsContextedRuntimeException(ErrorCode errorCode, Executable executable) {
		ContextedRuntimeException exception = assertThrows(ContextedRuntimeException.class, executable);
		assertEquals(errorCode, exception.findLastErrorCode().get(), "Errorcode does not match.");
		return exception;
	}

}
