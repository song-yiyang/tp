package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PARAM_ID_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PARAM_ID_NAME;
import static seedu.address.logic.parser.CliSyntax.PARAM_ID_PHONE;
import static seedu.address.logic.parser.CliSyntax.PARAM_ID_STATUS;
import static seedu.address.logic.parser.CliSyntax.PARAM_ID_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.Model;
import seedu.address.model.person.Person;
import seedu.address.model.person.Status;
import seedu.address.model.person.predicates.EmailContainsPredicate;
import seedu.address.model.person.predicates.NameContainsPredicate;
import seedu.address.model.person.predicates.PhoneContainsPredicate;
import seedu.address.model.person.predicates.StatusEqualsPredicate;
import seedu.address.model.person.predicates.TagContainsPredicate;
import seedu.address.model.tag.TagFilter;

/**
 * Filters the list of profiles by specified criteria.
 * Supports filtering by name substrings, partial phone numbers, partial email
 * addresses, status, and tag parameters.
 */
public class FilterCommand extends Command {

    public static final String COMMAND_WORD = "filter";
    public static final String EMPTY_FIELD_KEYWORD = "NONE";

    public static final String EXAMPLE = COMMAND_WORD + " "
            + PARAM_ID_NAME + " John "
            + PARAM_ID_PHONE + " 98765432 "
            + PARAM_ID_EMAIL + " johnd@example.com "
            + PARAM_ID_STATUS + " target "
            + PARAM_ID_TAG + " school:NUS "
            + PARAM_ID_TAG + " job ";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Filters profiles by the specified parameters.\n"
            + "Parameters: [" + PARAM_ID_NAME + " <name>]... "
            + "[" + PARAM_ID_PHONE + " <phone>]... "
            + "[" + PARAM_ID_EMAIL + " <email>]... "
            + "[" + PARAM_ID_STATUS + " <status>]... "
            + "[" + PARAM_ID_TAG + " <tagName>[:<tagValue>]]...\n"
            + "Use " + PARAM_ID_PHONE + " " + EMPTY_FIELD_KEYWORD + " and/or "
            + PARAM_ID_EMAIL + " " + EMPTY_FIELD_KEYWORD + " to filter missing phone/email fields.\n"
            + "Example: " + EXAMPLE;

    public static final String MESSAGE_SUCCESS = "Filtered victim profiles.";

    /**
     * Represents the supported categories of filter criteria for {@link FilterCommand}.
     */
    public enum FilterType {
        NAME, PHONE, EMAIL, STATUS
    }
    private final Map<FilterType, List<String>> paramFilters;
    private final List<TagFilter> tagFilters;

    /**
     * Creates a FilterCommand with the given filter criteria.
     * Supports filtering by name, phone, email, status, and tag parameters.
     * If no criteria are provided (empty map), all profiles are shown.
     *
     * @param paramFilters a map from filter type (NAME, PHONE, EMAIL, STATUS) to a list of filters
     * @param tagFilters a list of tag filters to filter by
     */
    public FilterCommand(Map<FilterType, List<String>> paramFilters, List<TagFilter> tagFilters) {
        requireNonNull(paramFilters);
        requireNonNull(tagFilters);

        this.paramFilters = paramFilters;
        this.tagFilters = tagFilters;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        Predicate<Person> combinedPredicate = buildPredicate();
        model.updateFilteredPersonList(combinedPredicate);
        // Scroll to and select first person in list
        if (model.getFilteredPersonList().isEmpty()) {
            model.setSelectedPerson(null);
        } else {
            model.setSelectedPerson(model.getFilteredPersonList().get(0));
        }
        return new CommandResult(MESSAGE_SUCCESS);
    }

