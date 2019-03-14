package io.github.leoniedermeier.utils.methodreference;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.framework.DefaultAopProxyFactory;

public final class SpringAopProxyTargetMethodDetector {

    private static class TargetMethodSavingMethodInterceptor implements MethodInterceptor {
        private static Map<Class<?>, Object> DEFAULT_VALUES = new HashMap<>();

        static {
            DEFAULT_VALUES.put(Integer.TYPE, Integer.valueOf(0));
            DEFAULT_VALUES.put(Long.TYPE, Long.valueOf(0));
            DEFAULT_VALUES.put(Boolean.TYPE, Boolean.FALSE);
            DEFAULT_VALUES.put(Double.TYPE, Double.valueOf(0));
            DEFAULT_VALUES.put(Float.TYPE, Float.valueOf(0));
            DEFAULT_VALUES.put(Byte.TYPE, Byte.valueOf((byte) 0));
            DEFAULT_VALUES.put(Short.TYPE, Short.valueOf((short) 0));
            DEFAULT_VALUES.put(Character.TYPE, '\0');
        }

        private Method method;

        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            method = invocation.getMethod();
            return DEFAULT_VALUES.get(method.getReturnType());
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
}
