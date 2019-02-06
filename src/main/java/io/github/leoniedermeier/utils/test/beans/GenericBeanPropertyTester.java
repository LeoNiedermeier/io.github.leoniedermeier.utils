package io.github.leoniedermeier.utils.test.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.opentest4j.AssertionFailedError;
import org.opentest4j.IncompleteExecutionException;
import org.opentest4j.MultipleFailuresError;

public final class GenericBeanPropertyTester {

	public static <B> void assertAllSettersGetters(Class<B> beanClass) {
		final BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(beanClass);
		} catch (IntrospectionException e) {
			throw new IncompleteExecutionException("Can not introspect bean of class " + beanClass, e);
		}
		List<AssertionFailedError> errorrs = new ArrayList<>();
		List<PropertyDescriptor> candidates = new ArrayList<>();

		for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
			if (isObjectProperty(propertyDescriptor)) {
				continue;
			}
			if (hasReadAndWriteMethods(propertyDescriptor)) {
				candidates.add(propertyDescriptor);
			} else {
				errorrs.add(new AssertionFailedError("Property: " + propertyDescriptor.getDisplayName()
						+ "   Reason: No matching read and write methods!"));
			}
		}

		// Test the getter / setter
		B bean = BeanTesterUtils.newInstance(beanClass);
		for (PropertyDescriptor propertyDescriptor : candidates) {
			Object inputValue = BeanTesterUtils.randomValue(propertyDescriptor.getPropertyType());
			invoke(propertyDescriptor.getWriteMethod(), bean, inputValue);

			Object outputValue = invoke(propertyDescriptor.getReadMethod(), bean);
			// Due to autoboxing primitives results in different instances,
			if (propertyDescriptor.getPropertyType().isPrimitive() ? !Objects.equals(inputValue, outputValue)
					: inputValue != outputValue) {

				errorrs.add(new AssertionFailedError("Property: '" + propertyDescriptor.getDisplayName()
						+ "' -- Reason: Setter - Getter different values!", inputValue, outputValue));
			}
		}

		if (!errorrs.isEmpty()) {
			throw new MultipleFailuresError("Bean property tests failed!", errorrs);
		}
	}

	private static Object invoke(Method method, Object target, Object... args) {
		try {
			return method.invoke(target, args);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new IncompleteExecutionException("ABC", e);
		}
	}

	private static boolean hasReadAndWriteMethods(PropertyDescriptor propertyDescriptor) {
		return propertyDescriptor.getWriteMethod() != null
				&& propertyDescriptor.getWriteMethod().getParameterCount() == 1
				&& propertyDescriptor.getReadMethod() != null
				&& propertyDescriptor.getReadMethod().getParameterCount() == 0;
	}

	private static boolean isObjectProperty(PropertyDescriptor propertyDescriptor) {
		return (propertyDescriptor.getWriteMethod() != null
				&& propertyDescriptor.getWriteMethod().getDeclaringClass() == Object.class)
				|| (propertyDescriptor.getReadMethod() != null
						&& propertyDescriptor.getReadMethod().getDeclaringClass() == Object.class);
	}

	private GenericBeanPropertyTester() {
		throw new AssertionError("No instances for you!");
	}
}
