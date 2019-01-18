package io.github.leoniedermeier.utils.beans;

import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;

import io.github.leoniedermeier.utils.beans.ExtractorDiffBuilder;

class ExtractorDiffBuilderTest {

	static class TestClass {
		String name;

		public TestClass(String name) {
			super();
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	@Test
	void test() {
		TestClass lhs = new TestClass("Hans");
		TestClass rhs = new TestClass("Sepp");
		DiffResult diffResult = new ExtractorDiffBuilder<>(lhs, rhs, ToStringStyle.JSON_STYLE)
				.append("name", TestClass::getName).build();
		System.out.println(diffResult);
	}

}
