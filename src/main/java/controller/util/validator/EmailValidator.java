package controller.util.validator;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public class EmailValidator extends RegexValidator {
    private final static String INVALID_EMAIL_KEY = "invalid.email";

    private final static int MAX_LENGTH = 50;

    /**
     * Regex used to perform validation of data.
     */
    private static final String EMAIL_REGEX = "^(.+\\@.+\\..+)$";

    public EmailValidator() {
        super(EMAIL_REGEX, MAX_LENGTH, INVALID_EMAIL_KEY);
    }
}
