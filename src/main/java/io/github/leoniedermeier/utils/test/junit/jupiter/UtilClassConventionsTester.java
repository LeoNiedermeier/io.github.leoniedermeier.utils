package io.github.leoniedermeier.utils.test.junit.jupiter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Conventions of Utils class")
@SuppressWarnings("squid:S2326")
// THE UTIL_CLASS type parameter is accessed by reflection
public interface UtilClassConventionsTester<UTIL_CLASS> {

    default Class<?> getClassUnderTest() {
        Class<?> clazz = getClass();
        ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericInterfaces()[0];
        Type[] typeArguments = parameterizedType.getActualTypeArguments();
        return (Class<?>) typeArguments[0];
    }

    @Test
    default void hasOnlyOnePrivateConstructor() {
        Class<?> clazz = getClassUnderTest();
        final Constructor<?>[] cons = clazz.getDeclaredConstructors();
        assertEquals(1, cons.length, "Class " + clazz + "has more than one constuctor.");
        assertTrue(Modifier.isPrivate(cons[0].getModifiers()), "Constuctor of class " + clazz + "is not private.");
    }

    @Test
    default void isPublicClass() {
        Class<?> clazz = getClassUnderTest();
        assertTrue(Modifier.isPublic(clazz.getModifiers()), "Class " + clazz + " is not public.");
    }

    @Test
    default void isFinalClass() {
        Class<?> clazz = getClassUnderTest();
        assertTrue(Modifier.isFinal(clazz.getModifiers()), "Class " + clazz + " is not final.");
    }
}