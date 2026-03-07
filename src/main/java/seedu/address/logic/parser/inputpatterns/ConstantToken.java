package seedu.address.logic.parser.inputpatterns;

/**
 * A Token that takes in an input that matches exactly a string
 */
public class ConstantToken extends Token {
    private final String constant;

    /**
     * @param constant the string the input must match this string exactly
     */
    public ConstantToken(String id, String constant) {
        super(id);
        this.constant = constant;
    }

    @Override
    public String getPreview() {
        return constant;
    }

    @Override
    public boolean matches(String segment) {
        return segment.equals(constant);
    }

    @Override
    public boolean prefixMatches(String segment) {
        if (segment.length() > constant.length()) {
            return false;
        }

        if (segment.isEmpty()) {
            return true;
        }

        return (constant.substring(0, segment.length())
                .equals(segment));
    }
}
