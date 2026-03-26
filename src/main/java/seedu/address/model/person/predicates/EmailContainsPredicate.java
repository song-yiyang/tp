package seedu.address.model.person.predicates;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s {@code Email} matches any of the keywords given.
 */
public class EmailContainsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public EmailContainsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        if (!person.hasEmail()) {
            return false;
        }

        return keywords.stream()
                .anyMatch(keyword -> person.getEmail().value.toLowerCase().contains(keyword.toLowerCase()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EmailContainsPredicate)) {
            return false;
        }

        EmailContainsPredicate otherEmailContainsPredicate = (EmailContainsPredicate) other;
        return keywords.equals(otherEmailContainsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
