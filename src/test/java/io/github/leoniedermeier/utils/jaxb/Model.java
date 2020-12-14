package io.github.leoniedermeier.utils.jaxb;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

public class Model {

    @XmlRootElement(name = "wrapper", namespace = "http://www.w3.org/2001/XMLSchema-instance")
    public static class Wrapper {
        @XmlElementWrapper(name = "vehicles")
        @XmlElementRef
        public Vehicle[] vehicles;

    }

    @XmlRootElement(name = "vehicle")
    public static class Vehicle {
        public String name;
    }

    @XmlRootElement(name = "car")
    public static class Car extends Vehicle {
        public String horsePower;
    }

    @XmlRootElement(name = "bicycle")
    public static class Bicycle extends Vehicle {
        public String color;
    }

}
