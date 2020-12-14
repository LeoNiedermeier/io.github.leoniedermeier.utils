package io.github.leoniedermeier.utils.stream;

import java.util.function.Supplier;
import java.util.stream.Stream;

@FunctionalInterface
public interface Step<E> {

    class Done<A> implements Step<A> {
        private final A value;

        private Done(A value) {
            this.value = value;
        }

        @Override
        public Step<A> next() {
            throw new IllegalStateException("Don't call");
        }
    }

    public static <A> Step<A> next(Supplier<Step<A>> nextStepSupplier) {
        return nextStepSupplier::get;
    }

    public static <A> Done<A> done(A value) {
        return new Done<>(value);
    }

    public static <A> A execute(final Step<A> step) {
        return Stream.iterate(step, Step::next).filter(Done.class::isInstance).map(x -> (Done<A>) x).findFirst()
                .orElseThrow(() -> new IllegalStateException("")).value;
    }

    public Step<E> next();

}