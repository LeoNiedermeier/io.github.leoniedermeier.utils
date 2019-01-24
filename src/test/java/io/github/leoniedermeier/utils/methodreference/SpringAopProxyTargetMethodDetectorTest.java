package io.github.leoniedermeier.utils.methodreference;

import static io.github.leoniedermeier.utils.methodreference.SpringAopProxyTargetMethodDetector.resolve;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import io.github.leoniedermeier.utils.methodreference.MethodDetectorTestHelperClasses.AbstractClass;
import io.github.leoniedermeier.utils.methodreference.MethodDetectorTestHelperClasses.MyInterface;
import io.github.leoniedermeier.utils.methodreference.MethodDetectorTestHelperClasses.Person;
import io.github.leoniedermeier.utils.methodreference.MethodDetectorTestHelperClasses.WithConstructorArgument;

class SpringAopProxyTargetMethodDetectorTest {

	static <T> void assertMethodName(String expected, Class<T> type, Consumer<T> method) {
		Method expectedMethod = resolve(type, method);
		assertEquals(expected, expectedMethod.getName());
	}

	static <T, U> void assertMethodName(String expected, Class<T> type, BiConsumer<T, U> method) {
		Method expectedMethod = resolve(type, method);
		assertEquals(expected, expectedMethod.getName());
	}

	@Test
	void call_abstract_method_from_abstract_class() {
		assertMethodName("getVoid", AbstractClass.class, AbstractClass::getVoid);
	}

	@Test
	void call_method_from_class_with_constructor_with_argument() {
		assertMethodName("getVoid", WithConstructorArgument.class, WithConstructorArgument::getVoid);
	}

	@Test
	void call_methods_with_no_parameter() {
		assertMethodName("getVoid", Person.class, Person::getVoid);

		assertMethodName("getName", Person.class, Person::getName);
	}

	@Test
	void call_methods_with_no_parameter_from_interface() {
		assertMethodName("getVoid", MyInterface.class, MyInterface::getVoid);
	}

	@Test
	void call_methods_with_one_parameter() {
		assertMethodName("setNameWithReturn", Person.class, Person::setNameWithReturn);
		assertMethodName("setName", Person.class, Person::setName);
	}
}
