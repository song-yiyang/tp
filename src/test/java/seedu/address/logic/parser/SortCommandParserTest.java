package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.SortCommand;
import seedu.address.logic.commands.SortCommand.SortMode;
import seedu.address.logic.commands.SortCommand.SortOrder;
import seedu.address.logic.commands.SortCommand.SortSpec;
import seedu.address.logic.commands.SortCommand.SortTargetType;

public class SortCommandParserTest {

    private final SortCommandParser parser = new SortCommandParser();

    @Test
    public void parse_emptyArgs_usesDefaults() {
        SortSpec expected = new SortSpec(SortTargetType.NAME, null, SortOrder.ASC, SortMode.NUMBER);
        assertParseSuccess(parser, "   ", new SortCommand(expected));
    }

    @Test
    public void parse_validArgs_returnsSortCommand() {
        SortSpec phoneDesc = new SortSpec(SortTargetType.PHONE, null, SortOrder.DESC, SortMode.NUMBER);
        assertParseSuccess(parser, "phone --desc --NUMBER", new SortCommand(phoneDesc));

        SortSpec tagAlpha = new SortSpec(SortTargetType.TAG, "income", SortOrder.ASC, SortMode.ALPHA);
        assertParseSuccess(parser, "income --alpha", new SortCommand(tagAlpha));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "NAME --ASC --desc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "NAME --NUMBER --alpha",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
        assertParseFailure(parser, "NAME --weird",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_multiWordTagName_returnsSortCommand() {
        // Two-word tag name
        SortSpec twoWordTag = new SortSpec(SortTargetType.TAG, "my tag", SortOrder.ASC, SortMode.NUMBER);
        assertParseSuccess(parser, "my tag", new SortCommand(twoWordTag));

        // Two-word tag name with flags
        SortSpec twoWordTagDesc = new SortSpec(SortTargetType.TAG, "my tag", SortOrder.DESC, SortMode.NUMBER);
        assertParseSuccess(parser, "my tag --desc", new SortCommand(twoWordTagDesc));

        // Three-word tag name with all flags
        SortSpec threeWordTag = new SortSpec(SortTargetType.TAG, "my long tag", SortOrder.DESC, SortMode.ALPHA);
        assertParseSuccess(parser, "my long tag --desc --alpha", new SortCommand(threeWordTag));

        // Multi-word tag name with flags in different order
        SortSpec multiWordAlphaAsc = new SortSpec(SortTargetType.TAG, "some tag", SortOrder.ASC, SortMode.ALPHA);
        assertParseSuccess(parser, "some tag --alpha --asc", new SortCommand(multiWordAlphaAsc));
    }

    @Test
    public void parse_flagsOnly_usesNameAsDefault() {
        // Flags without selector should default to NAME
        SortSpec descOnly = new SortSpec(SortTargetType.NAME, null, SortOrder.DESC, SortMode.NUMBER);
        assertParseSuccess(parser, "--desc", new SortCommand(descOnly));

        SortSpec alphaOnly = new SortSpec(SortTargetType.NAME, null, SortOrder.ASC, SortMode.ALPHA);
        assertParseSuccess(parser, "--alpha", new SortCommand(alphaOnly));

        SortSpec bothFlags = new SortSpec(SortTargetType.NAME, null, SortOrder.DESC, SortMode.ALPHA);
        assertParseSuccess(parser, "--desc --alpha", new SortCommand(bothFlags));
    }

    @Test
    public void parse_mixedSelectorAndFlagsInterspersed_throwsParseException() {
        // Non-flag word after flag should fail
        assertParseFailure(parser, "word1 --desc word2",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));

        // Multiple non-flag words separated by flags should fail
        assertParseFailure(parser, "tag1 --alpha tag2 --desc",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));

        // Flag then word should fail
        assertParseFailure(parser, "--desc mytag",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SortCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_reservedKeywordsAsMultiWord_treatedAsTag() {
        // "name something" should be treated as a tag, not NAME target
        SortSpec namePrefix = new SortSpec(SortTargetType.TAG, "name something", SortOrder.ASC, SortMode.NUMBER);
        assertParseSuccess(parser, "name something", new SortCommand(namePrefix));

        // "phone number" should be treated as a tag
        SortSpec phonePrefix = new SortSpec(SortTargetType.TAG, "phone number", SortOrder.ASC, SortMode.NUMBER);
        assertParseSuccess(parser, "phone number", new SortCommand(phonePrefix));

        // "email address" should be treated as a tag
        SortSpec emailPrefix = new SortSpec(SortTargetType.TAG, "email address", SortOrder.DESC, SortMode.ALPHA);
        assertParseSuccess(parser, "email address --desc --alpha", new SortCommand(emailPrefix));
    }

    @Test
    public void parse_singleWordReservedKeywords_stillWork() {
        // Single "name" should still be NAME target
        SortSpec nameTarget = new SortSpec(SortTargetType.NAME, null, SortOrder.ASC, SortMode.NUMBER);
        assertParseSuccess(parser, "name", new SortCommand(nameTarget));

        // Single "phone" should still be PHONE target
        SortSpec phoneTarget = new SortSpec(SortTargetType.PHONE, null, SortOrder.DESC, SortMode.NUMBER);
        assertParseSuccess(parser, "phone --desc", new SortCommand(phoneTarget));

        // Single "email" should still be EMAIL target
        SortSpec emailTarget = new SortSpec(SortTargetType.EMAIL, null, SortOrder.ASC, SortMode.ALPHA);
        assertParseSuccess(parser, "email --alpha", new SortCommand(emailTarget));
    }
}
