package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Person;
import seedu.address.model.person.predicates.NameContainsKeywordsPredicate;
import seedu.address.testutil.PersonBuilder;

public class SortCommandTest {

    @Test
    public void equals() {
        SortCommand.SortSpec firstSpec = new SortCommand.SortSpec(
                SortCommand.SortTargetType.NAME,
                null,
                SortCommand.SortOrder.ASC,
                SortCommand.SortMode.NUMBER
        );
        SortCommand.SortSpec secondSpec = new SortCommand.SortSpec(
                SortCommand.SortTargetType.PHONE,
                null,
                SortCommand.SortOrder.DESC,
                SortCommand.SortMode.NUMBER
        );

        SortCommand firstCommand = new SortCommand(firstSpec);
        SortCommand secondCommand = new SortCommand(secondSpec);

        assertTrue(firstCommand.equals(firstCommand));
        assertTrue(firstCommand.equals(new SortCommand(firstSpec)));
        assertFalse(firstCommand.equals(1));
        assertFalse(firstCommand.equals(null));
        assertFalse(firstCommand.equals(secondCommand));
    }

    @Test
    public void execute_unfiltered_sortsViewOnly() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        List<Person> masterBeforeSort = List.copyOf(model.getAddressBook().getPersonList());

        SortCommand.SortSpec spec = new SortCommand.SortSpec(
                SortCommand.SortTargetType.NAME,
                null,
                SortCommand.SortOrder.DESC,
                SortCommand.SortMode.ALPHA
        );
        SortCommand command = new SortCommand(spec);
        command.execute(model);

        // View should be sorted
        assertEquals("George Best", model.getFilteredPersonList().get(0).getName().fullName);
        // Master list should remain unchanged
        assertEquals(masterBeforeSort, model.getAddressBook().getPersonList());
    }

    @Test
    public void execute_filtered_onlySortsCurrentView() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        List<Person> masterBeforeSort = List.copyOf(model.getAddressBook().getPersonList());

        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList("Meier")));

        SortCommand.SortSpec spec = new SortCommand.SortSpec(
                SortCommand.SortTargetType.NAME,
                null,
                SortCommand.SortOrder.DESC,
                SortCommand.SortMode.ALPHA
        );
        SortCommand command = new SortCommand(spec);
        command.execute(model);

        assertEquals(Arrays.asList(DANIEL, BENSON), model.getFilteredPersonList());
        assertEquals(masterBeforeSort, model.getAddressBook().getPersonList());
    }

    @Test
    public void execute_unfiltered_selectsCorrectPerson() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

        SortCommand.SortSpec spec = new SortCommand.SortSpec(
                SortCommand.SortTargetType.NAME,
                null,
                SortCommand.SortOrder.DESC,
                SortCommand.SortMode.ALPHA
        );
        SortCommand command = new SortCommand(spec);
        command.execute(model);

        assertEquals(GEORGE, model.getSelectedPerson().getValue());
    }

    @Test
    public void execute_emptyFilteredList_selectedPersonIsNull() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList("None")));

        SortCommand.SortSpec spec = new SortCommand.SortSpec(
                SortCommand.SortTargetType.NAME,
                null,
                SortCommand.SortOrder.ASC,
                SortCommand.SortMode.ALPHA
        );
        SortCommand command = new SortCommand(spec);
        command.execute(model);

        assertNull(model.getSelectedPerson().getValue());
    }

    @Test
    public void execute_nonEmptyFilteredList_selectsCorrectPerson() throws CommandException {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList("Meier")));

        SortCommand.SortSpec spec = new SortCommand.SortSpec(
                SortCommand.SortTargetType.NAME,
                null,
                SortCommand.SortOrder.ASC,
                SortCommand.SortMode.ALPHA
        );
        SortCommand command = new SortCommand(spec);
        command.execute(model);

        assertEquals(BENSON, model.getSelectedPerson().getValue());
    }

    @Test
    public void constructor_nullSpec_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new SortCommand(null));
    }

    @Test
    public void execute_numericSort_leadingZerosTieBreaker() throws CommandException {
        AddressBook addressBook = new AddressBook();
        Person personA = new PersonBuilder().withName("A").withPhone("01234").build();
        Person personB = new PersonBuilder().withName("B").withPhone("1234").build();
        addressBook.addPerson(personA);
        addressBook.addPerson(personB);
        Model model = new ModelManager(addressBook, new UserPrefs());

        SortCommand.SortSpec spec = new SortCommand.SortSpec(
                SortCommand.SortTargetType.PHONE,
                null,
                SortCommand.SortOrder.ASC,
                SortCommand.SortMode.NUMBER
        );
        SortCommand command = new SortCommand(spec);
        command.execute(model);

        // "01234" comes before "1234" alphabetically when parsed numbers are equal
        assertEquals(Arrays.asList(personA, personB), model.getFilteredPersonList());

        // Test DESC order
        spec = new SortCommand.SortSpec(
                SortCommand.SortTargetType.PHONE,
                null,
                SortCommand.SortOrder.DESC,
                SortCommand.SortMode.NUMBER
        );
        command = new SortCommand(spec);
        command.execute(model);

        // "1234" comes before "01234" alphabetically when descending
        assertEquals(Arrays.asList(personB, personA), model.getFilteredPersonList());
    }

    @Test
    public void execute_sortUnknownTag_throwsCommandException() {
        Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        String unknownTag = "nonExistentTag";
        SortCommand.SortSpec spec = new SortCommand.SortSpec(
                SortCommand.SortTargetType.TAG,
                unknownTag,
                SortCommand.SortOrder.ASC,
                SortCommand.SortMode.ALPHA
        );
        SortCommand command = new SortCommand(spec);

        String expectedMessage = String.format(SortCommand.MESSAGE_UNKNOWN_TAG, unknownTag);
        assertThrows(CommandException.class, expectedMessage, () -> command.execute(model));
    }
}
