package io.github.leoniedermeier.utils.mapstruct;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReferenceResolverTest {

    private static class SampleBean {
        String name;

        public void setName(String name) {
            this.name = name;
        }
    }

    @Nested
    class ResolveReferences {
        @Test
        void reference_added_to_list() {
            ReferenceResolver referenceResolver = new ReferenceResolver();
            referenceResolver.registerInstance("1", "EXPECTED");

            SampleBean target = new SampleBean();
            referenceResolver.registerRefereceTarget("1", target::setName);

            referenceResolver.resolveReferences();

            assertEquals("EXPECTED", target.name);
        }

        @Test
        void type_mismatch() {
            ReferenceResolver referenceResolver = new ReferenceResolver();
            referenceResolver.registerInstance("1", BigInteger.ONE);

            SampleBean target = new SampleBean();
            referenceResolver.registerRefereceTarget("1", target::setName);

            assertThrows(IllegalStateException.class, () -> referenceResolver.resolveReferences());
        }

        @Test
        void missing_instance() {
            ReferenceResolver referenceResolver = new ReferenceResolver();
            SampleBean target = new SampleBean();
            referenceResolver.registerRefereceTarget("1", target::setName);
            assertThrows(IllegalStateException.class, () -> referenceResolver.resolveReferences());
        }
    }
}
