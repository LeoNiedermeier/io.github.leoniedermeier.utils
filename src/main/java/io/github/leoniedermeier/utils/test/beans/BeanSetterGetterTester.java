package io.github.leoniedermeier.utils.test.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import io.github.leoniedermeier.utils.methodreference.ByteBuddyTargetMethodDetector;

public class BeanSetterGetterTester {

    public static <B, T> void assertSetterGetter(Class<B> beanClass, BiConsumer<B, T> setter, Function<B, T> getter) {

        // oder mit
        // https://github.com/jhalterman/typetools/blob/master/src/main/java/net/jodah/typetools/TypeResolver.java

        // Target methods are also useful for error message.
        Method getMethod = ByteBuddyTargetMethodDetector.resolve(beanClass, getter::apply);

        @SuppressWarnings("unchecked")
        Class<T> getMethodReturnType = (Class<T>) getMethod.getReturnType();
        T inputValue = BeanTesterUtils.randomValue(getMethodReturnType);

        B bean = BeanTesterUtils.newInstance(beanClass);

        setter.accept(bean, inputValue);

        T outputValue = getter.apply(bean);

        Supplier<String> message = () -> {
            Method setMethod = ByteBuddyTargetMethodDetector.resolve(beanClass, b -> setter.accept(b, inputValue));
            return "Reason: Setter - Getter different values!" + "\nset-method: " + setMethod.getName()
                    + "\nget-method: " + getMethod.getName();
        };

        if (getMethodReturnType.isPrimitive()) {
            assertEquals(inputValue, outputValue, message);
        } else {
            assertSame(inputValue, outputValue, message);
        }
    }

    private BeanSetterGetterTester() {
        throw new AssertionError("No BeanSetterGetterTester instances for you!");
    }
}
