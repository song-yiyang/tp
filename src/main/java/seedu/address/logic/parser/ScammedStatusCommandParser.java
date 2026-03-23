package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ScammedStatusCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.inputpatterns.InputPattern;
import seedu.address.logic.parser.inputpatterns.IntegerToken;
import seedu.address.logic.parser.inputpatterns.Token;

/**
 * Parses input arguments and creates a new ScammedStatusCommand object
 */
public class ScammedStatusCommandParser extends Parser<ScammedStatusCommand> {
    @Override
    InputPattern createInputPattern() {
        ArrayList<Token> tokens = new ArrayList<Token>(List.of(
                new IntegerToken("index" , 1, 100)
        ));

        return new InputPattern("scammed", tokens);
    }

    @Override
    ScammedStatusCommand parse(String args) throws ParseException {
        try {
            requireNonNull(args);
            InputPattern inputPattern = createInputPattern();
            inputPattern.assignSegmentsFromArgs(args.strip());

            Token indexToken = inputPattern.getTokenWithId("index");
            Index index = ParserUtil.parseIndex(indexToken.getAssignedSegment());
            return new ScammedStatusCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScammedStatusCommand.MESSAGE_USAGE), pe);
        }
    }
}
