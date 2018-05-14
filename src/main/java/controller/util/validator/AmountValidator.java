package controller.util.validator;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public class AmountValidator extends RegexValidator {
    private final static int MAX_LENGTH = 50;
    private final static String AMOUNT_REGEX = "^(\\d*\\.\\d{2})$";
    private final static String INVALID_AMOUNT = "invalid.amount.format";

    public AmountValidator() {
        super(AMOUNT_REGEX, MAX_LENGTH, INVALID_AMOUNT);
    }
}
