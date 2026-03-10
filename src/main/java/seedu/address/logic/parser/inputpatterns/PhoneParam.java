package seedu.address.logic.parser.inputpatterns;

import seedu.address.model.person.Phone;

/**
 * A param that takes in the phone, with id "-phone"
 */
public class PhoneParam extends Param {

    public PhoneParam(int minOccurences, int maxOccurences) {
        super("-phone", minOccurences, maxOccurences);
    }

    @Override
    public String getPreview() {
        return "-phone <phone_number>";
    }

    @Override
    boolean valueMatches(String value) {
        return Phone.isValidPhone(value);
    }
}
