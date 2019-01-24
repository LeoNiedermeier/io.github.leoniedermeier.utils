package io.github.leoniedermeier.utils.beans;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

class PropertyAccessorListTest {

	@Test
	void test() {
		Parent parent = new Parent();
		parent.setChilds(Collections.singletonList(new Child("1")));
		// Exception fangen oder nicht?
		assertThrows(IndexOutOfBoundsException.class,
				() -> PropertyAccessor.get(parent, Parent::getChilds, l -> l.get(1)));

	}

	private static class Parent {
		private List<Child> childs;

		public List<Child> getChilds() {
			return childs;
		}

		public void setChilds(List<Child> childs) {
			this.childs = childs;
		}
	}

	private static class Child {

		private final String name;

		public Child(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

}
