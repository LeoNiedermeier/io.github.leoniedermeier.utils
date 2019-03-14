package io.github.leoniedermeier.utils.test.exception;

import static io.github.leoniedermeier.utils.test.exception.ContextedRuntimeExceptionAssertions.assertExceptionHasErrorCode;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.leoniedermeier.utils.excecption.ContextedRuntimeException;
import io.github.leoniedermeier.utils.excecption.EnumErrorCode;

class ContextedRuntimeExceptionAssertionsTest {

    enum MyErrorCodes implements EnumErrorCode {
        CODE_1, CODE_2, CODE_3;
    }

    @Nested
    class AssertExceptionHasErrorCode {
        @Test
        void errorcode_matches() {
            ContextedRuntimeException exception = new ContextedRuntimeException(MyErrorCodes.CODE_1);
            assertExceptionHasErrorCode(MyErrorCodes.CODE_1, exception);
        }

        @Test
        void errorcode_not_matches() {
            ContextedRuntimeException exception = new ContextedRuntimeException(MyErrorCodes.CODE_1);
            assertThrows(AssertionError.class, () -> assertExceptionHasErrorCode(MyErrorCodes.CODE_2, exception));
        }
    }

}
