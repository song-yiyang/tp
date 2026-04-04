package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.inputpatterns.InputPattern;
import seedu.address.logic.parser.inputpatterns.Token;

/**
 * Parses input arguments and creates a new {@link ClearCommand} object.
 * This command takes no arguments; any trailing arguments will cause a {@link ParseException}.
 */
public class ClearCommandParser extends Parser<ClearCommand> {

    @Override
    InputPattern createInputPattern() {
        // There should be no arguments after clear
        ArrayList<Token> tokens = new ArrayList<>(List.of());

        return new InputPattern(ClearCommand.COMMAND_WORD, tokens);
    }

    /**
     * Parses the given {@code String} of arguments in the context of the ClearCommand
     * and returns a ClearCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public ClearCommand parse(String args) throws ParseException {
        requireNonNull(args);
        InputPattern inputPattern = createInputPattern();
        inputPattern.assignSegmentsFromArgs(args.strip());

        return new ClearCommand();
    }
}
