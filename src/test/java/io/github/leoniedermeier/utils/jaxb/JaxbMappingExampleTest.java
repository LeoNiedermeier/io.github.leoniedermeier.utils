package io.github.leoniedermeier.utils.jaxb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.jupiter.api.Test;

import io.github.leoniedermeier.utils.jaxb.Model.Bicycle;
import io.github.leoniedermeier.utils.jaxb.Model.Car;
import io.github.leoniedermeier.utils.jaxb.Model.Vehicle;
import io.github.leoniedermeier.utils.jaxb.Model.Wrapper;

class JaxbMappingExampleTest {

    @Test
    void reoundtrip() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(Wrapper.class, Vehicle.class, Bicycle.class, Car.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        marshaller.marshal(createData(), writer);
        String xml = writer.toString();
       // System.out.println(xml);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

        Wrapper result = (Wrapper) unmarshaller.unmarshal(new StringReader(xml));
        checkResult(result);
    }

    private void checkResult(Wrapper result) {
        assertEquals(3, result.vehicles.length);
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
        wrapper.vehicles = new Vehicle[] {vehilcle, car, bicycle };
        return wrapper;
    }
}
