package io.github.leoniedermeier.utils.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.github.leoniedermeier.utils.jackson.Model.Bicycle;
import io.github.leoniedermeier.utils.jackson.Model.Car;
import io.github.leoniedermeier.utils.jackson.Model.Vehicle;
import io.github.leoniedermeier.utils.jackson.Model.Wrapper;

class JacksonSubtypesTest {

    @Test
    void withClassgraph() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        List<Class<?>> types = findJsonTypesWithClassGraph("io.github.leoniedermeier.utils");
        mapper.registerSubtypes(types);

        Wrapper wrapper = createData();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(wrapper);
        Wrapper result = mapper.readValue(json, Wrapper.class);

        checkResult(result);

    }

    @Test
    void withSpring() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        List<Class<?>> types = findJsonTypesWithSpring("io.github.leoniedermeier.utils");
        mapper.registerSubtypes(types);

        Wrapper wrapper = createData();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(wrapper);
        Wrapper result = mapper.readValue(json, Wrapper.class);

        checkResult(result);

    }

    private void checkResult(Wrapper result) {
        assertTrue(result.vehicles.length == 3);
        assertTrue(result.vehicles[1] instanceof Car);
        assertEquals("99", ((Car) result.vehicles[1]).horsePower);
    }

    private Wrapper createData() {
        Car car = new Car();
        car.name = "myCar";
        car.horsePower = "99";

        Vehicle vehilcle = new Vehicle();
        vehilcle.name = "myVehicle";

        Bicycle bicycle = new Bicycle();
        bicycle.name = "myBicycle";
        bicycle.color = "red";

        Wrapper wrapper = new Wrapper();
        wrapper.vehicles = new Vehicle[] { vehilcle, car, bicycle };
        return wrapper;
    }

    private List<Class<?>> findJsonTypesWithSpring(String basePackage) {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(JsonTypeName.class));
        return provider.findCandidateComponents(basePackage).stream().map(beanDefinition -> {
            try {
                return Class.forName(beanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException("Class not found " + beanDefinition.getBeanClassName(), e);
            }
        }).collect(Collectors.toList());
    }

    private List<Class<?>> findJsonTypesWithClassGraph(String basePackage) {
        // https://github.com/classgraph/classgraph
        try (ScanResult scanResult = new ClassGraph().enableClassInfo().enableAnnotationInfo().ignoreClassVisibility()
                .whitelistPackages(basePackage).scan()) {
            ClassInfoList classInfoList = scanResult.getClassesWithAnnotation(JsonTypeName.class.getName());
            return classInfoList.loadClasses();
        }
    }
}
