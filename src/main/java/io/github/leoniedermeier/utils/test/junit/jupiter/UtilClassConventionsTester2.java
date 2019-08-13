package io.github.leoniedermeier.utils.test.junit.jupiter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Map;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Conventions of Utils class")
@SuppressWarnings("squid:S2326")
// THE UTIL_CLASS type parameter is accessed by reflection
public interface UtilClassConventionsTester2<UTIL_CLASS> {
    
    default Class<?> getClassUnderTest() {
        Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(getClass(), UtilClassConventionsTester2.class);
        return (Class<?>) typeArguments.get(UtilClassConventionsTester2.class.getTypeParameters()[0]);
    }

    @Test
    default void hasOnlyOnePrivateConstructor() {
        Class<?> clazz = getClassUnderTest();
        final Constructor<?>[] cons = clazz.getDeclaredConstructors();
        assertEquals(1, cons.length, "Class " + clazz + "has more than one constuctor.");
        assertTrue(Modifier.isPrivate(cons[0].getModifiers()), "Constuctor of class " + clazz + "is not private.");
    }

    @Test
    default void isFinalClass() {
        Class<?> clazz = getClassUnderTest();
        assertTrue(Modifier.isFinal(clazz.getModifiers()), "Class " + clazz + " is not final.");
    }

    @Test
    default void isPublicClass() {
        Class<?> clazz = getClassUnderTest();
        assertTrue(Modifier.isPublic(clazz.getModifiers()), "Class " + clazz + " is not public.");
    }
}