package io.github.leoniedermeier.utils.beans;

import java.util.function.Function;

import org.apache.commons.lang3.builder.Builder;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ExtractorDiffBuilder<T, L extends T, R extends T> implements Builder<DiffResult<T>> {

    public static <T, L extends T, R extends T> ExtractorDiffBuilder<T, L, R> of(L lhs, R rhs, ToStringStyle style) {
        return new ExtractorDiffBuilder<>(lhs, rhs, style);
    }
    private final DiffBuilder<T> diffBuilder;
    private final L lhs;

    private final R rhs;

    public ExtractorDiffBuilder(L lhs, R rhs, final ToStringStyle style) {
        super();
        this.lhs = lhs;
        this.rhs = rhs;
        this.diffBuilder = new DiffBuilder<T>(lhs, rhs, style);
    }

    public ExtractorDiffBuilder<T, L, R> append(String fieldName, Function<T, Object> valueExtractor) {
        this.diffBuilder.append(fieldName, valueExtractor.apply(this.lhs), valueExtractor.apply(this.rhs));
        return this;
    }

    @Override
    public DiffResult<T> build() {
        return this.diffBuilder.build();
    }
}
