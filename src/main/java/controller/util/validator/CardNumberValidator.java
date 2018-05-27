package controller.util.validator;

/**
 * Created by JohnUkraine on 5/27/2018.
 */
public class CardNumberValidator extends RegexValidator {
    private final static int MAX_LENGTH = 16;
    private final static String CARD_REGEX = "^\\d{16}$";
    private final static String INVALID_CARD_FORMAT = "invalid.card.format";

    public CardNumberValidator() {
    super(CARD_REGEX, MAX_LENGTH, INVALID_CARD_FORMAT);
    }
}
