package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ClearStatusCommand;
import seedu.address.logic.commands.IgnoreStatusCommand;
import seedu.address.logic.commands.ScamStatusCommand;
import seedu.address.logic.commands.TargetStatusCommand;

/**
 * Contains integration tests (interaction with the Model) and unit tests for various StatusCommandParser.
 */
public class StatusCommandParsersTest {
    @Test
    public void invalid_arguments() {
        ClearStatusCommandParser csParser = new ClearStatusCommandParser();
        TargetStatusCommandParser tParser = new TargetStatusCommandParser();
        ScamStatusCommandParser sParser = new ScamStatusCommandParser();
        IgnoreStatusCommandParser iParser = new IgnoreStatusCommandParser();

        assertParseFailure(csParser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearStatusCommand.MESSAGE_USAGE));
        assertParseFailure(csParser, "1 1 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClearStatusCommand.MESSAGE_USAGE));
        assertParseFailure(tParser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TargetStatusCommand.MESSAGE_USAGE));
        assertParseFailure(tParser, "1 1 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, TargetStatusCommand.MESSAGE_USAGE));
        assertParseFailure(sParser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScamStatusCommand.MESSAGE_USAGE));
        assertParseFailure(sParser, "1 1 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScamStatusCommand.MESSAGE_USAGE));
        assertParseFailure(iParser, "",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, IgnoreStatusCommand.MESSAGE_USAGE));
        assertParseFailure(iParser, "1 1 1",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, IgnoreStatusCommand.MESSAGE_USAGE));
    }

    @Test
    public void success() {
        ClearStatusCommandParser csParser = new ClearStatusCommandParser();
        TargetStatusCommandParser tParser = new TargetStatusCommandParser();
        ScamStatusCommandParser sParser = new ScamStatusCommandParser();
        IgnoreStatusCommandParser iParser = new IgnoreStatusCommandParser();

        assertParseSuccess(csParser, "1", new ClearStatusCommand(INDEX_FIRST_PERSON));
        assertParseSuccess(tParser, "1", new TargetStatusCommand(INDEX_FIRST_PERSON));
        assertParseSuccess(sParser, "1", new ScamStatusCommand(INDEX_FIRST_PERSON));
        assertParseSuccess(iParser, "1", new IgnoreStatusCommand(INDEX_FIRST_PERSON));
    }
}
