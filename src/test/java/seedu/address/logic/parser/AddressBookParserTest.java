package seedu.address.logic.parser;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import seedu.address.logic.commands.ScamStatusCommand;
import seedu.address.logic.commands.SortCommand;
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
        ClearCommand command = (ClearCommand) parser.parseCommand(ClearCommand.COMMAND_WORD);
        assertEquals(new ClearCommand(), command);
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
        ExitCommand command = (ExitCommand) parser.parseCommand(ExitCommand.COMMAND_WORD);
        assertEquals(new ExitCommand(), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        HelpCommand command = (HelpCommand) parser.parseCommand(HelpCommand.COMMAND_WORD);
        assertEquals(new HelpCommand(), command);
    }

    @Test
    public void parseCommand_list() throws Exception {
        ListCommand command = (ListCommand) parser.parseCommand(ListCommand.COMMAND_WORD);
        assertEquals(new ListCommand(), command);
    }

    @Test
    public void parseCommand_nuke() throws Exception {
        NukeCommand command = (NukeCommand) parser.parseCommand(NukeCommand.COMMAND_WORD);
        assertEquals(new NukeCommand(), command);
    }

    @Test
    public void parseCommand_filter() throws Exception {
        java.util.Map<FilterType, java.util.List<String>> criteria = new java.util.HashMap<>();
        criteria.put(FilterType.NAME, java.util.List.of("Alice"));
        FilterCommand command = (FilterCommand) parser.parseCommand(
                FilterCommand.COMMAND_WORD + " --name Alice");
        assertEquals(new FilterCommand(criteria, emptyList()), command);
    }

    @Test
    public void parseCommand_sort() throws Exception {
        SortCommand command = (SortCommand) parser.parseCommand("sort NAME --ASC --alpha");
        SortCommand.SortSpec expectedSpec = new SortCommand.SortSpec(
                SortCommand.SortTargetType.NAME,
                null,
                SortCommand.SortOrder.ASC,
                SortCommand.SortMode.ALPHA
        );
        assertEquals(new SortCommand(expectedSpec), command);
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

        ScamStatusCommand sCommand = new ScamStatusCommand(INDEX_FIRST_PERSON);
        ScamStatusCommand sParsed = (ScamStatusCommand) parser.parseCommand(
                ScamStatusCommand.COMMAND_WORD + " 1");
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
