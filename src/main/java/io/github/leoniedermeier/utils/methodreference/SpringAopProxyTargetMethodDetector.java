package io.github.leoniedermeier.utils.methodreference;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.DefaultAopProxyFactory;

public final class SpringAopProxyTargetMethodDetector {

	private static class TargetMethodSavingMethodInterceptor implements MethodInterceptor {

		private Method method;

		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			method = invocation.getMethod();
			return defaultValue(method.getReturnType());
		}
	}

	public static <T, U> Method resolve(Class<T> type, BiConsumer<? super T, U> method) {
		return resolve(type,
				// aus einem BiConsumer wird ein Consumer
				p -> method.accept(p, null));
	}

	public static <T> Method resolve(Class<T> type, Consumer<? super T> method) {

		AdvisedSupport config = new AdvisedSupport();
		config.setTargetClass(type);

		TargetMethodSavingMethodInterceptor mi = new TargetMethodSavingMethodInterceptor();
		config.addAdvice(mi);
		DefaultAopProxyFactory defaultAopProxyFactory = new DefaultAopProxyFactory();
		AopProxy aopProxy = defaultAopProxyFactory.createAopProxy(config);

		@SuppressWarnings("unchecked")
		T proxy = (T) aopProxy.getProxy(type.getClassLoader());

		method.accept(proxy);
		return mi.method;
	}

	private SpringAopProxyTargetMethodDetector() {
		throw new AssertionError("No SpringAopProxyTargetMethodDetector instances for you!");
	}
	
	private static Object defaultValue(Class<?> cls) {
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