    /**
     * Builds the predicate used to filter persons for this command.
     *
     * Each non-empty filter category contributes one predicate, and these
     * predicates are combined using AND logic.
     *
     * A person must satisfy every supplied category such as name, phone, email,
     * status, and tags to be included.
     *
     * Empty or null filter lists are ignored. Invalid status values are skipped,
     * and if no usable criteria are supplied, the returned predicate matches all
     * persons.
     *
     * @return a predicate representing the active filter criteria
     */
    private Predicate<Person> buildPredicate() {
        Predicate<Person> predicate = PREDICATE_SHOW_ALL_PERSONS;

        if (paramFilters.containsKey(FilterType.NAME)) {
            List<String> nameFilters = paramFilters.get(FilterType.NAME);
            if (nameFilters != null && !nameFilters.isEmpty()) {
                predicate = predicate.and(new NameContainsPredicate(nameFilters));
            }
        }

        if (paramFilters.containsKey(FilterType.PHONE)) {
            List<String> phoneFilters = paramFilters.get(FilterType.PHONE);
            if (phoneFilters != null && !phoneFilters.isEmpty()) {
                Predicate<Person> phonePredicate = buildPhonePredicate(phoneFilters);
                if (phonePredicate != null) {
                    predicate = predicate.and(phonePredicate);
                }
            }
        }

        if (paramFilters.containsKey(FilterType.EMAIL)) {
            List<String> emailFilters = paramFilters.get(FilterType.EMAIL);
            if (emailFilters != null && !emailFilters.isEmpty()) {
                Predicate<Person> emailPredicate = buildEmailPredicate(emailFilters);
                if (emailPredicate != null) {
                    predicate = predicate.and(emailPredicate);
                }
            }
        }

        if (paramFilters.containsKey(FilterType.STATUS)) {
            List<String> statusFilters = paramFilters.get(FilterType.STATUS);
            if (statusFilters != null && !statusFilters.isEmpty()) {
                List<Status> statuses = statusFilters.stream()
                    .map(status -> {
                        try {
                            return Status.parseStatus(status);
                        } catch (IllegalValueException e) {
                            Logger logger = Logger.getLogger(FilterCommand.class.getName());
                            logger.warning(() -> "Invalid status filter value: " + status
                                    + ". Skipping this filter.");
                            return null; // Skip invalid status values
                        }
                    })
                    .toList();
                predicate = predicate.and(new StatusEqualsPredicate(statuses));
            }
        }

        if (!tagFilters.isEmpty()) {
            predicate = predicate.and(new TagContainsPredicate(tagFilters));
        }

        return predicate;
    }

    private Predicate<Person> buildPhonePredicate(List<String> phoneFilters) {
        boolean includesEmptyPhone = phoneFilters.stream().anyMatch(this::isEmptyFieldKeyword);
        List<String> phoneSubstrings = phoneFilters.stream()
                .filter(filter -> !isEmptyFieldKeyword(filter))
                .collect(Collectors.toList());

        Predicate<Person> phonePredicate = null;
        if (!phoneSubstrings.isEmpty()) {
            phonePredicate = new PhoneContainsPredicate(phoneSubstrings);
        }
        if (includesEmptyPhone) {
            Predicate<Person> noPhonePredicate = person -> !person.hasPhone();
            phonePredicate = phonePredicate == null ? noPhonePredicate : phonePredicate.or(noPhonePredicate);
        }

        return phonePredicate;
    }

    private Predicate<Person> buildEmailPredicate(List<String> emailFilters) {
        boolean includesEmptyEmail = emailFilters.stream().anyMatch(this::isEmptyFieldKeyword);
        List<String> emailSubstrings = emailFilters.stream()
                .filter(filter -> !isEmptyFieldKeyword(filter))
                .collect(Collectors.toList());

        Predicate<Person> emailPredicate = null;
        if (!emailSubstrings.isEmpty()) {
            emailPredicate = new EmailContainsPredicate(emailSubstrings);
        }
        if (includesEmptyEmail) {
            Predicate<Person> noEmailPredicate = person -> !person.hasEmail();
            emailPredicate = emailPredicate == null ? noEmailPredicate : emailPredicate.or(noEmailPredicate);
        }

        return emailPredicate;
    }

    private boolean isEmptyFieldKeyword(String value) {
        return EMPTY_FIELD_KEYWORD.equals(value.trim());
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
        return paramFilters.equals(otherFilterCommand.paramFilters)
            && tagFilters.equals(otherFilterCommand.tagFilters);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("filterCriteria", paramFilters)
                .add("tagFilters", tagFilters)
                .toString();
    }
}
