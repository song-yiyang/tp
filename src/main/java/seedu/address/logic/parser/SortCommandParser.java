package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.SortCommand.SortMode;
import seedu.address.logic.commands.SortCommand.SortOrder;
import seedu.address.logic.commands.SortCommand.SortSpec;
import seedu.address.logic.commands.SortCommand.SortTargetType;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.inputpatterns.InputPattern;

/**
 * Parses input arguments and creates a new SortCommand object.
 */
public class SortCommandParser extends Parser<SortCommand> {

    @Override
    InputPattern createInputPattern() {
        return null;
    }

    @Override
    public SortCommand parse(String args) throws ParseException {
        String trimmedArgs = args == null ? "" : args.trim();

        SortTargetType targetType = SortTargetType.NAME;
        String tagName = null;
        SortOrder order = SortOrder.ASC;
        SortMode mode = SortMode.NUMBER;

        if (!trimmedArgs.isEmpty()) {
            String[] tokens = trimmedArgs.split("\\s+");

            boolean hasOrderFlag = false;
            boolean hasModeFlag = false;
            boolean flagsStarted = false;
            StringBuilder selectorBuilder = new StringBuilder();

            for (String token : tokens) {
                String lowerToken = token.toLowerCase();

                if (lowerToken.startsWith("--")) {
                    flagsStarted = true;
                    switch (lowerToken) {
                    case "--asc":
                    case "--desc":
                        if (hasOrderFlag) {
                            throw usageError();
                        }
                        hasOrderFlag = true;
                        order = lowerToken.equals("--desc") ? SortOrder.DESC : SortOrder.ASC;
                        break;

                    case "--number":
                    case "--alpha":
                        if (hasModeFlag) {
                            throw usageError();
                        }
                        hasModeFlag = true;
                        mode = lowerToken.equals("--alpha") ? SortMode.ALPHA : SortMode.NUMBER;
                        break;

                    default:
                        throw usageError();
                    }
                } else {
                    // Non-flag token after flags have started is an error
                    if (flagsStarted) {
                        throw usageError();
                    }
                    if (selectorBuilder.length() > 0) {
                        selectorBuilder.append(" ");
                    }
                    selectorBuilder.append(token);
                }
            }

            String selector = selectorBuilder.toString();
            if (!selector.isEmpty()) {
                String normalizedSelector = selector.toLowerCase();

                switch (normalizedSelector) {
                case "name":
                    targetType = SortTargetType.NAME;
                    break;
                case "phone":
                    targetType = SortTargetType.PHONE;
                    break;
                case "email":
                    targetType = SortTargetType.EMAIL;
                    break;
                default:
                    targetType = SortTargetType.TAG;
                    tagName = selector;
                    break;
                }
            }
        }

        return new SortCommand(new SortSpec(targetType, tagName, order, mode));
    }

    private ParseException usageError() {
        return new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }
}
