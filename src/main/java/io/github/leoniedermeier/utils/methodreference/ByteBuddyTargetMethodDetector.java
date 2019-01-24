package io.github.leoniedermeier.utils.methodreference;

import static io.github.leoniedermeier.utils.methodreference.DefaultValues.defaultValue;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.not;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

public final class ByteBuddyTargetMethodDetector {

	public interface MethodCapturer {

		Method getMethod();

		void setMethod(Method method);
	}

	public static class MethodNameCapturingInterceptor {

		@RuntimeType
		public static Object intercept(@net.bytebuddy.implementation.bind.annotation.This MethodCapturer capturer,
				@Origin Method method) {
			capturer.setMethod(method);
			return defaultValue(method.getReturnType());
		}
	}

	public static <T, U> Method resolve(Class<T> type, BiConsumer<? super T, U> method) {
		return resolve(type,
				// aus einem BiConsumer wird ein Consumer
				p -> method.accept(p, null));
	}

	public static <T> Method resolve(Class<T> type, Consumer<? super T> method) {
		T proxy = getPropertyNameCapturer(type);
		method.accept(proxy);
		return ((MethodCapturer) proxy).getMethod();
	}

	private static <T> T /* & MethodCapturer */ getPropertyNameCapturer(Class<T> type) {

		Class<? extends T> proxyType = new ByteBuddy().subclass(type)
				.method(not(isDeclaredBy(Object.class)).and(not(isDeclaredBy(MethodCapturer.class))))
				.intercept(MethodDelegation.to(MethodNameCapturingInterceptor.class))

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

	public <T, P> void validateValue(Class<T> type, Function<? super T, P> property, P value) {
	}
}
