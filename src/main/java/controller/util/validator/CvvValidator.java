package controller.util.validator;

/**
 * Created by JohnUkraine on 5/27/2018.
 */
public class CvvValidator extends RegexValidator {
    private final static int MAX_LENGTH = 3;
    private final static String CARD_REGEX = "^\\d{3}$";
    private final static String INVALID_CVV_FORMAT = "invalid.cvv.format";

    public CvvValidator() {
    super(CARD_REGEX, MAX_LENGTH, INVALID_CVV_FORMAT);
    }
}
