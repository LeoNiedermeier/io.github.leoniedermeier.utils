package io.github.leoniedermeier.utils.methodreference;

final class DefaultValues {

	private DefaultValues() {
		throw new AssertionError("No DefaultValues instances for you!");
	}

	public static Object defaultValue(Class<?> cls) {
		if (cls == Integer.TYPE) {
			return Integer.valueOf(0);
		}
		if (cls == Long.TYPE) {
			return Long.valueOf(0L);
		}
		if (cls == Boolean.TYPE) {
			return Boolean.FALSE;
		}
		if (cls == Double.TYPE) {
			return Double.valueOf(0.0);
		}
		if (cls == Float.TYPE) {
			return Float.valueOf(0.0f);
		}
		if (cls == Byte.TYPE) {
			return Byte.valueOf((byte) 0);
		}
		if (cls == Short.TYPE) {
			return Short.valueOf((short) 0);
		}
		if (cls == Character.TYPE) {
			return '\0';
		}
		return null;
	}

}
