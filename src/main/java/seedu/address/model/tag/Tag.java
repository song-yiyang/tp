package seedu.address.model.tag;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Tag in the address book.
 */
public class Tag {

    public static final String TAG_DELIMITER = ":";
    public static final String NO_DELIMITER_REGEX = ".[^" + TAG_DELIMITER + "]*$";
    public static final String ONE_DELIMITER_REGEX =
            String.format("^[^%s]*%s[^%s]*$", TAG_DELIMITER, TAG_DELIMITER, TAG_DELIMITER);

    public static final int MAX_LENGTH = 60;

    public static final List<String> BANNED_NAMES = List.of("name", "phone", "email");

    public static final String ONE_DELIMITER_CONSTRAINT =
            "Tag name-value pairs should contain exactly one occurrence of the delimiter " + TAG_DELIMITER;
    public static final String DELETE_TAG_NAME_ONLY =
            "Tags to be deleted should be represented by only its name, without any occurrence of the delimiter "
                    + TAG_DELIMITER;
    public static final String NAME_NO_DELIMITER_CONSTRAINT =
            "Tag names should not contain any occurrence of the delimiter " + TAG_DELIMITER;
    public static final String VALUE_NO_DELIMITER_CONSTRAINT =
            "Tag values should not contain any occurrence of the delimiter " + TAG_DELIMITER;
    public static final String WHITESPACE_NAME_CONSTRAINTS =
            "Tag names should contain at least one non-whitespace character.";
    public static final String ILLEGAL_NAME_CONSTRAINTS =
            "Tag names must not equal, case-insensitive, any one of " + BANNED_NAMES.toString() + ".";
    public static final String WHITESPACE_VALUE_CONSTRAINTS =
            "Tag values should contain at least one non-whitespace character.";

    private final String tagName;
    private final String tagValue;

