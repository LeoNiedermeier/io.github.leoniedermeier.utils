package io.github.leoniedermeier.utils.beans;

import static io.github.leoniedermeier.utils.beans.PropertyAccessor.get;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.function.Supplier;

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

			assertNull(npeSafe(() -> person.address.street));

		}

		@Test
		@DisplayName("Existing nested properties")
		void existing_nested_properties() {
			Person person = new Person();
			person.address = new Address();
			person.address.street = "Street";

			assertSame(person.address.street, get(person, p -> p.getAddress(), a -> a.getStreet()));

			// oder:
			assertSame(person.address.street, get(person, Person::getAddress, Address::getStreet));

			assertSame(person.address.street, get(person.getAddress(), Address::getStreet));

			assertSame(person.address.street, npeSafe(() -> person.getAddress().getStreet()));
		}
	}

	static <U> U npeSafe(Supplier<U> s) {
		try {
			return s.get();
		} catch (NullPointerException e) {
			System.out.println("NPE");
			return null;
		}
	}

}
