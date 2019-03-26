 package io.github.leoniedermeier.utils.web.advice;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

/**
 * @see org.springframework.web.bind.annotation.ResponseStatus
 *
 *      Kann an einen Feld hängen (enum)
 */
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpResponseStatus {

    HttpStatus value();

}