    /**
     * Constructs a {@code Tag}.
     *
     * @param tagString A valid tag string, with name, exactly one delimiter and value.
     */
    public Tag(String tagString) {
        try {
            requireNonNull(tagString);
            Tag.isValidTagString(tagString);

            String tagName = Tag.getNameFromRaw(tagString);
            String tagValue = Tag.getValueFromRaw(tagString);

            requireNonNull(tagName);
            requireNonNull(tagValue);
            Tag.isValidLength(tagName);
            Tag.isValidLength(tagValue);
            Tag.isValidTagName(tagName);
            Tag.isValidTagValue(tagValue);

            this.tagName = tagName;
            this.tagValue = tagValue;
        } catch (IllegalValueException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }


    /**
     * Returns the tag name of this tag.
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * Returns the tag value of this tag.
     */
    public String getTagValue() {
        return tagValue;
    }


    /**
     * Retrieves tag name from raw string representing the tag.
     *
     * @param tag Raw tag string.
     * @return Tag name.
     */
    public static String getNameFromRaw(String tag) {
        return tag.split(TAG_DELIMITER)[0].trim();
    }

    /**
     * Retrieves tag value from raw string representing the tag.
     *
     * @param tag Raw tag string.
     * @return Tag value.
     */
    public static String getValueFromRaw(String tag) {
        return tag.split(TAG_DELIMITER)[1].trim();
    }


    /**
     * Checks if a given string contains at most {@code maxLength} number of characters.
     *
     * @param test The string to be tested.
     * @return True if the string is short enough.
     * @throws IllegalValueException If the string is too long, with specific error message.
     */
    public static boolean isValidLength(String test) throws IllegalValueException {
        if (test.length() > Tag.MAX_LENGTH) {
            throw new IllegalValueException('"' + test + '"' + " is too long, it should not exceed "
                    + MAX_LENGTH + " characters.");
        }
        return true;
    }

    /**
     * Checks if a given string contains exactly one occurrence of the tag delimiter.
     *
     * @param test The tag string to be tested.
     * @return True if string is valid.
     * @throws IllegalValueException If string is not valid, with specific error message.
     */
    public static boolean isValidTagString(String test) throws IllegalValueException {
        if (!test.matches(ONE_DELIMITER_REGEX)) {
            throw new IllegalValueException(ONE_DELIMITER_CONSTRAINT);
        }
        if (test.charAt(0) == TAG_DELIMITER.charAt(0)) {
            throw new IllegalValueException(WHITESPACE_NAME_CONSTRAINTS);
        }
        if (test.charAt(test.length() - 1) == TAG_DELIMITER.charAt(0)) {
            throw new IllegalValueException(WHITESPACE_VALUE_CONSTRAINTS);
        }
        return true;
    }

    /**
     * Checks if a given string is a valid tag name.
     *
     * @param test The name to be tested.
     * @return True if name is valid.
     * @throws IllegalValueException If name is not valid, with specific error message.
     */
    public static boolean isValidTagName(String test) throws IllegalValueException {
        isValidLength(test);
        if (test.isBlank()) {
            throw new IllegalValueException(WHITESPACE_NAME_CONSTRAINTS);
        }
        if (!test.matches(NO_DELIMITER_REGEX)) {
            throw new IllegalValueException(NAME_NO_DELIMITER_CONSTRAINT);
        }
        if (BANNED_NAMES.contains(test.toLowerCase())) {
            throw new IllegalValueException(ILLEGAL_NAME_CONSTRAINTS);
        }
        return true;
    }

    /**
     * Checks if a given string contains the tag delimiter.
     *
     * @param test The string to be tested.
     * @return True if string does not contain the tag delimiter.
     * @throws IllegalValueException If string contains the tag delimiter, with specific error message.
     */
    public static boolean isValidDeleteNameWithoutDelimiter(String test) throws IllegalValueException {
        if (test.contains(TAG_DELIMITER)) {
            throw new IllegalValueException(DELETE_TAG_NAME_ONLY);
        }
        return true;
    }

    /**
     * Checks if a given string is a valid tag value.
     *
     * @param test The value to be tested.
     * @return True if the string is a valid tag value.
     * @throws IllegalValueException If the string is not a valid tag value, with specific error message.
     */
    public static boolean isValidTagValue(String test) throws IllegalValueException {
        isValidLength(test);
        if (test.isBlank()) {
            throw new IllegalValueException(WHITESPACE_VALUE_CONSTRAINTS);
        }
        if (!test.matches(NO_DELIMITER_REGEX)) {
            throw new IllegalValueException(VALUE_NO_DELIMITER_CONSTRAINT);
        }
        return true;
    }

    /**
     * Checks if a given string is a fully valid tag.
     *
     * @param test Tag string to be tested.
     * @return True if the string is a valid tag string.
     * @throws IllegalValueException If the string is not a valid tag string, with specific error message.
     */
    public static boolean isValidTagPair(String test) throws IllegalValueException {
        Tag.isValidTagString(test);

        String tagName = Tag.getNameFromRaw(test);
        String tagValue = Tag.getValueFromRaw(test);
        return Tag.isValidTagName(tagName) && Tag.isValidTagValue(tagValue);
    }

    /**
     * Checks if a given {@code Tag} is a fully valid tag, without throwing errors.
     *
     * @param test Tag to be tested.
     * @return Boolean indicating validity of tag.
     */
    public static boolean isValidTagPairWithoutErrors(Tag test) {
        try {
            Tag.isValidTagName(test.tagName);
            Tag.isValidTagValue(test.tagValue);
            return true;
        } catch (IllegalValueException e) {
            return false;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Tag)) {
            return false;
        }

        Tag otherTag = (Tag) other;
        return tagName.equals(otherTag.tagName) && tagValue.equals(otherTag.tagValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, tagValue);
    }

    /**
     * Format state as text for viewing.
     */
    public String toString() {
        return tagName + ": " + tagValue;
    }

}
