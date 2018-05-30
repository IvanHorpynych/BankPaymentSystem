package controller.util.validator;

/**
 * Created by JohnUkraine on 5/28/2018.
 */
public class RateValidator extends RegexValidator {
    private final static int MAX_LENGTH = 5;
    private final static String RATE_REGEX = "^\\d{1,3}.?\\d{1,1}$";
    private final static String INVALID_INTEREST_RATE = "invalid.rate";

    public RateValidator() {
    super(RATE_REGEX, MAX_LENGTH, INVALID_INTEREST_RATE);
    }
}
