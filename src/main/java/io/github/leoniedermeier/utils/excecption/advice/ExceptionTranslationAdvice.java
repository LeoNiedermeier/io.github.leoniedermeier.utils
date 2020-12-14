package io.github.leoniedermeier.utils.excecption.advice;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import io.github.leoniedermeier.utils.excecption.ContextedRuntimeException;
import io.github.leoniedermeier.utils.excecption.ErrorCode;

@Aspect
@Component
public class ExceptionTranslationAdvice {

    @AfterThrowing(
            pointcut = "execution(@io.github.leoniedermeier.utils.excecption.advice.ExceptionTranslation * *(..))  && @annotation(exceptionTranslation)",
            throwing = "ex")
    public void translateException(ExceptionTranslation exceptionTranslation, Exception ex) {
        throw ContextedRuntimeException.castOrCreate(new StringErrorCode(exceptionTranslation.value()), ex);
    }

    private class StringErrorCode implements ErrorCode {

        private final String errorCode;

        public StringErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        @Override
        public String code() {
            return errorCode;
        }
    }
}