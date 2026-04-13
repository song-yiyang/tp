package seedu.address.logic.parser.inputpatterns;

import static seedu.address.logic.parser.CliSyntax.PARAM_ID_PHONE;

import seedu.address.commons.exceptions.IllegalValueException;

/**
 * A filter-only param that accepts any non-empty phone substring.
 * The value {@code NONE} is interpreted by {@code FilterCommand} as a missing phone field.
 */
public class FilterPhoneParam extends Param {

    public FilterPhoneParam(int minOccurrences, int maxOccurrences) {
        super(PARAM_ID_PHONE, minOccurrences, maxOccurrences);
    }

    @Override
    public String getPreview() {
        return PARAM_ID_PHONE + " <phone_substring|NONE>";
    }

    @Override
    public boolean valueMatches(String value) throws IllegalValueException {
        return !value.isBlank();
    }
}
