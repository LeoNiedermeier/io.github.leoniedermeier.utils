package io.github.leoniedermeier.utils.mapstruct;

public class ThreadLocalReferenceResolverHolder {

    private static final ThreadLocal<ReferenceResolver> REFERENCE_RESOLVER_HOLDER = ThreadLocal
            .withInitial(ReferenceResolver::new);

    public static ReferenceResolver getReferenceResolver() {
        return REFERENCE_RESOLVER_HOLDER.get();
    }

    /**
     * 
     */
    public static void reset() {
        REFERENCE_RESOLVER_HOLDER.remove();
    }

    private ThreadLocalReferenceResolverHolder() {
        throw new AssertionError("No ThreadLocalReferenceResolverHolder instances for you!");
    }
}
