package seedu.address.logic.parser;

import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FilterCommand.FilterType;

/**
 * Contains unit tests for FilterCommandParser.
 */
public class FilterCommandParserTest {

    private final FilterCommandParser parser = new FilterCommandParser();

    @Test
    public void parse_noCriteria_returnsFilterCommandWithEmptyMap() throws Exception {
        Map<FilterType, List<String>> expectedCriteria = new HashMap<>();
        assertParseSuccess(parser, "", new FilterCommand(expectedCriteria));
    }

    @Test
    public void parse_singleNameCriteria_returnsFilterCommand() throws Exception {
        Map<FilterType, List<String>> expectedCriteria = new HashMap<>();
        expectedCriteria.put(FilterType.NAME, List.of("Alice"));
        assertParseSuccess(parser, " --name Alice", new FilterCommand(expectedCriteria));
    }

    @Test
    public void parse_multipleNameCriteria_returnsFilterCommand() throws Exception {
        Map<FilterType, List<String>> expectedCriteria = new HashMap<>();
        expectedCriteria.put(FilterType.NAME, Arrays.asList("Alice", "Benson"));
        assertParseSuccess(parser, " --name Alice --name Benson",
                new FilterCommand(expectedCriteria));
    }
}
