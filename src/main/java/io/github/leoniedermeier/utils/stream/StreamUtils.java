package io.github.leoniedermeier.utils.stream;

import static java.util.Spliterator.IMMUTABLE;
import static java.util.Spliterator.ORDERED;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class StreamUtils {

    /**
     * Shortcut for {@link Stream#filter(Predicate)} and
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
     * Experimental
     */
    public static <T> Stream<T> flattenedTreeForIterableChildren(T element, Function<T, Iterable<T>> childrenSupplier) {
        return flattenedTreeForStreamChildren(element, childrenSupplier.andThen(StreamUtils::stream));
    }

    /**
     * Experimental
     */
    public static <T> Stream<T> flattenedTreeForIterableChildrenAlternative(T element,
            Function<T, Iterable<T>> childrenSupplier) {
        return flattenedTreeForStreamChildrenAlternative(element, childrenSupplier.andThen(StreamUtils::stream));
    }

    /**
     * Experimental
     */
    public static <T> Stream<T> flattenedTreeForStreamChildren(T element, Function<T, Stream<T>> childrenSupplier) {
        // Depth-first Pre-order (NLR)
        // Attention: each element is wrapped in a Stream of a single element. Can be
        // performance critical.
        return Stream.concat(Stream.of(element), Optional.ofNullable(element).map(childrenSupplier)
                .orElse(Stream.empty()).flatMap(t -> flattenedTreeForStreamChildren(t, childrenSupplier)));
    }

    /**
     * Experimental
     */
    public static <T> Stream<T> flattenedTreeForStreamChildrenAlternative(T element,
            Function<T, Stream<T>> childrenSupplier) {
        // Order is (parent, children of parent, children of child 1, children of child
        // 1 of child 1, children of child 2)
        // like Depth-first Pre-order (NLR) but instead of element the children of
        // element are returned
        return Stream.concat(Stream.of(element),
                flattenedTreeForStreamChildrenAlternativeRecursion(element, childrenSupplier));
    }

    /**
     * 
     * 
     * @param <T>          the stream type
     * @param firstElement
     * @param hasNext      {@link Predicate} to test if there is a next element.
     * @param next         {@link UnaryOperator} to access the next element.
     * @return
     */
    public static <T> Stream<T> iterate(final T firstElement, Predicate<T> hasNext, final UnaryOperator<T> next) {
        Objects.requireNonNull(firstElement);
        Objects.requireNonNull(hasNext);
        Objects.requireNonNull(next);
        final Iterator<T> iterator = new Iterator<T>() {
            private T current = firstElement;

            @Override
            public boolean hasNext() {
                return hasNext.test(current);
            }

            @Override
            public T next() {
                current = next.apply(current);
                return current;
            }
        };
        // "Problem" is the first element. The first call of iterator::next returns
        // firstElement::next
        // Therefore, in order to get the firstElement also in the stream, we have to
        // add it in the front.
        return Stream.concat(Stream.of(firstElement), StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED | Spliterator.IMMUTABLE), false));
    }

    public static <T> Stream<T> iterate2(final T firstElement, Predicate<T> hasNext, final UnaryOperator<T> next) {
        Objects.requireNonNull(firstElement);
        Objects.requireNonNull(hasNext);
        Objects.requireNonNull(next);

        Spliterator<T> spliterator = new AbstractSpliterator<T>(Long.MAX_VALUE, ORDERED | IMMUTABLE) {
            private T nextElementToConsume = firstElement;

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                Objects.requireNonNull(action);
                // the call order ensures that the first element is consumed and afterwards
                // the "next" elements.
                action.accept(nextElementToConsume);
                // prepare the next element
                if (hasNext.test(nextElementToConsume)) {
                    nextElementToConsume = next.apply(nextElementToConsume);
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<T> trySplit() {
                return null;
            }
        };

        return StreamSupport.stream(spliterator, false);
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
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED | Spliterator.IMMUTABLE), false);
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