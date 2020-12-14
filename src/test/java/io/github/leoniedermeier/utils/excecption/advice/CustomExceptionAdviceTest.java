package io.github.leoniedermeier.utils.excecption.advice;

import static io.github.leoniedermeier.utils.test.exception.ContextedRuntimeExceptionAssertions.assertThrowsContextedRuntimeException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import io.github.leoniedermeier.utils.excecption.ContextedRuntimeException;
import io.github.leoniedermeier.utils.excecption.EnumErrorCode;
import io.github.leoniedermeier.utils.excecption.ErrorCode;

@SpringBootTest(classes = { ApplicationForException.class, CustomExceptionAdviceTest.MyService.class,
        MyExceptionTranslationAdvice.class })
class CustomExceptionAdviceTest {


    @Component
    public static class MyService {

        @MyExceptionTranslation( MyErrorCodes.MY_ERROR_CODE)
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

        assertThrowsContextedRuntimeException(MyErrorCodes.MY_ERROR_CODE, () -> myService.throwExceptionWithAdvice());
    }

    @Test
    void withoutAdvice() {

        assertThrows(IllegalArgumentException.class, () -> myService.throwExceptionWithoutAdvice());
    }

}

 enum MyErrorCodes implements EnumErrorCode {
    MY_ERROR_CODE;

}
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface MyExceptionTranslation {
    MyErrorCodes value();
}

@Aspect
@Component
 class MyExceptionTranslationAdvice {

    @AfterThrowing(
            pointcut = "execution(@io.github.leoniedermeier.utils.excecption.advice.MyExceptionTranslation * *(..))  && @annotation(exceptionTranslation)",
            throwing = "ex")
    public void translateException(MyExceptionTranslation exceptionTranslation, Exception ex) {
        throw ContextedRuntimeException.castOrCreate(exceptionTranslation.value(), ex);
    }
}
