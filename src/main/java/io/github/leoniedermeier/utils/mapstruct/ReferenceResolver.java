package io.github.leoniedermeier.utils.mapstruct;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ReferenceResolver {

    private static class ReferenceTarget<T> {
        private final Object key;
        private final Consumer<T> setter;

        public ReferenceTarget(Object key, Consumer<T> setter) {
            this.key = key;
            this.setter = setter;
        }
    }

    private final Map<Object, Object> instances = new HashMap<>();

    private final List<ReferenceTarget<?>> referenceTargets = new ArrayList<>();

    public ReferenceResolver() {
        super();
    }

    /**
     * Register an object as candidate for reference resolving.
     * 
     * @param key   The key.
     * @param value The instance.
     */
    public void registerInstance(Object key, Object value) {
        instances.put(key, value);
    }

    /**
     * Registers a reference target.
     * 
     * @param key    The key of the desired object instance.
     * @param setter The method to be invoked.
     */
    public <T> void registerRefereceTarget(Object key, Consumer<T> setter) {
        referenceTargets.add(new ReferenceTarget<T>(key, setter));
    }

    /**
     * Resolves all registered references.
     * 
     * @throws IllegalStateException if a reference can not resolved or the
     *                               registered instance has a incompatible type.
     */
    public void resolveReferences() {
        for (ReferenceTarget<?> referenceTarget : referenceTargets) {
            Object object = instances.get(referenceTarget.key);
            if (object == null) {
                throw new IllegalStateException("No instance for reference id '" + referenceTarget.key + "' found.");
            }
            try {
                // type mismatch for setter method is handled by the catch ClassCastException.
                @SuppressWarnings("unchecked")
                Consumer<Object> setter = (Consumer<Object>) referenceTarget.setter;
                setter.accept(object);
            } catch (ClassCastException e) {
                throw new IllegalStateException("Type of reference target does not match type of provided object. "
                        + "Class mismatch: " + e.getMessage() + "; Key: '" + referenceTarget.key + "'", e);
            }
        }
    }
}
