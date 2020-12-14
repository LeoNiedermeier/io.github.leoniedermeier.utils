package io.github.leoniedermeier.utils.test.web.advice;

import static io.github.leoniedermeier.utils.web.advice.RestExceptionHandler.HEADER_ERROR_CODES;

import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import io.github.leoniedermeier.utils.excecption.ErrorCode;
import io.github.leoniedermeier.utils.web.advice.RestExceptionHandler;

public final class HeaderErrorCodeMockMvcResultMatchers {

    /**
     * Builds a {@link ResultMatcher} for the string value of the header field
     * {@link RestExceptionHandler#HEADER_ERROR_CODES}.
     * 
     * @param errorCode The expected {@link ErrorCode}.
     * @return The {@link ResultMatcher}.
     */
    public static ResultMatcher headerErrorCode(ErrorCode errorCode) {
        return MockMvcResultMatchers.header().stringValues(HEADER_ERROR_CODES, errorCode.code());
    }

    private HeaderErrorCodeMockMvcResultMatchers() {
        throw new AssertionError("No HeaderErrorCodeMockMvcResultMatchers instances for you!");
    }
}
