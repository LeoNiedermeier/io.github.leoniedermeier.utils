package io.github.leoniedermeier.utils.methodreference;

public interface MethodDetectorTestHelperClasses {

	public interface MyInterface {
		void getVoid();
	}

	public class Person {

		public Person() {

		}

		public String getName() {
			return "x";
		}

		public String setNameWithReturn(String name) {
			return null;
		}

		public void setName(String name) {
		}

		public int getInt() {
			return 0;
		}

		public void getVoid() {
		}
	}

	public class WithConstructorArgument {
		public WithConstructorArgument(String param) {
		}

		public void getVoid() {
		}
	}

	public abstract class AbstractClass {
		public AbstractClass() {
		}

		abstract public void getVoid();
	}
}
