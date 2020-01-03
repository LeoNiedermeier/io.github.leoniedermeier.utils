package io.github.leoniedermeier.utils.misc;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.reflect.TypeUtils;
import org.junit.jupiter.api.Test;

class TypeUtilsExamplesTest {

    // @Nested
    static class DetermineTypeParameters {

        interface MyInterface extends Runnable, Supplier<String>, BiFunction<Integer, List<String>, Supplier<String>> {

        }

        @Test
        void test() {
            Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(MyInterface.class, Supplier.class);
            System.out.println(typeArguments);
            TypeVariable<Class<Supplier>> x = Supplier.class.getTypeParameters()[0];
            System.out.println(x);
            Type type = typeArguments.get(x);
            System.out.println(type);

            System.out.println(getTypes(MyInterface.class, BiFunction.class));

        }
    }

    public static List<Type> getTypes(final Type type, final Class<?> toClass) {
        Map<TypeVariable<?>, Type> typeArguments = TypeUtils.getTypeArguments(type, toClass);
        return Stream.of(toClass.getTypeParameters()).map(typeArguments::get).collect(Collectors.toList());
    }

}
