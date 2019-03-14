package io.github.leoniedermeier.utils.excecption;

import static io.github.leoniedermeier.utils.test.exception.ContextedRuntimeExceptionHamcrestMatchers.hasErrorCode;
import static io.github.leoniedermeier.utils.test.exception.ContextedRuntimeExceptionHamcrestMatchers.hasErrorCodes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.leoniedermeier.utils.test.exception.ErrorCodeMatchers;

class ContextedRuntimeExceptionHamcrestMatchersTest {

    enum MyErrorCodes implements EnumErrorCode {
        CODE_1, CODE_2, CODE_3;
    }

    static class ThrowsException {

        void doSomething() {
            throw new ContextedRuntimeException(MyErrorCodes.CODE_1).addContextValue("label", "MyValue")
                    .addErrorCode(MyErrorCodes.CODE_3);
        }
    }

    @Nested
    class HasErrorCodes {

        @Test
        void matches_single_errror_code() {
            ContextedRuntimeException exception = new ContextedRuntimeException(MyErrorCodes.CODE_1);
            assertThat(exception, hasErrorCode(MyErrorCodes.CODE_1));
        }

        @Test
        void matches_multiple_error_codes() {
            ContextedRuntimeException exception = new ContextedRuntimeException(MyErrorCodes.CODE_1)
                    .addContextValue("label", "MyValue").addErrorCode(MyErrorCodes.CODE_3);
            assertThat(exception, hasErrorCodes(MyErrorCodes.CODE_1, MyErrorCodes.CODE_3));
        }

        @Test
        void not_matches() {
            ContextedRuntimeException exception = new ContextedRuntimeException(MyErrorCodes.CODE_3);
            assertThrows(AssertionError.class, () -> assertThat(exception, hasErrorCode(MyErrorCodes.CODE_1)));
        }
    }

    @Test
    void exceptionTesting2() {

        ContextedRuntimeException exception = assertThrows(ContextedRuntimeException.class,
                () -> new ThrowsException().doSomething());

        assertThat(exception.getErrorCodes().collect(Collectors.toList()),
                hasItems(ErrorCodeMatchers.equalTo(ErrorCode::equalsTo, MyErrorCodes.CODE_1),
                        ErrorCodeMatchers.equalTo(ErrorCode::equalsTo, MyErrorCodes.CODE_3)));

    }

    @Test
    void exceptionTesting3() {

        ContextedRuntimeException exception = assertThrows(ContextedRuntimeException.class,
                () -> new ThrowsException().doSomething());

        assertThat(exception.getErrorCodes().collect(Collectors.toList()),
                ErrorCodeMatchers.hasItems(ErrorCode::equalsTo, MyErrorCodes.CODE_1, MyErrorCodes.CODE_3));

    }
}
