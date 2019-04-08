package io.github.leoniedermeier.utils.test.beans;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

public class BeanSetterGetterTesterTest {

    @Nested
    class AssertSetterGetter_with_random_input_value {
        @Test
        void all_properties_ok() {
            BeanSetterGetterTester.assertSetterGetter(MyBean.class, MyBean::setName, MyBean::getName);
            BeanSetterGetterTester.assertSetterGetter(MyBean.class, MyBean::setSomeInt, MyBean::getSomeInt);
        }

        @Test
        void wrong_property_throws_exception() {
            assertThrows(AssertionFailedError.class, () -> BeanSetterGetterTester.assertSetterGetter(MyErrorBean.class,
                    MyErrorBean::setWrong, MyErrorBean::getWrong));
        }
    }

    @Nested
    class AssertSetterGetter_with_given_input_value {
        @Test
        void all_properties_ok() {
            BeanSetterGetterTester.assertSetterGetter(MyBean.class, "NAME", MyBean::setName, MyBean::getName);
            BeanSetterGetterTester.assertSetterGetter(MyBean.class, 1, MyBean::setSomeInt, MyBean::getSomeInt);
        }

        @Test
        void wrong_property_throws_exception() {
            assertThrows(AssertionFailedError.class, () -> BeanSetterGetterTester.assertSetterGetter(MyErrorBean.class,
                    "NAME", MyErrorBean::setWrong, MyErrorBean::getWrong));
        }
    }

    public static class MyBean {
        private String name;

        private int someInt;

        public void setSomeInt(int someInt) {
            this.someInt = someInt;
        }

        public int getSomeInt() {
            return someInt;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    public static class MyErrorBean {

        private String wrong;

        public void setWrong(String wrong) {
            this.wrong = wrong;
        }

        public String getWrong() {
            return "WRONG: " + this.wrong;
        }
    }
}
