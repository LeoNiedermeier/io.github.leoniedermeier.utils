package io.github.leoniedermeier.utils.mapstruct;

import java.util.function.Consumer;

public class MappingContext {

    private final ReferenceResolver referenceResolver = new ReferenceResolver();

    public MappingContext() {
        super();
    }

    public void registerInstance(Object key, Object value) {
        referenceResolver.registerInstance(key, value);
    }

    public <T> void registerRefereceTarget(Object key, Consumer<T> setter) {
        referenceResolver.registerRefereceTarget(key, setter);
    }

    public void resolveReferences() {
        referenceResolver.resolveReferences();
    }
}
