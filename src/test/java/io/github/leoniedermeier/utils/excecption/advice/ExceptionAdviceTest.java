package io.github.leoniedermeier.utils.excecption.advice;

import static io.github.leoniedermeier.utils.test.exception.ContextedRuntimeExceptionAssertions.assertThrowsContextedRuntimeException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import io.github.leoniedermeier.utils.excecption.EnumErrorCode;

@SpringBootTest(classes = { ApplicationForException.class, ExceptionAdviceTest.MyService.class })
class ExceptionAdviceTest {

   private enum MyErrorCodes implements EnumErrorCode {
        MY_ERROR_CODE;
        
        static final String XYZ ="MY_ERROR_CODE";
    }
    @Component
    public static class MyService {

        @ExceptionTranslation( MyErrorCodes.XYZ)
        public String throwExceptionWithAdvice() {
            throw new IllegalArgumentException("THROW EXCEPTION");
        }

        public String throwExceptionWithoutAdvice() {
            throw new IllegalArgumentException("THROW EXCEPTION");
        }
    }

    @Autowired
    MyService myService;

    @Test
    void withAdvice() {

        assertThrowsContextedRuntimeException("MY_ERROR_CODE", () -> myService.throwExceptionWithAdvice());
    }

    @Test
    void withoutAdvice() {

        assertThrows(IllegalArgumentException.class, () -> myService.throwExceptionWithoutAdvice());
    }
}
