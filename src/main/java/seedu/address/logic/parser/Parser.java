package seedu.address.logic.parser;

import seedu.address.logic.commands.Command;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.inputpatterns.InputPattern;

/**
 * Represents a Parser that is able to parse user input into a {@code Command} of type {@code T}.
 */
public abstract class Parser<T extends Command> {

    /**
     * Defines the InputPattern of the parser for the command of type T
     * @return An InputPattern
     */
    abstract InputPattern createInputPattern();


    /**
     * Parses {@code userInput} into a command and returns it.
     * @throws ParseException if {@code userInput} does not conform the expected format
     */
    abstract T parse(String userInput) throws ParseException;




}
