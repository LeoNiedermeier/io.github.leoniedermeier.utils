package io.github.leoniedermeier.utils.test.hamcrest;

import static io.github.leoniedermeier.utils.test.hamcrest.PropertyAccess.property;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.everyItem;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class PersonAndPhoneTest {

    @Test
    void person_has_phones_with_number_starts_with_0049() {
        Person person = new Person("Name");
        person.addPhone(new Phone("0049-1234"));
        person.addPhone(new Phone("0049-ABCD"));
        assertThat(person, allPhoneNumbersStartWith("0049"));
    }

    /**
     * Reusable Matcher for person's phone number
     */
    static Matcher<Person> allPhoneNumbersStartWith(String string) {
        return property(Person::getPhones, " person phone numbers ")
                .is(everyItem(property(Phone::getNumber, " phone number ").is(Matchers.startsWith(string))));
    }
}
