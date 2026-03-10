package seedu.address.logic.parser.inputpatterns;

import seedu.address.model.person.Email;

/**
 * A Param of the id "-email"
 */
public class EmailParam extends Param {

    public EmailParam(int minOccurences, int maxOccurences) {
        super("-email", minOccurences, maxOccurences);
    }

    @Override
    public String getPreview() {
        return "-email <email>";
    }

    @Override
    boolean valueMatches(String value) {
        return Email.isValidEmail(value);
    }
}

