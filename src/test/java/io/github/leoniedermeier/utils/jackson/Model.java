package io.github.leoniedermeier.utils.jackson;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;

public class Model {
    
    @Target({ ElementType.TYPE })
    @Retention(RetentionPolicy.RUNTIME)
    @JacksonAnnotationsInside
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
    public @interface JsonBaseClass {

    }
    
    public static class Wrapper {
        public Vehicle[] vehicles;
    }

    @JsonBaseClass
    @JsonTypeName("my-vehicle")
    public static class Vehicle {
        public String name;
    }

    @JsonTypeName("my-car")
    public static class Car extends Vehicle {
        public String horsePower;
    }

    @JsonTypeName("my-bicycle")
    public static class Bicycle extends Vehicle {
        public String color;
    }

}
