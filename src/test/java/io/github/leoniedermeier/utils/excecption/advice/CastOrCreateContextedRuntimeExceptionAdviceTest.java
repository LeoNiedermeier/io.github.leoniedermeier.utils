package io.github.leoniedermeier.utils.excecption.advice;

import static io.github.leoniedermeier.utils.test.exception.ContextedRuntimeExceptionAssertions.assertThrowsContextedRuntimeException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;

import io.github.leoniedermeier.utils.excecption.ContextedRuntimeException;

@SpringBootTest(classes = { ApplicationForException.class, CastOrCreateContextedRuntimeExceptionAdviceTest.MyService.class })
public class CastOrCreateContextedRuntimeExceptionAdviceTest {


    @Component
    public static class MyService {

        @CastOrCreateContextedRuntimeException("MY_ERROR_CODE")
        public String throwExceptionWithAdvice( @IgnoreParam String param1, String param2) {
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
       

       ContextedRuntimeException result = assertThrowsContextedRuntimeException("MY_ERROR_CODE", () -> myService.throwExceptionWithAdvice("paramValue1", "paramValue2"));
       assertEquals(0, result.getContextValues("param1").count());
       assertEquals(1, result.getContextValues("param2").count());
       
       assertEquals("paramValue2", result.getContextValues("param2").collect(Collectors.toList()).get(0));
   
    }

    @Test
    void withoutAdvice() {

        assertThrows(IllegalArgumentException.class, () -> myService.throwExceptionWithoutAdvice());
    }
}
