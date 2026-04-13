package seedu.address.model.person.predicates;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s {@code Phone} exactly matches the given phone number.
 */
public class PhoneEqualsPredicate implements Predicate<Person> {
    private final List<String> phone;

    public PhoneEqualsPredicate(List<String> phone) {
        this.phone = phone;
    }

    @Override
    public boolean test(Person person) {
        if (person.hasPhone()) {
            return phone.contains(person.getPhone().value);
        }
        return false;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof PhoneEqualsPredicate)) {
            return false;
        }

        PhoneEqualsPredicate otherPhoneEqualsPredicate = (PhoneEqualsPredicate) other;
        return Objects.equals(phone, otherPhoneEqualsPredicate.phone);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("phone", phone).toString();
    }
}
