package io.github.leoniedermeier.utils.beans;

import static io.github.leoniedermeier.utils.beans.PropertyAccessor.get;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.function.Supplier;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.leoniedermeier.utils.test.junit.jupiter.UtilClassConventionsTester;

class PropertyAccessorTest {

    static class Address {
        String street;

        public String getStreet() {
            return street;
        }
    }

    static class Person {
        Address address;

        public Address getAddress() {
            return address;
        }
    }

    @Nested
    @DisplayName("Method: get(T, Function)")
    class TestGetOneMethodReference {
        @Test
        @DisplayName("Existing property: value")
        void property_get_existing() {
            Person person = new Person();
            person.address = new Address();
            Address result = PropertyAccessor.get(person, Person::getAddress);
            assertThat(result, sameInstance(person.address));
        }

        @Test
        @DisplayName("Null property: returns null")
        void property_get_null_throws_exception() {
            Person person = new Person();
            Address result = PropertyAccessor.get(person, Person::getAddress);
            assertThat(result, nullValue());
        }
    }

    @Nested
    @DisplayName("Method: get(T, Function, Function)")
    class TestGetWithTwoMethodRefernces {

        @Test
        @DisplayName("Existing nested properties")
        void existing_nested_properties() {
            Person person = new Person();
            person.address = new Address();
            person.address.street = "Street";

            assertThat(get(person, p -> p.getAddress(), a -> a.getStreet()), sameInstance(person.address.street));

            // oder:
            assertSame(person.address.street, get(person, p -> p.getAddress(), a -> a.getStreet()));

            assertSame(person.address.street, get(person, Person::getAddress, Address::getStreet));

            assertSame(person.address.street, get(person.getAddress(), Address::getStreet));

            assertSame(person.address.street, npeSafe(() -> person.getAddress().getStreet()));
        }

        @Test
        @DisplayName("Not existing nested properties")
        void not_existing_nested_properties() {
            Person person = new Person();
            String result = PropertyAccessor.get(person, Person::getAddress, Address::getStreet);
            assertThat(result, nullValue());

            assertNull(npeSafe(() -> person.address.street));

        }
    }

    @Nested
    class UtilClassConventions implements UtilClassConventionsTester<PropertyAccessor> {

    }

    static <U> U npeSafe(Supplier<U> s) {
        try {
            return s.get();
        } catch (NullPointerException e) {
            return null;
        }
    }
}
