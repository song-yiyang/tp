package seedu.address.model.tag;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import seedu.address.commons.exceptions.IllegalValueException;

public class TagTest {

    // EP: null tag
    @Test
    public void constructor_tagIsNull_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Tag(null));
    }

    // EP: no delimiter
    @Test
    public void constructor_invalidTagString_throwsIllegalArgumentException() {
        String invalidTagString = "no_delimiter_tag";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagString));
    }

    // EP: empty tag name
    @Test
    public void constructor_invalidTagName_throwsIllegalArgumentException() {
        String invalidTagName = "  ";
        assertThrows(IllegalArgumentException.class, () -> new Tag(invalidTagName + ":investment banker"));
    }

    // EP: empty tag value
    @Test
    public void constructor_invalidTagValue_throwsIllegalArgumentException() {
        String invalidTagValue = "  ";
        assertThrows(IllegalArgumentException.class, () -> new Tag("job:" + invalidTagValue));
    }

    // EP: valid tag, with multiple "sub-EP"s
    @Test
    public void constructor_validInputs_success() {
        // alphanumeric name and value
        Tag tag = new Tag("job:engineer");
        assertEquals("job", tag.getTagName());
        assertEquals("engineer", tag.getTagValue());

        // name and value with spaces
        Tag tagWithSpaces = new Tag("job title:investment banker");
        assertEquals("job title", tagWithSpaces.getTagName());
        assertEquals("investment banker", tagWithSpaces.getTagValue());

        // value with special characters
        Tag tagWithSpecial = new Tag("income:$200,000");
        assertEquals("income", tagWithSpecial.getTagName());
        assertEquals("$200,000", tagWithSpecial.getTagValue());

        // leading and trailing whitespace
        Tag tagWithWhitespace = new Tag("here is a tab\t:   and many spaces");
        assertEquals("here is a tab", tagWithWhitespace.getTagName());
        assertEquals("and many spaces", tagWithWhitespace.getTagValue());
    }

    public void helperAssertThrows(Executable executable, String expectedMessage) {
        Exception e = assertThrows(IllegalValueException.class, executable);
        assertEquals(expectedMessage, e.getMessage());
    }

    @Test
    public void validityTagName() throws IllegalValueException {
        // EP: null tag name
        assertThrows(NullPointerException.class, () -> Tag.isValidTagName(null));

        // EP: all whitespace
        helperAssertThrows(() -> Tag.isValidTagName(" \t\n"), Tag.WHITESPACE_NAME_CONSTRAINTS);

        // EP: all whitespace with delimiter
        helperAssertThrows(() -> Tag.isValidTagName(" \t:\n"), Tag.NAME_NO_DELIMITER_CONSTRAINT);

        // EP: non-whitespace but has delimiter
        helperAssertThrows(() -> Tag.isValidTagName("tab\t:newline\n"), Tag.NAME_NO_DELIMITER_CONSTRAINT);

        // EP: too long
        helperAssertThrows(() -> Tag.isValidTagName("a".repeat(100)),
                '"' + "a".repeat(100) + '"'
                        + " is too long, it should not exceed "
                        + Tag.MAX_LENGTH + " characters.");

        // EP: contains "name", "phone" or "email"
        helperAssertThrows(() -> Tag.isValidTagName("name"), Tag.ILLEGAL_NAME_CONSTRAINTS);
        helperAssertThrows(() -> Tag.isValidTagName("phone"), Tag.ILLEGAL_NAME_CONSTRAINTS);
        helperAssertThrows(() -> Tag.isValidTagName("email"), Tag.ILLEGAL_NAME_CONSTRAINTS);
        helperAssertThrows(() -> Tag.isValidTagName("Name"), Tag.ILLEGAL_NAME_CONSTRAINTS);
        helperAssertThrows(() -> Tag.isValidTagName("Phone"), Tag.ILLEGAL_NAME_CONSTRAINTS);
        helperAssertThrows(() -> Tag.isValidTagName("Email"), Tag.ILLEGAL_NAME_CONSTRAINTS);
        helperAssertThrows(() -> Tag.isValidTagName("NAME"), Tag.ILLEGAL_NAME_CONSTRAINTS);
        helperAssertThrows(() -> Tag.isValidTagName("PHONE"), Tag.ILLEGAL_NAME_CONSTRAINTS);
        helperAssertThrows(() -> Tag.isValidTagName("EMAIL"), Tag.ILLEGAL_NAME_CONSTRAINTS);

        // EP: Valid, non-whitespace with no delimiter
        assertTrue(Tag.isValidTagName("job"));
    }

    @Test
    public void validityTagValue() throws IllegalValueException {
        // EP: null tag value
        assertThrows(NullPointerException.class, () -> Tag.isValidTagValue(null));

        // EP: all whitespace
        helperAssertThrows(() -> Tag.isValidTagValue(" \t\n"), Tag.WHITESPACE_VALUE_CONSTRAINTS);

        // EP: all whitespace with delimiter
        helperAssertThrows(() -> Tag.isValidTagValue(" \t:\n"), Tag.VALUE_NO_DELIMITER_CONSTRAINT);

        // EP: non-whitespace but has delimiter
        helperAssertThrows(() -> Tag.isValidTagValue("tab\t:newline\n"), Tag.VALUE_NO_DELIMITER_CONSTRAINT);

        // EP: too long
        helperAssertThrows(() -> Tag.isValidTagValue("a".repeat(100)),
                '"' + "a".repeat(100) + '"'
                        + " is too long, it should not exceed "
                        + Tag.MAX_LENGTH + " characters.");

        // EP: Valid, non-whitespace with no delimiter
        assertTrue(Tag.isValidTagValue("engineer"));
    }

    // EP: Invalid tag pair
    @Test
    public void invalidTagPair() throws IllegalValueException {
        // EP: invalid tag string
        helperAssertThrows(() -> Tag.isValidTagPair("too:many:delimiters"), Tag.ONE_DELIMITER_CONSTRAINT);

        // EP: invalid tag name
        helperAssertThrows(() -> Tag.isValidTagPair("\t\n :valid value"), Tag.WHITESPACE_NAME_CONSTRAINTS);

        // EP: valid tag name, invalid tag value
        helperAssertThrows(() -> Tag.isValidTagPair("valid name:\n\t "), Tag.WHITESPACE_VALUE_CONSTRAINTS);

        // EP: valid tag name and value
        assertTrue(Tag.isValidTagPair("valid name:valid value"));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        Tag tag = new Tag("job:engineer");
        assertTrue(tag.equals(tag));
    }

    @Test
    public void equals_null_returnsFalse() {
        Tag tag = new Tag("tagname:value");
        assertNotEquals(null, tag);
    }

    @Test
    public void equals_nonTagClass_returnsFalse() {
        String tagString = "tagname:value";
        Tag tag = new Tag(tagString);
        assertFalse(tag.equals(tagString));
    }



    @Test
    public void equals_sameNameAndValue_returnsTrue() {
        Tag tag1 = new Tag("job:engineer");
        Tag tag2 = new Tag("job:engineer");
        assertEquals(tag1, tag2);
    }

    @Test
    public void equals_differentValue_returnsFalse() {
        Tag tag1 = new Tag("job:engineer");
        Tag tag2 = new Tag("job:doctor");
        assertNotEquals(tag1, tag2);
    }

    @Test
    public void equals_differentName_returnsFalse() {
        Tag tag1 = new Tag("job:engineer");
        Tag tag2 = new Tag("occupation:engineer");
        assertNotEquals(tag1, tag2);
    }

    @Test
    public void hashCode_equalTags_sameHashCode() {
        Tag tag1 = new Tag("job:engineer");
        Tag tag2 = new Tag("job:engineer");
        assertEquals(tag1.hashCode(), tag2.hashCode());
    }

    @Test
    public void toString_validTag_correctFormat() {
        Tag tag = new Tag("job:engineer");
        assertEquals("job: engineer", tag.toString());

        Tag tagWithSpecialChars = new Tag("income:$200,000");
        assertEquals("income: $200,000", tagWithSpecialChars.toString());
    }
}
