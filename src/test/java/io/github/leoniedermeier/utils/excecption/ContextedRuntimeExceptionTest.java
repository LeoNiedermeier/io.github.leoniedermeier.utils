package io.github.leoniedermeier.utils.excecption;

import static io.github.leoniedermeier.utils.test.exception.ContextedRuntimeExceptionAssertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ContextedRuntimeExceptionTest {

    enum MyErrorCodes implements EnumErrorCode, MessagesProvider {
        CODE_1("Messeg 1"), //
        CODE_2("Messeg 2"), //
        CODE_3("Messeg 1");

        private final String message;

        private MyErrorCodes(String message) {
            this.message = message;
        }

        @Override
        public String getMessage() {
            return this.message;
        }
    }

    static class ThrowsException {

        String doSomething() {
            throw new ContextedRuntimeException(MyErrorCodes.CODE_1).addContextValue("label", "MyValue")
                    .addErrorCode(MyErrorCodes.CODE_3);
        }
    }

    @Test
    void exceptionTesting() {

        ThrowsException throwsException = new ThrowsException();
        ContextedRuntimeException exception = assertThrows(ContextedRuntimeException.class,
                () -> throwsException.doSomething());
        assertEquals(MyErrorCodes.CODE_3, exception.findLastErrorCode().get());
    }

    @Test
    void exceptionTesting2() {
        // same as exceptionTesting() but with custom assert method.
        ThrowsException throwsException = new ThrowsException();
        ContextedRuntimeException exception = assertThrowsContextedRuntimeException(MyErrorCodes.CODE_1,
                () -> throwsException.doSomething());
        assertExceptionHasErrorCode(MyErrorCodes.CODE_1, exception);
    }

}
