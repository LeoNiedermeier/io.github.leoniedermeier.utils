package io.github.leoniedermeier.utils.methodreference;

import static io.github.leoniedermeier.utils.methodreference.SerializedLambdaTargetMethodDetector.resolve;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

import io.github.leoniedermeier.utils.methodreference.MethodDetectorTestHelperClasses.AbstractClass;
import io.github.leoniedermeier.utils.methodreference.MethodDetectorTestHelperClasses.MyInterface;
import io.github.leoniedermeier.utils.methodreference.MethodDetectorTestHelperClasses.Person;
import io.github.leoniedermeier.utils.methodreference.MethodDetectorTestHelperClasses.WithConstructorArgument;
import io.github.leoniedermeier.utils.methodreference.SerializedLambdaTargetMethodDetector.SerializableBiConsumer;
import io.github.leoniedermeier.utils.methodreference.SerializedLambdaTargetMethodDetector.SerializableConsumer;

class SerializedLambdaTargetMethodDetectorTest {

	private static <T, U> void assertMethodName2(String expected, SerializableBiConsumer<T, U> method) {
		Method expectedMethod = resolve(method);
		assertEquals(expected, expectedMethod.getName());
	}

	private static <T> void assertMethodName(String expected, SerializableConsumer<T> method) {
		Method expectedMethod = resolve(method);
		assertEquals(expected, expectedMethod.getName());
	}

	// @Test
	void call_abstract_method_from_abstract_class() {
		assertMethodName("getVoid", AbstractClass::getVoid);
	}

	@Test
	void call_method_from_class_with_constructor_with_argument() {
		assertMethodName("getVoid", WithConstructorArgument::getVoid);
	}

	@Test
	void call_methods_with_no_parameter() {
		assertMethodName("getVoid", Person::getVoid);

		assertMethodName("getName", Person::getName);
	}

	@Test
	void call_methods_with_no_parameter_from_interface() {
		assertMethodName("getVoid", MyInterface::getVoid);
	}

	@Test
	void call_methods_with_one_parameter() {
		assertMethodName2("setNameWithReturn", Person::setNameWithReturn);
		assertMethodName2("setName", Person::setName);
	}

}
