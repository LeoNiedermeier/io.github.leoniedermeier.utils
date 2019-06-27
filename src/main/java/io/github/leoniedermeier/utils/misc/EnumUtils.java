package io.github.leoniedermeier.utils.misc;

import java.lang.annotation.Annotation;

public class EnumUtils {

    /**
     * Returns the enum constant's annotation for the specified type if such an
     * annotation is present, else null.
     * 
     * @param <T>          The type of the annotation.
     * @param enumConstant The enum constant from which to retrieve the annotation.
     * @param annotation   The annotation class.
     * @return The annotation or <code>null</code>, if the given enum constant has
     *         no annotation of the desired type.
     */
    public static <T extends Annotation> T getAnnotation(Enum<?> enumConstant, Class<T> annotation) {

        try {
            return enumConstant.getClass().getField(enumConstant.name()).getAnnotation(annotation);

        } catch (NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    private EnumUtils() {
        throw new AssertionError("No EnumUtils instances for you!");
    }
}
