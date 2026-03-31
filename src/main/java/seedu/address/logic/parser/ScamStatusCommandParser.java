package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ScamStatusCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.inputpatterns.InputPattern;
import seedu.address.logic.parser.inputpatterns.IntegerToken;
import seedu.address.logic.parser.inputpatterns.Token;

/**
 * Parses input arguments and creates a new ScamStatusCommand object
 */
public class ScamStatusCommandParser extends Parser<ScamStatusCommand> {
    @Override
    InputPattern createInputPattern() {
        ArrayList<Token> tokens = new ArrayList<Token>(List.of(
                new IntegerToken("index" , 1)
        ));

        return new InputPattern("scam", tokens);
    }

    @Override
    ScamStatusCommand parse(String args) throws ParseException {
        try {
            requireNonNull(args);
            InputPattern inputPattern = createInputPattern();
            inputPattern.assignSegmentsFromArgs(args.strip());

            Token indexToken = inputPattern.getTokenWithId("index");
            Index index = ParserUtil.parseIndex(indexToken.getAssignedSegment());
            return new ScamStatusCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ScamStatusCommand.MESSAGE_USAGE), pe);
        }
    }
}
