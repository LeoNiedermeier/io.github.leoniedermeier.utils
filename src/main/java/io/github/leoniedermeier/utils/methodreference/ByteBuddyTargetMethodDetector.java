package io.github.leoniedermeier.utils.methodreference;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.not;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.objenesis.ObjenesisHelper;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.modifier.Visibility;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FieldAccessor;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.StubValue;
import net.bytebuddy.implementation.bind.annotation.This;

/**
 * Bytebuddy und Objensis kommen mit mockito mit.
 */
public final class ByteBuddyTargetMethodDetector {

	public interface MethodCapturer {

		Method getMethod();

		void setMethod(Method method);
	}

	public static class MethodCapturingInterceptor {

		@RuntimeType
		public static Object intercept(@This MethodCapturer capturer, @Origin Method method,
				@StubValue Object stubValue) {
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
				.intercept(MethodDelegation.to(MethodCapturingInterceptor.class))

				// a new field in the class, where the interceptor can store the called method.
				.defineField("method", Method.class, Visibility.PRIVATE)//
				.implement(MethodCapturer.class).intercept(FieldAccessor.ofBeanProperty())

				.make().load(type.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER).getLoaded();
		return ObjenesisHelper.newInstance(proxyType);
	}

	private ByteBuddyTargetMethodDetector() {
		throw new AssertionError("No ByteBuddyTargetMethodDetector instances for you!");
	}
}
