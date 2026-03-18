package seedu.address.logic.parser;

import static seedu.address.logic.parser.CliSyntax.PARAM_ID_NAME;
import static seedu.address.logic.parser.CliSyntax.PARAM_ID_PHONE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FilterCommand.FilterType;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.inputpatterns.InputPattern;
import seedu.address.logic.parser.inputpatterns.NameParam;
import seedu.address.logic.parser.inputpatterns.Param;
import seedu.address.logic.parser.inputpatterns.PhoneParam;
import seedu.address.logic.parser.inputpatterns.Token;

/**
 * Parses input arguments and creates a new FilterCommand object.
 * Supports filtering by name and phone parameters.
 */
public class FilterCommandParser extends Parser<FilterCommand> {

    /**
     * Defines the InputPattern of the parser for FilterCommand.
     *
     * @return An InputPattern
     */
    @Override
    InputPattern createInputPattern() {
        ArrayList<Token> tokens = new ArrayList<>();

        ArrayList<Param> params = new ArrayList<>(List.of(
                new NameParam(0, 100),
                new PhoneParam(0, 100)
        ));

        return new InputPattern("filter", tokens, params);
    }

    /**
     * Parses {@code userInput} into a FilterCommand and returns it.
     *
     * @throws ParseException if {@code userInput} does not conform the expected format
     */
    @Override
    FilterCommand parse(String args) throws ParseException {
        InputPattern inputPattern = createInputPattern();
        inputPattern.assignSegmentsFromArgs(args.strip());

        Map<FilterType, List<String>> filterCriterion = new HashMap<>();

        Param nameParam = inputPattern.getParamWithId(PARAM_ID_NAME);
        List<String> nameFilter = nameParam.getValues();
        if (!nameFilter.isEmpty()) {
            filterCriterion.put(FilterType.NAME, nameFilter);
        }

        Param phoneParam = inputPattern.getParamWithId(PARAM_ID_PHONE);
        List<String> phoneFilter = phoneParam.getValues();
        if (!phoneFilter.isEmpty()) {
            filterCriterion.put(FilterType.PHONE, phoneFilter);
        }

        return new FilterCommand(filterCriterion);
    }
}
