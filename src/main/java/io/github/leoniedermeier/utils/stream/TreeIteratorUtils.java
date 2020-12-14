package io.github.leoniedermeier.utils.stream;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Function;

/**
 * https://en.wikipedia.org/wiki/Tree_traversal
 *
 */
public class TreeIteratorUtils {

    private abstract static class AbstractTreeIterator<T> implements Iterator<T> {

        protected final Function<T, ? extends Iterable<T>> childrenSupplier;
        protected final LinkedList<T> intermediate = new LinkedList<>();

        public AbstractTreeIterator(T root, Function<T, ? extends Iterable<T>> childrenSupplier) {
            this.childrenSupplier = childrenSupplier;
            this.intermediate.add(root);
        }

        @Override
        public boolean hasNext() {
            return !intermediate.isEmpty();
        }
    }

    private static class BreathFirstTreeIterator<T> extends AbstractTreeIterator<T> {

        public BreathFirstTreeIterator(final T root, Function<T, ? extends Iterable<T>> childrenSupplier) {
            super(root, childrenSupplier);
        }

        @Override
        @SuppressWarnings("squid:S2272")
        // NoSuchElementException thrown from pop() method
        public T next() {
            final T current = intermediate.pop();
            Optional.ofNullable(current).map(childrenSupplier).ifPresent(it -> it.forEach(intermediate::add));
            return current;
        }
    }

    private static class DepthFirstPreOrderTreeIterator<T> extends AbstractTreeIterator<T> {
        public DepthFirstPreOrderTreeIterator(final T root, Function<T, ? extends Iterable<T>> childrenSupplier) {
            super(root, childrenSupplier);
        }

        @Override
        @SuppressWarnings("squid:S2272")
        // NoSuchElementException thrown from pop() method
        public T next() {
            final T current = intermediate.pop();
            Optional.ofNullable(current).map(childrenSupplier)
                    .ifPresent(it -> it.forEach(intermediate.listIterator()::add));
            return current;
        }
    }

    public static final <T> Iterator<T> breathFirst(final T root,
            final Function<T, ? extends Iterable<T>> childrenSupplier) {
        return new BreathFirstTreeIterator<>(root, childrenSupplier);
    }

    public static final <T> Iterator<T> depthFirstPreOrder(final T root,
            final Function<T, ? extends Iterable<T>> childrenSupplier) {
        return new DepthFirstPreOrderTreeIterator<>(root, childrenSupplier);
    }

    private TreeIteratorUtils() {
        throw new AssertionError("No TreeIteratorUtils instances for you!");
    }
}