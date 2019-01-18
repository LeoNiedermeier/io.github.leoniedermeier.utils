package io.github.leoniedermeier.utils.excecption;

import static io.github.leoniedermeier.utils.test.exception.ContextedRuntimeExceptionAssertions.assertThrowsContextedRuntimeException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ContextedRuntimeExceptionTest {

	enum MyErrorCodes implements EnumErrorCode {
		CODE_1, CODE_2;
	}

	static class ThrowsException {

		void doSomething() {
			throw new ContextedRuntimeException(MyErrorCodes.CODE_1).addContextValue("label", "MyValue")
					.addErrorCode(MyErrorCodes.CODE_2);
		}
	}

	@Test
	void exceptionTesting() {

		ThrowsException throwsException = new ThrowsException();
		ContextedRuntimeException exception = assertThrows(ContextedRuntimeException.class,
				() -> throwsException.doSomething());
		assertEquals(MyErrorCodes.CODE_2, exception.findLastErrorCode().get());
	}

	@Test
	void exceptionTesting2() {
		// same as exceptionTesting() but with custom assert method.
		ThrowsException throwsException = new ThrowsException();
		assertThrowsContextedRuntimeException(MyErrorCodes.CODE_2, () -> throwsException.doSomething());
	}

}
