package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.inputpatterns.InputPattern;

/**
 * Parses input arguments and creates a new {@link ExitCommand} object.
 * This command takes no arguments; any trailing arguments will cause a {@link ParseException}.
 */
public class ExitCommandParser extends Parser<ExitCommand> {

    @Override
    InputPattern createInputPattern() {
        return new InputPattern(ExitCommand.COMMAND_WORD, new ArrayList<>(List.of()));
    }

    @Override
    public ExitCommand parse(String args) throws ParseException {
        requireNonNull(args);
        InputPattern inputPattern = createInputPattern();
        inputPattern.assignSegmentsFromArgs(args.strip());
        return new ExitCommand();
    }
}
