package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.SILENT;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.model.person.Person;
import seedu.address.testutil.PersonBuilder;

public class AddCommandParserTest {
    private AddCommandParser parser = new AddCommandParser();

    @Test
    public void parse_nameOnlyPresent_success() {
        Person expectedPerson = new PersonBuilder(SILENT).build();

        // whitespace only preamble
        assertParseSuccess(parser, SILENT.getName().toString(), new AddCommand(expectedPerson));
    }

}
