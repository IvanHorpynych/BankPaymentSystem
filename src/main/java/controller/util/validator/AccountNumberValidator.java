package controller.util.validator;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public class AccountNumberValidator extends RegexValidator {
    private final static int MAX_LENGTH = 30;
    private final static String ACCOUNT_REGEX = "^(\\d+)$";
    private final static String INVALID_ACCOUNT_NUMBER = "invalid.account.number";

    public AccountNumberValidator() {
        super(ACCOUNT_REGEX, MAX_LENGTH, INVALID_ACCOUNT_NUMBER);
    }
}
