package io.github.leoniedermeier.utils.stream;

import java.util.function.Function;
import java.util.stream.Stream;

public class StreamUtils {

	/**
	 * Shortcut for {@link Stream#filter(java.util.function.Predicate)} and
	 * {@link Stream#flatMap(Function)}.
	 * 
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * {@code
	 * stream.filter(Number.class::isInstance).map(Number.class::cast)
	 *    .forEach(System.out::println);
	 * }
	 * </pre>
	 * 
	 * to
	 * 
	 * <pre>
	 * {@code stream.
	 *     flatMap(filterInstances(Number.class))
	 *      .forEach(System.out::println);}
	 * </pre>
	 * </p>
	 * 
	 * @param clazz The desired class.
	 * @return A {@link Function} which filters and casts the elements and returns a
	 *         {@link Stream}.
	 */
	public static <T, R> Function<? super T, ? extends Stream<? extends R>> filterInstances(Class<R> clazz) {
		return o -> {
			if (clazz.isInstance(o)) {
				return Stream.of(clazz.cast(o));
			}
			return Stream.empty();
		};
	}

	private StreamUtils() {
		throw new AssertionError("No StreamUtils instances for you!");
	}
}