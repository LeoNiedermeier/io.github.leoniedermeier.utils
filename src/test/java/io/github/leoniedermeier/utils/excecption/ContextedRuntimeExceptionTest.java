package io.github.leoniedermeier.utils.excecption;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import io.github.leoniedermeier.utils.excecption.ContextedRuntimeException;

public class ContextedRuntimeExceptionTest {

	enum MyErrorCodes implements EnumErrorCode {
		CODE_1, CODE_2;
	}

	static class ThrowsException {

		void doSomething(String x) {
			throw new ContextedRuntimeException(MyErrorCodes.CODE_1).addContextValue("label", "MyValue").addErrorCode(MyErrorCodes.CODE_2);
		}
	}

	@Test
	void exceptionTesting() {

		ThrowsException throwsException = new ThrowsException();
		ContextedRuntimeException exception = assertThrows(ContextedRuntimeException.class,
				() -> throwsException.doSomething("d"));
		assertEquals(MyErrorCodes.CODE_2, exception.findLastErrorCode().get());
	}

}
