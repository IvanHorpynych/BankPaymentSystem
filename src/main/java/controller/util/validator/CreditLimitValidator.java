package controller.util.validator;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public class CreditLimitValidator extends RegexValidator {
    private final static int MAX_LENGTH = 50;
    private final static String LIMIT_REGEX = "^(\\d{1,8}.?\\d{1,4})$";
    private final static String INVALID_CREDIT_LIMIT = "invalid.credit.limit";

    public CreditLimitValidator() {
        super(LIMIT_REGEX, MAX_LENGTH, INVALID_CREDIT_LIMIT);
    }

}
