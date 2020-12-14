package io.github.leoniedermeier.utils.excecption;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class EnumErrorCodeTest {

    enum MyEnum implements EnumErrorCode {

        @Description("description")
        WITH_DESCRIPTION,

        WITHOUT_DESCRIPTION;
    }

    enum EnumWithSubclass implements EnumErrorCode {

        @Description("description")
        WITH_DESCRIPTION {
            @Override
            public String toString() {
                return super.toString();
            }
        },
    }

    @Nested
    class DescriptionTest {
        @Test
        void withDescription() {
            assertEquals("description", MyEnum.WITH_DESCRIPTION.description());
        }

        @Test
        void withOutDescription() {
            assertNull(MyEnum.WITHOUT_DESCRIPTION.description());
        }

        @Test
        void notEnum() {
            EnumErrorCode enumErrorCode = new EnumErrorCode() {

                @Override
                public String name() {
                    return "123";
                }
            };
            assertNull(enumErrorCode.description());
        }

        @Test
        void notEnum2() {
            assertEquals("description", EnumWithSubclass.WITH_DESCRIPTION.description());
        }

    }

}
