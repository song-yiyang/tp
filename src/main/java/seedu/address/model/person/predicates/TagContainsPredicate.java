package seedu.address.model.person.predicates;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Person;
import seedu.address.model.person.TagList;
import seedu.address.model.tag.TagFilter;

/**
 * Tests that a {@code Person}'s {@code TagList} matches the given tag filters.
 *
 * Filters sharing the same tag name are grouped together and combined with OR logic.
 * These groups are then combined with AND logic.
 */
public class TagContainsPredicate implements Predicate<Person> {
    private final List<TagFilter> tagFilters;

    public TagContainsPredicate(List<TagFilter> tagFilters) {
        this.tagFilters = tagFilters;
    }

    @Override
    public boolean test(Person person) {
        TagList personTags = person.getTags();

        // Group tagFilters by tag name (case-insensitive)
        Map<String, List<TagFilter>> tagFilterGroups = tagFilters.stream()
                .collect(Collectors.groupingBy(tf -> tf.getTagName().toLowerCase()));

        // Check that for each tag filter group, at least one tag filter matches the person's tags
        return tagFilterGroups.values().stream()
                .allMatch(tagFilterGroup -> tagFilterGroup.stream()
                        .anyMatch(tagFilter -> matchesTagFilter(personTags, tagFilter)));
    }

    private boolean matchesTagFilter(TagList personTags, TagFilter tagFilter) {
        return personTags.filterTagCaseInsensitive(tagFilter.getTagName()).stream()
                .anyMatch(tagValue -> tagFilter.getTagValue()
                        .map(expectedValue -> tagValue.toLowerCase().contains(expectedValue.toLowerCase()))
                        .orElse(true));
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
