package controller.util.validator;

/**
 * Created by JohnUkraine on 5/27/2018.
 */
public class PinValidator extends RegexValidator {
    private final static int MAX_LENGTH = 4;
    private final static String CARD_REGEX = "^\\d{4}$";
    private final static String INVALID_PIN_FORMAT = "invalid.pin.format";

    public PinValidator() {
    super(CARD_REGEX, MAX_LENGTH, INVALID_PIN_FORMAT);
    }
}
