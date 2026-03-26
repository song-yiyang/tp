package seedu.address.model.person.predicates;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagList;
import seedu.address.model.tag.Tag;

/**
 * Tests that a {@code Person}'s {@code TagList} contains any of the given tag strings.
 */
public class TagContainsPredicate implements Predicate<Person> {
    private final List<Tag> tags;

    public TagContainsPredicate(List<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean test(Person person) {
        TagList personTags = person.getTags();
        return tags.stream()
                   .allMatch(tag -> personTags.filterTagCaseInsensitive(tag.tagName)
                                              .filter(t -> t.toLowerCase().contains(tag.tagValue.toLowerCase()))
                                              .isPresent());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagContainsPredicate)) {
            return false;
        }

        TagContainsPredicate otherTagEqualsPredicate = (TagContainsPredicate) other;
        return tags.equals(otherTagEqualsPredicate.tags);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tags", tags).toString();
    }
}
