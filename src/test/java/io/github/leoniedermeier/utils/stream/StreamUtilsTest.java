package io.github.leoniedermeier.utils.stream;

import static io.github.leoniedermeier.utils.stream.StreamUtils.filterInstances;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class StreamUtilsTest {

	@Test
	void filterInstances_matches() {
		List<String> result = Stream.of("x", 1, "y").flatMap(filterInstances(String.class)).collect(toList());
		assertIterableEquals(result, asList("x", "y"));
	}

	@Test
	void filterInstances_no_matches() {
		List<LocalDate> result = Stream.of("x", 1, "y").flatMap(filterInstances(LocalDate.class)).collect(toList());
		assertTrue(result.isEmpty());

	}

}
