package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.inputpatterns.InputPattern;

/**
 * Parses input arguments and creates a new {@link HelpCommand} object.
 * This command takes no arguments; any trailing arguments will cause a {@link ParseException}.
 */
public class HelpCommandParser extends Parser<HelpCommand> {

    @Override
    InputPattern createInputPattern() {
        return new InputPattern(HelpCommand.COMMAND_WORD, new ArrayList<>(List.of()));
    }

    @Override
    public HelpCommand parse(String args) throws ParseException {
        requireNonNull(args);
        InputPattern inputPattern = createInputPattern();
        inputPattern.assignSegmentsFromArgs(args.strip());
        return new HelpCommand();
    }
}
