package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a filter on a tag, either by tag name alone or by tag name and value.
 */
public class TagFilter {
    public final String tagName;
    private final String tagValue;

    /**
     * Constructs a {@code TagFilter}.
     *
     * @param filterStr A valid tag filter string, either {@code <tag-name>} or {@code <tag-name>:<tag-value>}.
     */
    public TagFilter(String filterStr) {
        requireNonNull(filterStr);

        try {
            if (filterStr.contains(Tag.TAG_DELIMITER)) {
                Tag.validateTagPair(filterStr);
                this.tagName = Tag.getNameFromRaw(filterStr);
                this.tagValue = Tag.getValueFromRaw(filterStr);
            } else {
                String strippedFilter = filterStr.trim();
                Tag.validateTagName(strippedFilter);
                this.tagName = strippedFilter;
                this.tagValue = null;
            }
        } catch (IllegalValueException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Optional<String> getTagValue() {
        return Optional.ofNullable(tagValue);
    }

    public boolean hasTagValue() {
        return tagValue != null;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagFilter)) {
            return false;
        }

        TagFilter otherTagFilter = (TagFilter) other;
        return tagName.equals(otherTagFilter.tagName)
                && Objects.equals(tagValue, otherTagFilter.tagValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, tagValue);
    }

    @Override
    public String toString() {
        if (!hasTagValue()) {
            return tagName;
        }

        return tagName + ": " + tagValue;
    }
}
