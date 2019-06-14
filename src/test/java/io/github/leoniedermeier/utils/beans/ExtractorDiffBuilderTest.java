package io.github.leoniedermeier.utils.beans;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;

import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Test;

class ExtractorDiffBuilderTest {

    static class TestClass {
        String name;

        public TestClass(String name) {
            super();
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    void test() {
        TestClass lhs = new TestClass("Hans");
        TestClass rhs = new TestClass("Sepp");
        DiffResult diffResult = new ExtractorDiffBuilder<>(lhs, rhs, ToStringStyle.JSON_STYLE)
                .append("id", TestClass::getName).build();
        assertThat(diffResult.getDiffs(), iterableWithSize(1));
        Diff<?> diff = diffResult.getDiffs().get(0);
        assertThat(lhs.name, equalTo(diff.getLeft()));
        assertThat(rhs.name, equalTo(diff.getRight()));
    }
}
