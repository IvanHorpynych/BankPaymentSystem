package controller.util.validator;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public class NameValidator extends RegexValidator {
    private final static int MAX_LENGTH = 50;

    /**
     * Regex used to perform validation of name.
     */
    private static final String NAME_REGEX = "^([a-zA-Zа-яА-Я'][a-zA-Zа-яА-Я-' ]+[a-zA-Zа-яА-Я'])?$";

    public NameValidator(String errorMessage) {
        super(NAME_REGEX, MAX_LENGTH, errorMessage);
    }
}