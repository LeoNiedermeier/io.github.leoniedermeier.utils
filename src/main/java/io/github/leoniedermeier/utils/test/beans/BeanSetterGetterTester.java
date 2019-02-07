package io.github.leoniedermeier.utils.test.beans;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import org.opentest4j.AssertionFailedError;

import io.github.leoniedermeier.utils.methodreference.ByteBuddyTargetMethodDetector;

public class BeanSetterGetterTester {

	private BeanSetterGetterTester() {
		throw new AssertionError("No instances for you!");
	}

	public static <B, T> void assertSetterGetter(Class<B> beanClass, BiConsumer<B, T> setter, Function<B, T> getter) {

// https://github.com/jhalterman/typetools/blob/master/src/main/java/net/jodah/typetools/TypeResolver.java
//		Class<?>[] resolveRawArguments = TypeResolver.resolveRawArguments(BiConsumer.class, setter.getClass());
//		Class<?> setterArgumentType = resolveRawArguments[1];
//		Object inputValue = randomValue(setterArgumentType);

		// Target methods are also useful for error message.
		Method setMethod = ByteBuddyTargetMethodDetector.resolve(beanClass,
				(Consumer<B>) b -> setter.accept(b, null));

		Class<T> setMethodParameterType = (Class<T>) setMethod.getParameters()[0].getType();
		T inputValue = BeanTesterUtils.randomValue(setMethodParameterType);

		B bean = BeanTesterUtils.newInstance(beanClass);

		setter.accept(bean, inputValue);

		T outputValue = getter.apply(bean);
		if (setMethodParameterType.isPrimitive() ? !Objects.equals(inputValue, outputValue)
				: inputValue != outputValue) {

			Method getMethod = ByteBuddyTargetMethodDetector.resolve(beanClass,
					(Consumer<B>) b -> getter.apply(b));
			String message = "Reason: Setter - Getter different values!" + "\nset-method: " + setMethod.getName()
					+ "\nget-method: " + getMethod.getName();
			throw new AssertionFailedError(message, inputValue, outputValue);
		}
	}
}
