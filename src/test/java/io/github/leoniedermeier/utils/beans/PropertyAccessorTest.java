package io.github.leoniedermeier.utils.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import io.github.leoniedermeier.utils.beans.PropertyAccessorTest.Address;

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
	@DisplayName("Test for get(T, Function)")
	class TestGet1 {
		@Test
		@DisplayName("Existing property: value")
		void property_get_existing() {
			Person person = new Person();
			person.address = new Address();
			Address result = PropertyAccessor.get(person, Person::getAddress);

			assertSame(person.address, result);
		}

		@Test
		@DisplayName("Null property: returns null")
		void property_get_null_throws_exception() {
			Person person = new Person();
			Address result = PropertyAccessor.get(person, Person::getAddress);
			assertNull(result);
		}
	}

	@Nested
	@DisplayName("Test for get(T, Function, Function)")
	class TestGet2 {

		@Test
		@DisplayName("Not existing nested properties")
		void not_existing_nested_properties() {
			Person person = new Person();
			String result = PropertyAccessor.get(person, Person::getAddress, Address::getStreet);
			assertNull(result);
		}

		@Test
		@DisplayName("Existing nested properties")
		void existing_nested_properties() {
			Person person = new Person();
			person.address = new Address();
			person.address.street = "Street";
			String result = PropertyAccessor.get(person, Person::getAddress, Address::getStreet);
			assertSame(person.address.street, result);
		}

	}

}
