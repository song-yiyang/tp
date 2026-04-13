package seedu.address.model.person;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

import seedu.address.model.tag.Tag;

/**
 * Represents a Person's list of tag name-value pairs.
 */
public class TagList {
    private final TreeMap<String, String> tags;

    public TagList() {
        this.tags = new TreeMap<>();
    }

    /**
     * Creates a new TagList object by deep copying the contents of another TagList object.
     *
     * @param other The TagList object to be referenced.
     */
    public TagList(TagList other) {
        this.tags = new TreeMap<>();
        this.tags.putAll(other.tags);
    }

    /**
     * Creates a new TagList object from a variable number of tag strings.
     *
     * @param tagStrings Varargs argument of variable number of tag strings.
     */
    public TagList(String... tagStrings) {
        this.tags = new TreeMap<>();
        for (String tagString : tagStrings) {
            this.tags.put(Tag.getNameFromRaw(tagString), Tag.getValueFromRaw(tagString));
        }
    }

    /**
     * Creates a new TagList object from a list of Tags.
     *
     * @param tags List of Tags to be put into the new TagList.
     */
    public TagList(List<Tag> tags) {
        this.tags = new TreeMap<>();
        for (Tag tag : tags) {
            this.addTag(tag);
        }
    }

    /**
     * Adds a {@code Tag} to the {@code TagList}.
     */
    public void addTag(Tag tag) {
        this.tags.put(tag.getTagName(), tag.getTagValue());
    }

    /**
     * Edits a {@code Tag} in the {@code TagList}.
     */
    public void editTag(Tag tag) {
        this.tags.replace(tag.getTagName(), tag.getTagValue());
    }

    /**
     * Deletes a {@code Tag} in the {@code TagList}.
     */
    public void deleteTag(Tag tag) {
        this.tags.remove(tag.getTagName());
    }

    /**
     * Checks if any tag exists in the {@code TagList} with the corresponding tag name.
     */
    public boolean containsTagName(String tagName) {
        return this.tags.containsKey(tagName);
    }

    /**
     * Returns a list containing the values of tags with names containing the given search string,
     * ignoring case, if it exists.
     *
     * @param searchString The substring for the tag names to search for, case-insensitive.
     * @return A list containing the values of any tags found, or an empty list if none found.
     */
    public List<String> filterTagCaseInsensitive(String searchString) {
        return this.tags.entrySet().stream()
                .filter(tag -> tag.getKey().toLowerCase().contains(searchString.toLowerCase()))
                .map(Map.Entry::getValue)
                .toList();
    }

    /**
     * Returns an Optional containing the value of the tag with the given name, ignoring case, if it exists.
     *
     * @param tagName The name of the tag to search for, case-insensitive.
     * @return An Optional containing the value of the tag if found, or an empty Optional if not found.
     */
    public Optional<String> getTagValueCaseInsensitive(String tagName) {
        return this.tags.entrySet().stream()
                .filter(tag -> tag.getKey().equalsIgnoreCase(tagName))
                .findFirst()
                .map(Map.Entry::getValue);
    }

    /**
     * Returns a list of string representations of each tag stored in this {@code TagList}.
     *
     * @return List of strings corresponding to each tag.
     */
    public List<String> viewTags() {
        return this.tags.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue()).toList();
    }

    /**
     * Returns a string representation of the tags in a table format.
     *
     * @return A string containing the tags in a table format.
     */
    public String viewTagsTable() {
        StringBuilder builder = new StringBuilder();
        this.tags.entrySet().forEach(entry -> builder.append("  ")
                .append(entry.getKey())
                .append(":")
                .append(entry.getValue())
                .append("\n"));
        return builder.toString();
    }

    @Override
    public String toString() {
        return this.viewTags().toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof TagList)) {
            return false;
        }

        return this.toString().equals(other.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
