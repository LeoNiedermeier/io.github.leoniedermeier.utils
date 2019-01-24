package io.github.leoniedermeier.utils.methodreference;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.not;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.StubValue;

public final class ByteBuddyTargetMethodDetector {

	public interface MethodCapturer {

		Method getMethod();

		void setMethod(Method method);
	}

	public static class MethodNameCapturingInterceptor {

		@RuntimeType
		public static Object intercept(@net.bytebuddy.implementation.bind.annotation.This MethodCapturer capturer,
				@Origin Method method, @StubValue Object stubValue) {
			capturer.setMethod(method);
			return stubValue;
		}
	}

	public static <T, U> Method resolve(Class<T> type, BiConsumer<? super T, U> method) {
		return resolve(type,
				// aus einem BiConsumer wird ein Consumer
				p -> method.accept(p, null));
	}

	public static <T> Method resolve(Class<T> type, Consumer<? super T> method) {
		T proxy = createProxy(type);
		method.accept(proxy);
		return ((MethodCapturer) proxy).getMethod();
	}

	private static <T> T /* & MethodCapturer */ createProxy(Class<T> type) {

		Class<? extends T> proxyType = new ByteBuddy().subclass(type)
				// Select where to apply interceptor:
				.method(not(isDeclaredBy(Object.class)).and(not(isDeclaredBy(MethodCapturer.class))))
				.intercept(MethodDelegation.to(MethodNameCapturingInterceptor.class))
				
				// a new field in the class, where the interceptor can store the called method.
				.implement(MethodCapturer.class).defineField("method", Method.class, Visibility.PRIVATE)//
				.method(named("setMethod").or(named("getMethod"))).intercept(FieldAccessor.ofBeanProperty())

				.make().load(type.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();

		try {
			// only no argument constructors
			return proxyType.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException("Couldn't instantiate proxy for method name retrieval", e);
		}
	}

	private ByteBuddyTargetMethodDetector() {
		throw new AssertionError("No ByteBuddyTargetMethodDetector instances for you!");

	}
}
