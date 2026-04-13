package seedu.address.model.person.predicates;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;

/**
 * Tests that a {@code Person}'s {@code Name} matches any of the keywords given.
 */
public class NameContainsPredicate implements Predicate<Person> {
    private final List<String> substring;

    public NameContainsPredicate(List<String> keywords) {
        this.substring = keywords;
    }

    @Override
    public boolean test(Person person) {
        return substring.stream()
                .map(String::toLowerCase)
                .anyMatch(keyword -> person.getName().fullName.toLowerCase().contains(keyword));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsPredicate otherNameContainsPredicate)) {
            return false;
        }

        return substring.equals(otherNameContainsPredicate.substring);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", substring).toString();
    }
}
