package io.github.leoniedermeier.utils.jackson;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.leoniedermeier.utils.jackson.Model.Bicycle;
import io.github.leoniedermeier.utils.jackson.Model.Car;
import io.github.leoniedermeier.utils.jackson.Model.Vehicle;
import io.github.leoniedermeier.utils.jackson.Model.Wrapper;

class JacksonSubtypesUtilsTest {

    @Test
    void withClassgraph() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        List<Class<?>> types = JacksonSubtypesUtils.findJsonTypesWithClassGraph(Wrapper.class.getPackage().getName());
        mapper.registerSubtypes(types);

        Wrapper wrapper = createData();
        String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(wrapper);
        Wrapper result = mapper.readValue(json, Wrapper.class);

        checkResult(result);

    }

    @Test
    void withSpring() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        List<Class<?>> types = JacksonSubtypesUtils.findJsonTypesWithSpring(Wrapper.class.getPackage().getName());
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
}
