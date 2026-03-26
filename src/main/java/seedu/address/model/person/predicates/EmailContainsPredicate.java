package seedu.address.model.person.predicates;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s {@code Email} matches any of the keywords given.
 */
public class EmailContainsPredicate implements Predicate<Person> {
    private final List<String> substring;

    public EmailContainsPredicate(List<String> keywords) {
        this.substring = keywords;
    }

    @Override
    public boolean test(Person person) {
        if (!person.hasEmail()) {
            return false;
        }

        return substring.stream()
                .map(String::toLowerCase)
                .anyMatch(keyword -> person.getEmail().value.toLowerCase().contains(keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EmailContainsPredicate otherEmailContainsPredicate)) {
            return false;
        }

        return substring.equals(otherEmailContainsPredicate.substring);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", substring).toString();
    }
}
