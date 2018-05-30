package controller.util.validator;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public class RequestNumberValidator extends RegexValidator {
    private final static int MAX_LENGTH = 30;
    private final static String REQUEST_REGEX = "^(\\d+)$";
    private final static String INVALID_REQUEST_NUMBER = "invalid.request.number";

    public RequestNumberValidator() {
        super(REQUEST_REGEX, MAX_LENGTH, INVALID_REQUEST_NUMBER);
    }
}
