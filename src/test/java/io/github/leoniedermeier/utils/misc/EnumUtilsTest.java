package io.github.leoniedermeier.utils.misc;

import static java.lang.annotation.ElementType.FIELD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EnumUtilsTest {

    @Nested
    static class GetAnnotation {
        @Retention(RetentionPolicy.RUNTIME)
        @Target(value = { FIELD })
        public @interface MyAnnotation {
            String value();
        }

        enum MyEnum {
            @MyAnnotation("123")
            WITH_ANNOTATION,

            WITHOUT_ANNOTATION;
        }

        @Test
        void with_annotation() {
            MyAnnotation annotation = EnumUtils.getAnnotation(MyEnum.WITH_ANNOTATION, MyAnnotation.class );
            assertNotNull(annotation);
            assertEquals("123", annotation.value());
        }
        
        @Test
        void without_annotation() {
            MyAnnotation annotation = EnumUtils.getAnnotation(MyEnum.WITHOUT_ANNOTATION, MyAnnotation.class );
            assertNull(annotation);
        }
        @Test
        void with_other_annotation() {
            Deprecated annotation = EnumUtils.getAnnotation(MyEnum.WITHOUT_ANNOTATION, Deprecated.class );
            assertNull(annotation);
        }
    }

}
