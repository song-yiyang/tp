package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.List;
import java.util.Optional;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Optional<Phone> phone;
    private final Optional<Email> email;

    // Data fields
    private final TagList tags;
    private final Status status;

    /**
     * Initialises a new person.
     * Name and tag list must be present and not null (tag list can be empty).
     */
    public Person(Name name, Phone phone, Email email, TagList tags) {
        this(name, phone, email, tags, Status.NONE);
    }

    /**
     * Initialises a new person.
     * Name and tag list must be present and not null (tag list can be empty).
     */
    public Person(Name name, Phone phone, Email email, TagList tags, Status status) {
        requireAllNonNull(name, tags, status); // defensive coding, to avoid null values

        this.name = name;
        this.phone = Optional.ofNullable(phone);
        this.email = Optional.ofNullable(email);
        this.tags = new TagList(tags);
        this.status = status;
    }

    /**
     * Creates an identical copy of another person.
     */
    public Person(Person otherPerson) {
        requireAllNonNull(otherPerson);
        this.name = otherPerson.name;
        this.phone = otherPerson.phone;
        this.email = otherPerson.email;
        this.tags = otherPerson.tags;
        this.status = otherPerson.status;
    }

    public Name getName() {
        return name;
    }

    public boolean hasPhone() {
        return phone.isPresent();
    }

    public Phone getPhone() {
        assert (hasPhone());
        return phone.get();
    }

    public boolean hasEmail() {
        return email.isPresent();
    }

    public Email getEmail() {
        assert (hasEmail());
        return email.get();
    }

    public TagList getTags() {
        return this.tags;
    }

    /**
     * Returns a list of printable tag name-value pairs, collapsed into Strings.
     */
    public List<String> getPrintableTags() {
        return this.tags.viewTags();
    }

    public Status getStatus() {
        return this.status;
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null
                && otherPerson.getName().equals(getName());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     * Used for testing.
     */
    public boolean testEquals(Person otherPerson) {
        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && email.equals(otherPerson.email)
                && tags.equals(otherPerson.tags)
                && status.equals(otherPerson.status);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("email", email)
                .add("tags", tags)
                .add("status", status)
                .toString();
    }

}
