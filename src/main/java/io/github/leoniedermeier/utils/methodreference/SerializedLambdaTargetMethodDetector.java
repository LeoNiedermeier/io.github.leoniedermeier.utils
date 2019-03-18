package io.github.leoniedermeier.utils.methodreference;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class SerializedLambdaTargetMethodDetector {

    public interface SerializableBiConsumer<T, U> extends BiConsumer<T, U>, Serializable {

    }

    public interface SerializableConsumer<T> extends Consumer<T>, Serializable {

    }

    public static <T, U> Method resolve(SerializableBiConsumer<T, U> method) {
        return method(method);
    }

    public static <T> Method resolve(SerializableConsumer<T> method) {

        return method(method);
    }

    public static <T> String resolveMethodName(SerializableConsumer<T> method) {

        return methodName(method);
    }

    private static Method findWriteReplaceMethod(Serializable lambda) {
        for (Class<?> clazz = lambda.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            Method[] declaredMethods = clazz.getDeclaredMethods();
            for (Method method : declaredMethods) {
                if ("writeReplace".equals(method.getName())) {
                    return method;
                }
            }
        }

        throw new IllegalStateException("writeReplace method not found");
    }

    private static Class<?> getLambdaImplClass(SerializedLambda lambda) {
        try {
            String className = lambda.getImplClass().replace('/', '.');
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    private static Method method(Serializable target) {
        SerializedLambda lambda = serialized(target);
        Class<?> lambdaImplClass = getLambdaImplClass(lambda);

        String implMethodName = lambda.getImplMethodName();
        List<Method> candidates = Arrays.asList(lambdaImplClass.getDeclaredMethods()).stream()
                .filter(method -> Objects.equals(method.getName(), implMethodName)).collect(Collectors.toList());

        if (candidates.size() > 1) {
            throw new IllegalStateException("Multiple methods with id '" + implMethodName + "' found in class '"
                    + lambdaImplClass + "'. Currently no overload resolution.");
        } else if (candidates.isEmpty()) {
            throw new IllegalStateException(
                    "No method with id '" + implMethodName + "' found in class '" + lambdaImplClass + "'.");
        } else {
            return candidates.get(0);
        }
    }

    private static String methodName(Serializable target) {
        SerializedLambda lambda = serialized(target);
        return lambda.getImplMethodName();
    }

    private static SerializedLambda serialized(Serializable lambda) {

        try {
            Method method = findWriteReplaceMethod(lambda);
            method.setAccessible(true);
            Object serializedForm = method.invoke(lambda);
            return (SerializedLambda) serializedForm;
        } catch (SecurityException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Unable to cleanly serialize lambda", e);
        }

    }

    private SerializedLambdaTargetMethodDetector() {
        throw new AssertionError("No SerializedLambdaTargetMethodDetector instances for you!");
    }

}
