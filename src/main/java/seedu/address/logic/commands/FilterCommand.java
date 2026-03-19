package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PARAM_ID_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PARAM_ID_NAME;
import static seedu.address.logic.parser.CliSyntax.PARAM_ID_PHONE;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.predicates.NameContainsKeywordsPredicate;
import seedu.address.model.person.predicates.PhoneEqualsPredicate;

/**
 * Filters the list of profiles by specified criteria.
 * Supports filtering by name, phone, and email parameters.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters profiles by the specified parameters.\n"
            + "Parameters: [" + PARAM_ID_NAME + " <name>]* [" + PARAM_ID_PHONE + " <phone>]* "
            + "[" + PARAM_ID_EMAIL + " <email>]*\n"
            + "Example: " + COMMAND_WORD + " " + PARAM_ID_NAME + " Tom " + PARAM_ID_PHONE + " 88345678 ";

    public static final String MESSAGE_SUCCESS = "Filtered victim profiles.";

    /**
     * Represents the supported categories of filter criteria for {@link FilterCommand}.
     */
    public enum FilterType {
        NAME, PHONE
    }
    private final Map<FilterType, List<String>> filterCriterion;

    /**
     * Creates a FilterCommand with the given filter criteria.
     * Supports filtering by name and phone parameters.
     * If no criteria are provided (empty map), all profiles are shown.
     *
     * @param filterCriterion a map from filter type (NAME, PHONE) to filter value
     */
    public FilterCommand(Map<FilterType, List<String>> filterCriterion) {
        requireNonNull(filterCriterion);
        this.filterCriterion = filterCriterion;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Predicate<Person> combinedPredicate = buildPredicate();
        model.updateFilteredPersonList(combinedPredicate);
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Builds a combined predicate from all filter criteria using AND logic.
     * Supports filtering by name, phone, and email parameters.
     *
     * @return a predicate that combines all filter criteria
     */
    private Predicate<Person> buildPredicate() {
        Predicate<Person> predicate = PREDICATE_SHOW_ALL_PERSONS;

        if (filterCriterion.containsKey(FilterType.NAME)) {
            List<String> nameFilters = filterCriterion.get(FilterType.NAME);
            if (nameFilters != null && !nameFilters.isEmpty()) {
                predicate = predicate.and(new NameContainsKeywordsPredicate(nameFilters));
            }
        }

        if (filterCriterion.containsKey(FilterType.PHONE)) {
            List<String> phoneFilters = filterCriterion.get(FilterType.PHONE);
            if (phoneFilters != null && !phoneFilters.isEmpty()) {
                predicate = predicate.and(new PhoneEqualsPredicate(phoneFilters));
            }
        }

        return predicate;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof FilterCommand)) {
            return false;
        }
        FilterCommand otherFilterCommand = (FilterCommand) other;
        return filterCriterion.equals(otherFilterCommand.filterCriterion);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("filterCriteria", filterCriterion)
                .toString();
    }
}
