package io.github.leoniedermeier.utils.test.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
		Method setMethod = ByteBuddyTargetMethodDetector.resolve(beanClass, (Consumer<B>) b -> setter.accept(b, null));

		@SuppressWarnings("unchecked")
		Class<T> setMethodParameterType = (Class<T>) setMethod.getParameters()[0].getType();
		T inputValue = BeanTesterUtils.randomValue(setMethodParameterType);

		B bean = BeanTesterUtils.newInstance(beanClass);

		setter.accept(bean, inputValue);

		T outputValue = getter.apply(bean);

		Supplier<String> message = () -> {
			Method getMethod = ByteBuddyTargetMethodDetector.resolve(beanClass, (Consumer<B>) b -> getter.apply(b));
			return "Reason: Setter - Getter different values!" + "\nset-method: " + setMethod.getName()
					+ "\nget-method: " + getMethod.getName();
		};
		
		if (setMethodParameterType.isPrimitive()) {
			assertEquals(inputValue, outputValue, message);
		} else {
			assertSame(inputValue, outputValue, message);
		}
	}
}
