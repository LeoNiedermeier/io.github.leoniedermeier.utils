package io.github.leoniedermeier.utils.test.exception;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.function.Executable;
import org.opentest4j.AssertionFailedError;

import io.github.leoniedermeier.utils.excecption.ContextedRuntimeException;
import io.github.leoniedermeier.utils.excecption.ErrorCode;

public final class ContextedRuntimeExceptionAssertions {

    /**
     * Custom assert method checking whether the given
     * {@link ContextedRuntimeException} has the given {@link ErrorCode}.
     * 
     * @param expectedErrorCode The expected {@link ErrorCode}.
     * @param exception         The {@link ContextedRuntimeException} to test.
     */
    public static void assertExceptionHasErrorCode(ErrorCode expectedErrorCode, ContextedRuntimeException exception) {
       assertExceptionHasErrorCode(expectedErrorCode.code(), exception);
    }
    
    /**
     * Custom assert method checking whether the given
     * {@link ContextedRuntimeException} has the given error code.
     * 
     * @param expectedErrorCode The expected error code.
     * @param exception         The {@link ContextedRuntimeException} to test.
     */
    public static void assertExceptionHasErrorCode(String expectedErrorCode, ContextedRuntimeException exception) {
        boolean noneMatch = exception.getErrorCodes().map(ErrorCode::code).noneMatch(expectedErrorCode::equals);
        if (noneMatch) {
            List<String> codes = exception.getErrorCodes().map(ErrorCode::code).collect(toList());
            throw new AssertionFailedError("Exception does not contain errorcode " + expectedErrorCode
                    + "\navailable error codes: " + codes, expectedErrorCode, codes);
        }
    }

    /**
     * Custom assert method checking for {@link ContextedRuntimeException} and
     * {@link ErrorCode}.
     * 
     * @param expectedErrorCode The expected {@link ErrorCode}.
     * @param executable        The {@link Executable}.
     * @return The thrown {@link ContextedRuntimeException}.
     * @see org.junit.jupiter.api.Assertions#assertThrows
     */
    public static ContextedRuntimeException assertThrowsContextedRuntimeException(ErrorCode expectedErrorCode,
            Executable executable) {
        ContextedRuntimeException exception = assertThrows(ContextedRuntimeException.class, executable);
        assertExceptionHasErrorCode(expectedErrorCode, exception);
        return exception;
    }
    
    /**
     * Custom assert method checking for {@link ContextedRuntimeException} and
     * error code.
     * 
     * @param expectedErrorCode The expected error code.
     * @param executable        The {@link Executable}.
     * @return The thrown {@link ContextedRuntimeException}.
     * @see org.junit.jupiter.api.Assertions#assertThrows
     */
    public static ContextedRuntimeException assertThrowsContextedRuntimeException(String expectedErrorCode,
            Executable executable) {
        ContextedRuntimeException exception = assertThrows(ContextedRuntimeException.class, executable);
        assertExceptionHasErrorCode(expectedErrorCode, exception);
        return exception;
    }

    private ContextedRuntimeExceptionAssertions() {
        throw new AssertionError("No ContextedRuntimeExceptionAssertions instances for you!");
    }
}
