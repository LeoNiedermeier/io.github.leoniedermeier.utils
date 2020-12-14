package io.github.leoniedermeier.utils.test.hamcrest;

import java.util.ArrayList;
import java.util.List;

class Person {

    private String name;
    private final List<Phone> phones = new ArrayList<>();

    public Person(String name) {
        this.name = name;
    }

    public void addPhone(Phone phone) {
        phones.add(phone);
    }

    public String getName() {
        return name;
    }

    public List<Phone> getPhones() {
        return phones;
    }
}

class Phone {
    private String number;

    public Phone(String number) {
        super();
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}
