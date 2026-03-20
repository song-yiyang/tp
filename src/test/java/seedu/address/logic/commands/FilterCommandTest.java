package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FilterCommand.FilterType;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.predicates.NameContainsKeywordsPredicate;
import seedu.address.model.person.predicates.PhoneEqualsPredicate;

/**
 * Contains integration tests (interaction with the Model) and unit tests for FilterCommand.
 */
public class FilterCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_noCriteria_showsAll() {
        FilterCommand command = new FilterCommand(new HashMap<>());
        assertCommandSuccess(command, model, FilterCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE),
                model.getFilteredPersonList());
    }

    @Test
    public void execute_singleNameCriteria_filtersCorrectly() {
        Map<FilterType, List<String>> criteria = new HashMap<>();
        criteria.put(FilterType.NAME, List.of("Alice"));
        FilterCommand command = new FilterCommand(criteria);

        expectedModel.updateFilteredPersonList(new NameContainsKeywordsPredicate(List.of("Alice")));
        assertCommandSuccess(command, model, FilterCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(List.of(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_multipleNameCriteria_filtersAll() {
        Map<FilterType, List<String>> criteria = new HashMap<>();
        criteria.put(FilterType.NAME, Arrays.asList("Alice", "Benson"));
        FilterCommand command = new FilterCommand(criteria);

        expectedModel.updateFilteredPersonList(
                new NameContainsKeywordsPredicate(Arrays.asList("Alice", "Benson")));
        assertCommandSuccess(command, model, FilterCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_singlePhoneCriteria_filtersCorrectly() {
        Map<FilterType, List<String>> criteria = new HashMap<>();
        criteria.put(FilterType.PHONE, List.of("94351253"));
        FilterCommand command = new FilterCommand(criteria);

        expectedModel.updateFilteredPersonList(new PhoneEqualsPredicate(List.of("94351253")));
        assertCommandSuccess(command, model, FilterCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(List.of(ALICE), model.getFilteredPersonList());
    }

    @Test
    public void execute_multiplePhoneCriteria_filtersAnyPhone() {
        Map<FilterType, List<String>> criteria = new HashMap<>();
        criteria.put(FilterType.PHONE, Arrays.asList("94351253", "98765432"));
        FilterCommand command = new FilterCommand(criteria);

        expectedModel.updateFilteredPersonList(new PhoneEqualsPredicate(Arrays.asList("94351253", "98765432")));
        assertCommandSuccess(command, model, FilterCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(Arrays.asList(ALICE, BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_nameAndPhoneCriteria_filtersByBoth() {
        Map<FilterType, List<String>> criteria = new HashMap<>();
        criteria.put(FilterType.NAME, List.of("Meier"));
        criteria.put(FilterType.PHONE, List.of("98765432"));
        FilterCommand command = new FilterCommand(criteria);

        expectedModel.updateFilteredPersonList(
                new NameContainsKeywordsPredicate(List.of("Meier")).and(new PhoneEqualsPredicate(List.of("98765432"))));
        assertCommandSuccess(command, model, FilterCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(List.of(BENSON), model.getFilteredPersonList());
    }

    @Test
    public void execute_nameMatchesNoOne_emptyList() {
        Map<FilterType, List<String>> criteria = new HashMap<>();
        criteria.put(FilterType.NAME, List.of("Zzz"));
        FilterCommand command = new FilterCommand(criteria);

        expectedModel.updateFilteredPersonList(new NameContainsKeywordsPredicate(List.of("Zzz")));
        assertCommandSuccess(command, model, FilterCommand.MESSAGE_SUCCESS, expectedModel);
        assertEquals(Collections.emptyList(), model.getFilteredPersonList());
    }

    @Test
    public void equals() {
        Map<FilterType, List<String>> criteriaAlice = new HashMap<>();
        criteriaAlice.put(FilterType.NAME, List.of("Alice"));

        Map<FilterType, List<String>> criteriaAliceCopy = new HashMap<>();
        criteriaAliceCopy.put(FilterType.NAME, List.of("Alice"));

        Map<FilterType, List<String>> criteriaBob = new HashMap<>();
        criteriaBob.put(FilterType.NAME, List.of("Bob"));

        Map<FilterType, List<String>> criteriaAlicePhone = new HashMap<>();
        criteriaAlicePhone.put(FilterType.NAME, List.of("Alice"));
        criteriaAlicePhone.put(FilterType.PHONE, List.of("94351253"));

        FilterCommand filterAlice = new FilterCommand(criteriaAlice);
        FilterCommand filterAliceCopy = new FilterCommand(criteriaAliceCopy);
        FilterCommand filterBob = new FilterCommand(criteriaBob);
        FilterCommand filterAlicePhone = new FilterCommand(criteriaAlicePhone);
        FilterCommand filterEmpty = new FilterCommand(new HashMap<>());

        // same object -> returns true
        assertTrue(filterAlice.equals(filterAlice));

        // same values -> returns true
        assertTrue(filterAlice.equals(filterAliceCopy));

        // different values -> returns false
        assertFalse(filterAlice.equals(filterBob));

        // different filter types -> returns false
        assertFalse(filterAlice.equals(filterAlicePhone));

        // empty vs non-empty -> returns false
        assertFalse(filterAlice.equals(filterEmpty));

        // null -> returns false
        assertFalse(filterAlice.equals(null));

        // different type -> returns false
        assertFalse(filterAlice.equals(5));
    }

    @Test
    public void toString_isCorrect() {
        Map<FilterType, List<String>> criteria = new HashMap<>();
        criteria.put(FilterType.NAME, List.of("Alice"));
        FilterCommand command = new FilterCommand(criteria);
        String expected = FilterCommand.class.getCanonicalName() + "{filterCriteria="
                + criteria + "}";
        assertEquals(expected, command.toString());
    }
}
