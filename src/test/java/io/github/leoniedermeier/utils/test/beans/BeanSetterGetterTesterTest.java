package io.github.leoniedermeier.utils.test.beans;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

public class BeanSetterGetterTesterTest {

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

    public static class MyBean {
        private String name;

        private int someInt;

        public void setSomeInt(int someInt) {
            this.someInt = someInt;
        }

        public int getSomeInt() {
            return this.someInt;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

    }

    public static class MyErrorBean {

        public void setWrong(String wrong) {
        }

        public String getWrong() {
            return "WRONG";
        }

    }
}
