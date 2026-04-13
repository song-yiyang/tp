package seedu.address.testutil;

import seedu.address.model.person.Person;

/**
 * Subclass of Person that implements equality checking by fields only.
 * Used for testing to check that 2 persons are equal.
 */
public class TestPerson extends Person {

    public TestPerson(Person person) {
        super(person);
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        System.out.println("TestPerson equals");
        System.out.println(this);
        System.out.println(other);
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        System.out.println(super.testEquals((Person) other));

        return super.testEquals((Person) other);
    }
}
