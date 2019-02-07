package io.github.leoniedermeier.utils.test.beans;

import java.util.Random;
import java.util.UUID;

import org.opentest4j.IncompleteExecutionException;

final class BeanTesterUtils {

	private static final Random random = new Random();

	static <T> T newInstance(Class<T> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IncompleteExecutionException(
					"Can not create instance of class" + clazz + "by default constructor. ", e);
		}
	}
	
	@SuppressWarnings("unchecked")
	static <T> T randomValue(Class<T> cls) {
		if (cls == Integer.TYPE) {
			return (T) Integer.valueOf(random.nextInt());
		}
		if (cls == Long.TYPE) {
			return (T) Long.valueOf(random.nextLong());
		}
		if (cls == Boolean.TYPE) {
			return (T) Boolean.valueOf(random.nextBoolean());
		}
		if (cls == Double.TYPE) {
			return (T) Double.valueOf(random.nextDouble());
		}
		if (cls == Float.TYPE) {
			return (T) Float.valueOf(0.0f);
		}
		if (cls == Byte.TYPE) {
			return (T) Byte.valueOf((byte) random.nextInt(Byte.MAX_VALUE));
		}
		if (cls == Short.TYPE) {
			return (T) Short.valueOf((short) random.nextInt(Short.MAX_VALUE));
		}
		if (cls == Character.TYPE) {
			return (T) Character.valueOf((char) (random.nextInt('z' - 'a') + 'a'));
		}
		if (cls == String.class) {
			// random String
			return (T) UUID.randomUUID().toString();
		}
		return newInstance(cls);
	}

	private BeanTesterUtils() {
		throw new AssertionError("No instances for you!");
	}

}
