package io.github.leoniedermeier.utils.stream;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class BreathFirstTreeIteratorVariation<T> implements Iterator<T> {

    private static class SingleElementIterator<T> implements Iterator<T> {

        private boolean hasNext = true;
        private final T object;

        public SingleElementIterator(T object) {
            this.object = object;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public T next() {
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            this.hasNext = false;
            return object;
        }
    }
    private final Function<T, ? extends Iterable<T>> childrenSupplier;

    private Iterator<T> current;

    private final LinkedList<Iterable<T>> intermediate = new LinkedList<>();

    public BreathFirstTreeIteratorVariation(final T root, Function<T, ? extends Iterable<T>> childrenSupplier) {
        this.childrenSupplier = childrenSupplier;
        current = new SingleElementIterator<>(root);
    }

    @Override
    public boolean hasNext() {
        return current != null && current.hasNext();
    }

    @Override
    public T next() {
        if (current == null) {
            throw new NoSuchElementException();
        }
        final T element = current.next();

        Iterable<T> children = childrenSupplier.apply(element);
        if (children != null) {
            intermediate.add(children);
        }
        while (!hasNext() && !intermediate.isEmpty()) {
            Iterable<T> candidate = intermediate.pop();
            current = candidate.iterator();
        }
        return element;
    }
}