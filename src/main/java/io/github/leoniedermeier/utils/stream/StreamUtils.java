package io.github.leoniedermeier.utils.stream;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    public static <T, R> Function<T, Stream<R>> filterInstances(Class<R> clazz) {
        return (T t) -> {
            if (clazz.isInstance(t)) {
                return Stream.of(clazz.cast(t));
            }
            return Stream.empty();
        };
    }

    /**
     * Returns a sequential {@link Stream} of the contents of the given
     * {@link Enumeration}.
     *
     * @param             <T> the stream type
     * @param enumeration The {@link Enumeration} to built the stream from.
     * @return The stream built from the given {@link Enumeration}
     */
    public static <T> Stream<T> stream(Enumeration<T> enumeration) {
        // https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Enumeration.html#asIterator()
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return enumeration.hasMoreElements();
            }

            @Override
            public T next() {
                if (enumeration.hasMoreElements()) {
                    return enumeration.nextElement();
                } else {
                    throw new NoSuchElementException();
                }
            }
        }, Spliterator.ORDERED), false);
    }

    /**
     * Returns a sequential {@link Stream} of the contents of the given
     * {@link Iterable}, delegating to {@link Collection#stream} if possible.
     *
     * @param          <T> the stream type
     * @param iterable The {@link Iterable} to built the stream from.
     * @return The stream built from the given {@link Iterable}
     */
    public static <T> Stream<T> stream(Iterable<T> iterable) {
        return (iterable instanceof Collection) ? ((Collection<T>) iterable).stream()
                : StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * Returns a sequential {@link Stream} of the contents of the given
     * {@link Iterator}.
     *
     * @param          <T> the stream type
     * @param iterator The {@link Iterator} to built the stream from.
     * @return The stream built from the given {@link Iterator}
     */
    public static <T> Stream<T> stream(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }

    private StreamUtils() {
        throw new AssertionError("No StreamUtils instances for you!");
    }
}