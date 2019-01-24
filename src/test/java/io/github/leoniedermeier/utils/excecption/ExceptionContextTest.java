package io.github.leoniedermeier.utils.excecption;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class ExceptionContextTest {

	static class TestExceptionContext implements ExceptionContext<TestExceptionContext> {

		final List<Entry> entries = new ArrayList<ExceptionContext.Entry>();

		@Override
		public List<Entry> getContextEntries() {
			return entries;
		}

	}

	TestExceptionContext exceptionContext = new TestExceptionContext();

	@Test
	void getFirstContextValue_existing() {
		Object value = new Object();
		exceptionContext.addContextValue("label", value);

		assertSame(value, exceptionContext.findFirstContextValue("label").get());
	}

	@Test
	void getFirstContextValue_multiple_existing() {
		Object value = new Object();
		exceptionContext.addContextValue("label", value);
		exceptionContext.addContextValue("label", "2");

		assertSame(value, exceptionContext.findFirstContextValue("label").get());
	}

	@Test
	void getFirstContextValue_not_existing() {
		assertFalse(exceptionContext.findFirstContextValue("label").isPresent());
	}

	@Test
	void getFirstContextValue_null() {
		assertFalse(exceptionContext.findFirstContextValue(null).isPresent());
	}

	@Test
	void getContextEntries_empty() {
		assertTrue(exceptionContext.getContextEntries().isEmpty());
	}

	@Test
	void getContextEntries_two_elements() {
		exceptionContext.addContextValue("label-1", "value-1");
		exceptionContext.addContextValue("label-2", "value-2");

		assertEquals(2, exceptionContext.getContextEntries().size());
	}
}
