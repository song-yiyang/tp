package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.ClearStatusCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditPersonDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FilterCommand;
import seedu.address.logic.commands.FilterCommand.FilterType;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.IgnoreStatusCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.NukeCommand;
import seedu.address.logic.commands.ScammedStatusCommand;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.commands.TargetStatusCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Person person = new PersonBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(PersonUtil.getAddCommand(person));
        assertEquals(new AddCommand(person), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Person person = new PersonBuilder().build();
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder(person).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditPersonDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_filter() throws Exception {
        java.util.Map<FilterType, java.util.List<String>> criteria = new java.util.HashMap<>();
        criteria.put(FilterType.NAME, java.util.List.of("Alice"));
        FilterCommand command = (FilterCommand) parser.parseCommand(
                FilterCommand.COMMAND_WORD + " --name Alice");
        assertEquals(new FilterCommand(criteria), command);
    }

    @Test
    public void parseCommand_nuke() throws Exception {
        assertTrue(parser.parseCommand(NukeCommand.COMMAND_WORD) instanceof NukeCommand);
        assertTrue(parser.parseCommand(NukeCommand.COMMAND_WORD + " now") instanceof NukeCommand);
    }

    @Test
    public void parseCommand_tag() throws Exception {
        List<Tag> tagsToAdd = List.of(new Tag("friend:John"));
        List<Tag> tagsToEdit = List.of(new Tag("colleague:Jane"));
        List<Tag> tagsToDelete = List.of(new Tag("office:dummy"));
        TagCommand command = new TagCommand(INDEX_FIRST_PERSON, tagsToAdd, tagsToEdit, tagsToDelete);
        TagCommand parsed = (TagCommand) parser.parseCommand(
                TagCommand.COMMAND_WORD + " 1 --add friend:John --edit colleague:Jane --delete office");
        assertEquals(parsed, command);
    }

    @Test
    public void parseCommand_status() throws Exception {
        TargetStatusCommand tCommand = new TargetStatusCommand(INDEX_FIRST_PERSON);
        TargetStatusCommand tParsed = (TargetStatusCommand) parser.parseCommand(
                TargetStatusCommand.COMMAND_WORD + " 1");
        assertEquals(tParsed, tCommand);

        ClearStatusCommand cCommand = new ClearStatusCommand(INDEX_FIRST_PERSON);
        ClearStatusCommand cParsed = (ClearStatusCommand) parser.parseCommand(
                ClearStatusCommand.COMMAND_WORD + " 1");
        assertEquals(cParsed, cCommand);

        ScammedStatusCommand sCommand = new ScammedStatusCommand(INDEX_FIRST_PERSON);
        ScammedStatusCommand sParsed = (ScammedStatusCommand) parser.parseCommand(
                ScammedStatusCommand.COMMAND_WORD + " 1");
        assertEquals(sParsed, sCommand);

        IgnoreStatusCommand iCommand = new IgnoreStatusCommand(INDEX_FIRST_PERSON);
        IgnoreStatusCommand iParsed = (IgnoreStatusCommand) parser.parseCommand(
                IgnoreStatusCommand.COMMAND_WORD + " 1");
        assertEquals(iParsed, iCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
