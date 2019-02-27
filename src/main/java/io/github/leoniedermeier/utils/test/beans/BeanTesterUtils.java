package io.github.leoniedermeier.utils.test.beans;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.function.Supplier;

import org.opentest4j.IncompleteExecutionException;

final class BeanTesterUtils {

    private static final Random RANDOM = new Random();

    private static Map<Class<?>, Supplier<Object>> SUPPLIERS = new HashMap<>();

    static {
        SUPPLIERS.put(Integer.TYPE, () -> Integer.valueOf(RANDOM.nextInt()));
        SUPPLIERS.put(Long.TYPE, () -> Long.valueOf(RANDOM.nextLong()));
        SUPPLIERS.put(Boolean.TYPE, () -> Boolean.valueOf(RANDOM.nextBoolean()));
        SUPPLIERS.put(Double.TYPE, () -> Double.valueOf(RANDOM.nextDouble()));
        SUPPLIERS.put(Float.TYPE, () -> Float.valueOf(RANDOM.nextFloat()));
        SUPPLIERS.put(Byte.TYPE, () -> Byte.valueOf((byte) RANDOM.nextInt(Byte.MAX_VALUE)));
        SUPPLIERS.put(Short.TYPE, () -> Short.valueOf((short) RANDOM.nextInt(Short.MAX_VALUE)));
        SUPPLIERS.put(Character.TYPE, () -> Character.valueOf((char) (RANDOM.nextInt('z' - 'a') + 'a')));
        SUPPLIERS.put(String.class, () -> UUID.randomUUID().toString());
    }

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
        Supplier<Object> supplier = SUPPLIERS.get(cls);
        if (supplier != null) {
            return (T) supplier.get();
        }
        return newInstance(cls);
    }

    private BeanTesterUtils() {
        throw new AssertionError("No instances for you!");
    }

}
