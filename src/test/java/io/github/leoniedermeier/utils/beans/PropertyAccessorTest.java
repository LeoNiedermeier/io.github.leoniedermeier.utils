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
	@DisplayName("Test for get()")
	class TestGet {
		@Test
		@DisplayName("Existing property: value")
		void property_get_existing() {
			Person person = new Person();
			Person result = PropertyAccessor.of(person).get();

			assertSame(person, result);
		}

		@Test
		@DisplayName("Null property: returns null")
		void property_get_null_throws_exception() {
			assertNull(PropertyAccessor.of(null).get());
		}
	}

	@Nested
	@DisplayName("Test for nested properties")
	class TestNested {

		@Test
		@DisplayName("Not existing nested properties")
		void not_existing_nested_properties() {
			Person person = new Person();
			String result = PropertyAccessor.of(person).then(Person::getAddress).then(Address::getStreet).orElse("X");
			assertSame("X", result);
		}

		@Test
		@DisplayName("Existing nested properties")
		void existing_nested_properties() {
			Person person = new Person();
			person.address = new Address();
			person.address.street = "Street";
			String result = PropertyAccessor.of(person).then(Person::getAddress).then(Address::getStreet).get();
			assertSame(person.address.street, result);
		}

		@Test
		@DisplayName("Existing nested properties with of")
		void existing_nested_properties_with_of() {
			Person person = new Person();
			person.address = new Address();
			person.address.street = "Street";
			String result = PropertyAccessor.get(person, Person::getAddress, Address::getStreet);
			assertSame(person.address.street, result);
		}
	}

	@Nested
	@DisplayName("Test for orElse()")
	class TestOrElse {

		@Test
		@DisplayName("Existing property: value == 'property'")
		void orElse_existing_property() {
			Person person = new Person();
			Object result = PropertyAccessor.of(person).orElse(new Person());
			assertSame(person, result);
		}

		@Test
		@DisplayName("Null property: value == 'other'")
		void orElse_null_property() {

			Object result = PropertyAccessor.of(null).orElse("X");
			assertEquals("X", result);
		}
	}

	@Test
	@DisplayName("Factory Method: of")
	void of() {
		PropertyAccessor<?> propertyAccessor = PropertyAccessor.of(null);
		assertNotNull(propertyAccessor, "PropertyAcessor must not be null");
	}
}
