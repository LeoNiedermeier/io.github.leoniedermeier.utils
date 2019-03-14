package io.github.leoniedermeier.utils.test.beans;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.opentest4j.MultipleFailuresError;

class GenericBeanPropertyTesterTest {

    @Test
    void all_properties_ok() {
        GenericBeanPropertyTester.testAllSettersGetters(MyBean.class);
    }

    @Test
    void wrong_property_throws_exception() {
        assertThrows(MultipleFailuresError.class,
                () -> GenericBeanPropertyTester.testAllSettersGetters(MyErrorBean.class));
    }

    public static class MyBean {
        private String name;

        private int number;

        public void setNumber(int number) {
            this.number = number;
        }

        public int getNumber() {
            return number;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

    public static class MyErrorBean {

        public void setWrong(String wrong) {
        }

        public String getWrong() {
            return "WRONG";
        }

        public void setNumber(int number) {

        }

        public int getNumber() {
            return 12;
        }
    }

}
