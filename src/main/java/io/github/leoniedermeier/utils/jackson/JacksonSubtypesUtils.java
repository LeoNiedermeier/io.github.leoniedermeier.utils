package io.github.leoniedermeier.utils.jackson;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

public class JacksonSubtypesUtils {

    public static List<Class<?>> findJsonTypesWithClassGraph(String basePackage) {
        // https://github.com/classgraph/classgraph
        try (ScanResult scanResult = new ClassGraph().enableClassInfo().enableAnnotationInfo().disableRuntimeInvisibleAnnotations().ignoreClassVisibility()
                .whitelistPackages(basePackage).scan()) {
            return scanResult.getClassesWithAnnotation(JsonTypeName.class.getName()).loadClasses();
        }
    }

    public static List<Class<?>> findJsonTypesWithSpring(String basePackage) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(JsonTypeName.class));
        return provider.findCandidateComponents(basePackage).stream().map(JacksonSubtypesUtils::classForName).collect(toList());
    }

    private static Class<?> classForName(BeanDefinition beanDefinition) {
        try {
            return Class.forName(beanDefinition.getBeanClassName());
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Class not found " + beanDefinition.getBeanClassName(), e);
        }
    }

    private JacksonSubtypesUtils() {
        throw new AssertionError("No JacksonSubtypesUtils instances for you!");
    }
}
