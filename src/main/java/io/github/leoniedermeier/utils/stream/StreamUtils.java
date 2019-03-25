package io.github.leoniedermeier.utils.stream;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;
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

    public static <T> Stream<T> flattenedTreeForIterableChildren(T element, Function<T, Iterable<T>> childrenSupplier) {
        return flattenedTreeForStreamChildren(element, childrenSupplier.andThen(StreamUtils::stream));
    }

    public static <T> Stream<T> flattenedTreeForIterableChildrenAlternative(T element,
            Function<T, Iterable<T>> childrenSupplier) {
        return flattenedTreeForStreamChildrenAlternative(element, childrenSupplier.andThen(StreamUtils::stream));
    }

    public static <T> Stream<T> flattenedTreeForStreamChildren(T element, Function<T, Stream<T>> childrenSupplier) {
        // Depth-first Pre-order (NLR)
        // Attention: each element is wrapped in a Stream of a single element. Can be
        // preformance critical.
        return Stream.concat(Stream.of(element), Optional.ofNullable(element).map(childrenSupplier)
                .orElse(Stream.empty()).flatMap(t -> flattenedTreeForStreamChildren(t, childrenSupplier)));
    }

    public static <T> Stream<T> flattenedTreeForStreamChildrenAlternative(T element,
            Function<T, Stream<T>> childrenSupplier) {
        // Order is (parent, children of parent, children of child 1, children of child
        // 1 of child 1, childeren of child 2)
        // like Depth-first Pre-order (NLR) but insted of element the children of
        // element are returnde
        return Stream.concat(Stream.of(element),
                flattenedTreeForStreamChildrenAlternativeRecursion(element, childrenSupplier));
    }

    /**
     * Returns a sequential {@link Stream} of the contents of the given
     * {@link Enumeration}.
     *
     * @param <T>         the stream type
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
     * @param <T>      the stream type
     * @param iterable The {@link Iterable} to built the stream from.
     * @return The stream built from the given {@link Iterable}
     */
    public static <T> Stream<T> stream(Iterable<T> iterable) {
        if (iterable instanceof Collection) {
            return ((Collection<T>) iterable).stream();
        } else if (iterable == null) {
            return Stream.empty();
        }
        return StreamSupport.stream(iterable.spliterator(), false);
    }

    /**
     * Returns a sequential {@link Stream} of the contents of the given
     * {@link Iterator}.
     *
     * @param <T>      the stream type
     * @param iterator The {@link Iterator} to built the stream from.
     * @return The stream built from the given {@link Iterator}
     */
    public static <T> Stream<T> stream(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }

    private static <T> Stream<T> flattenedTreeForStreamChildrenAlternativeRecursion(T element,
            Function<T, Stream<T>> childrenSupplier) {
        return Stream.concat(Optional.ofNullable(element).map(childrenSupplier).orElse(Stream.empty()),
                Optional.ofNullable(element).map(childrenSupplier).orElse(Stream.empty())
                        .flatMap(t -> flattenedTreeForStreamChildrenAlternativeRecursion(t, childrenSupplier)));
    }

    private StreamUtils() {
        throw new AssertionError("No StreamUtils instances for you!");
    }
}