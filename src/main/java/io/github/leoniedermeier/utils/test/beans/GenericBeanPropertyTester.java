package io.github.leoniedermeier.utils.test.beans;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.api.function.Executable;
import org.opentest4j.IncompleteExecutionException;

// ignored by sonar due to entry in pom.xml
// because throws exception when analyzing.
public final class GenericBeanPropertyTester {

    public static <B> void testAllSettersGetters(Class<B> beanClass) {
        final BeanInfo beanInfo = getBeanInfo(beanClass);

        Function<PropertyDescriptor, Executable> toExecutable = propertyDescriptor -> {
            return () -> {
                checkMethodsAvailable(propertyDescriptor);

                // Test a setter and getter invocation: should result in same value
                // then getter and setters are ok
                B bean = BeanTesterUtils.newInstance(beanClass);
                Object inputValue = BeanTesterUtils.randomValue(propertyDescriptor.getPropertyType());
                invoke(propertyDescriptor.getWriteMethod(), bean, inputValue);

                Object outputValue = invoke(propertyDescriptor.getReadMethod(), bean);

                Supplier<String> message = () -> "\nproperty: " + propertyDescriptor.getName() //
                        + "\nset-method: " + propertyDescriptor.getWriteMethod().getName() //
                        + "\nget-method: " + propertyDescriptor.getReadMethod().getName() + "\n";

                // Due to autoboxing primitives results in different instances,
                if (propertyDescriptor.getPropertyType().isPrimitive()) {
                    assertEquals(inputValue, outputValue, message);
                } else {
                    assertSame(inputValue, outputValue, message);
                }
            };
        };
        assertAll("Test properties getter and setters", Stream.of(beanInfo.getPropertyDescriptors())
                .filter(GenericBeanPropertyTester::isNotObjectProperty).map(toExecutable));
    }

    private static void checkMethodsAvailable(PropertyDescriptor propertyDescriptor) {
        if (propertyDescriptor.getReadMethod() == null) {
            fail("No set-method for property: " + propertyDescriptor.getDisplayName());
        }

        if (propertyDescriptor.getWriteMethod() == null) {
            fail("No get-method for property: " + propertyDescriptor.getDisplayName());
        }
    }

    private static <B> BeanInfo getBeanInfo(Class<B> beanClass) {
        try {
            return Introspector.getBeanInfo(beanClass);
        } catch (IntrospectionException e) {
            throw new IncompleteExecutionException("Can not introspect bean of class " + beanClass, e);
        }
    }

    private static Object invoke(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IncompleteExecutionException("Can not invoke method " + method.getName(), e);
        }
    }

    private static boolean isNotObjectProperty(PropertyDescriptor propertyDescriptor) {
        return !((propertyDescriptor.getWriteMethod() != null
                && propertyDescriptor.getWriteMethod().getDeclaringClass() == Object.class)
                || (propertyDescriptor.getReadMethod() != null
                        && propertyDescriptor.getReadMethod().getDeclaringClass() == Object.class));
    }

    private GenericBeanPropertyTester() {
        throw new AssertionError("No instances for you!");
    }
}
