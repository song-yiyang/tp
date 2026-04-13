package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.NukeCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.inputpatterns.InputPattern;

/**
 * Parses input arguments and creates a new {@link NukeCommand} object.
 * This command takes no arguments; any trailing arguments will cause a {@link ParseException}.
 */
public class ListCommandParser extends Parser<ListCommand> {

    @Override
    InputPattern createInputPattern() {
        return new InputPattern(ListCommand.COMMAND_WORD, new ArrayList<>(List.of()));
    }

    @Override
    public ListCommand parse(String args) throws ParseException {
        requireNonNull(args);
        InputPattern inputPattern = createInputPattern();
        inputPattern.assignSegmentsFromArgs(args.strip());
        return new ListCommand();
    }
}
