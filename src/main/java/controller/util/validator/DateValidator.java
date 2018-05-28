package controller.util.validator;

/**
 * Created by JohnUkraine on 5/28/2018.
 */
public class DateValidator extends RegexValidator {
    private final static int MAX_LENGTH = 10;
    private final static String DATE_REGEX = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$";
    private final static String INVALID_DATE = "invalid.date";

    public DateValidator() {
    super(DATE_REGEX, MAX_LENGTH, INVALID_DATE);
    }
}
