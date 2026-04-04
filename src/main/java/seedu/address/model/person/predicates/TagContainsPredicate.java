package seedu.address.model.person.predicates;

import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagList;
import seedu.address.model.tag.TagFilter;

/**
 * Tests that a {@code Person}'s {@code TagList} contains all of the given tag filters.
 */
public class TagContainsPredicate implements Predicate<Person> {
    private final List<TagFilter> tagFilters;

    public TagContainsPredicate(List<TagFilter> tagFilters) {
        this.tagFilters = tagFilters;
    }

    @Override
    public boolean test(Person person) {
        // A person matches the tag filters if for each tag filter, the person's tags contain a tag with the same name
        // and a value that contains the filter's value (if specified).
        TagList personTags = person.getTags();
        return tagFilters.stream().allMatch(tagFilter -> personTags.filterTagCaseInsensitive(tagFilter.tagName)
            .filter(tagValue -> tagFilter.getTagValue()
                                         .map(expectedValue -> tagValue.toLowerCase()
                                                                       .contains(expectedValue.toLowerCase()))
                                         .orElse(true))
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
        return tagFilters.equals(otherTagEqualsPredicate.tagFilters);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("tagFilters", tagFilters).toString();
    }
}
