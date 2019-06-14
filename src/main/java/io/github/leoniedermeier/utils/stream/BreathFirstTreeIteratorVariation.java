package io.github.leoniedermeier.utils.stream;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class BreathFirstTreeIteratorVariation<T> implements Iterator<T> {

    private final Function<T, ? extends Iterable<T>> childrenSupplier;

    private Iterator<T> current;

    private final LinkedList<Iterable<T>> intermediate = new LinkedList<>();

    public BreathFirstTreeIteratorVariation(final T root, Function<T, ? extends Iterable<T>> childrenSupplier) {
        this.childrenSupplier = childrenSupplier;
        current = Collections.singletonList(root).iterator();
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