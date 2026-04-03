package seedu.address.logic;

import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_OUT_OF_BOUNDS_PERSON_INDEX =
                "The person index provided is out of bounds (too large).";

    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName());

        if (person.hasPhone()) {
            builder.append("; Phone: ").append(person.getPhone());
        }
        if (person.hasEmail()) {
            builder.append("; Email: ").append(person.getEmail());
        }

        builder.append("; \nTags: \n");
        builder.append(person.getTags().viewTagsTable());
        // person.getPrintableTags().forEach(tag -> builder.append(tag).append("\n"));
        return builder.toString();
    }

}
