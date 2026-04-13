package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * Represents a Person's email in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidEmail(String)}.
 */
public class Email {

    //@@author don-ko-reused
    //The regex is reused from
    // https://stackoverflow.com/questions/201323/how-can-i-validate-an-email-address-using-a-regular-expression
    public static final String VALIDATION_REGEX = "(?:[a-z0-9!#$%&'*+\\x2f=?^_`\\x7b-\\x7d~\\x2d]+(?:\\.[a-z0-9!#$%&'*"
            + "+\\x2f=?^_`\\x7b-\\x7d~\\x2d]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\"
            + "[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9\\x2d]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9"
            + "\\x2d]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9]"
            + ")|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9\\x2d]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-"
            + "\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
    //@@author

    public static final String INVALID_STRING = " is not a valid email.\n"
            + "Refer to the user guide for all the constraints an email should satisfy.";

    private static final String UNQUOTED_LOCAL_PART_SPECIAL_CHARACTERS = "!#$%&'*+/=?^_`{|}~-";

    public static final String MESSAGE_CONSTRAINTS = "Emails should be of the format local-part@domain "
            + "and adhere to the following constraints:\n"
            + "1. The local-part must be either:\n"
            + "    - unquoted: lowercase alphanumeric characters and these special characters "
            + "(" + UNQUOTED_LOCAL_PART_SPECIAL_CHARACTERS + "), with single periods allowed between segments only\n"
            + "    - quoted: enclosed in double quotes, with escaped ASCII characters allowed\n"
            + "2. This is followed by a '@' and then a domain which must be either:\n"
            + "    - a standard domain name made up of labels separated by periods, where each label starts and ends "
            + "with a lowercase alphanumeric character and may contain hyphens in between\n"
            + "    - a bracketed domain-literal (for example, an IPv4 address like [192.168.0.1])\n"
            + "3. Spaces are not allowed.";

    public final String value;

    /**
     * Constructs an {@code Email}.
     *
     * @param email A valid email address.
     */
    public Email(String email) {
        requireNonNull(email);
        try {
            isValidEmail(email);
        } catch (IllegalValueException e) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
        value = email;
    }

    /**
     * Checks if a given string is a valid email.
     */
    public static boolean isValidEmail(String test) throws IllegalValueException {
        if (test.toLowerCase().matches(VALIDATION_REGEX)) {
            return true;
        } else {
            throw new IllegalValueException('"' + test + '"' + INVALID_STRING);
        }
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Email)) {
            return false;
        }

        Email otherEmail = (Email) other;
        return value.equals(otherEmail.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
