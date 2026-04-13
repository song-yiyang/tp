package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;

public class EmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    public void helperAssertThrows(String test) {
        Exception e = assertThrows(IllegalValueException.class, () -> Email.isValidEmail(test));
        assertEquals('"' + test + '"' + Email.INVALID_STRING, e.getMessage());
    }

    @Test
    public void validateEmail() throws IllegalValueException {
        // null email
        assertThrows(NullPointerException.class, () -> Email.isValidEmail(null));

        // blank email
        helperAssertThrows(""); // empty string
        helperAssertThrows(" "); // spaces only

        // missing parts
        helperAssertThrows("@example.com"); // local part
        helperAssertThrows("peterjackexample.com"); // '@' symbol
        helperAssertThrows("peterjack@"); // domain name

        // invalid parts
        helperAssertThrows("peterjack@-"); // invalid domain name
        helperAssertThrows("peterjack@exam_ple.com"); // underscore in domain name
        helperAssertThrows("peter jack@example.com"); // spaces in local part
        helperAssertThrows("peterjack@exam ple.com"); // spaces in domain name
        helperAssertThrows("peterjack@example.com "); // trailing space
        helperAssertThrows("peterjack@@example.com"); // double '@' symbol
        helperAssertThrows("peter@jack@example.com"); // '@' symbol in local part
        helperAssertThrows("peter..jack@example.com"); // local part has two consecutive periods
        helperAssertThrows("peter.jack.@example.com"); // local part has two consecutive periods
        helperAssertThrows("peterjack@example@com"); // '@' symbol in domain name
        helperAssertThrows("peterjack@.example.com"); // domain name starts with a period
        helperAssertThrows("peterjack@example.com."); // domain name ends with a period
        helperAssertThrows("peterjack@-example.com"); // domain name starts with a hyphen
        helperAssertThrows("peterjack@example.com-"); // domain name ends with a hyphen

        helperAssertThrows("peterjack@example-.com"); // domain label ends with a hyphen
        helperAssertThrows(".peterjack@example.com"); // local part starts with a period
        helperAssertThrows("a@bc"); // minimal
        helperAssertThrows("test@localhost"); // alphabets only
        helperAssertThrows("(@)@example.com"); // special characters in local part without quotes

        // valid email
        assertTrue(Email.isValidEmail("\"\"@example.com")); // quoted local part with special characters
        assertTrue(Email.isValidEmail("peterjack@example.c")); // minimal domain name
        assertTrue(Email.isValidEmail("PeterJack_1190@example.com")); // underscore in local part
        assertTrue(Email.isValidEmail("PeterJack.1190@example.com")); // period in local part
        assertTrue(Email.isValidEmail("PeterJack+1190@example.com")); // '+' symbol in local part
        assertTrue(Email.isValidEmail("PeterJack-1190@example.com")); // hyphen in local part
        assertTrue(Email.isValidEmail("PeterJack-1190@127.0.0.1")); // IP address as domain name
        assertTrue(Email.isValidEmail("a1+be.d@example1.com")); // mixture of alphanumeric and special characters
        assertTrue(Email.isValidEmail("o'hara+dev-team@example-domain.com")); // apostrophe, plus and hyphens
        assertTrue(Email.isValidEmail("customer/department=shipping@example-domain.com")); // slash and equals
        assertTrue(Email.isValidEmail("mailbox%ops@example-domain.co.uk")); // percent and subdomains
        assertTrue(Email.isValidEmail("peter_jack@very-very-very-long-example.com")); // long domain name
        assertTrue(Email.isValidEmail("if.you.dream.it_you.can.do.it@example.com")); // long local part
        assertTrue(Email.isValidEmail("e1234567@u.nus.edu")); // more than one period in domain
    }

    @Test
    public void equals() {
        Email email = new Email("valid@email.com");

        // same values -> returns true
        assertTrue(email.equals(new Email("valid@email.com")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("other.valid@email.com")));
    }
}
