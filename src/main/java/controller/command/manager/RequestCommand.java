package controller.command.manager;

import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.validator.RequestNumberValidator;
import entity.CreditRequest;
import service.CreditRequestService;
import service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class RequestCommand {
    private final static String INVALID_CREDIT_REQUEST = "invalid.credit.request";
    private final static String INCORRECT_REQUEST_STATUS = "incorrect.request.status";

    protected final CreditRequestService creditRequestService =
            ServiceFactory.getCreditRequestService();

    protected List<String> validateRequest(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        Util.validateField(new RequestNumberValidator(),
                request.getParameter(Attributes.CREDIT_REQUEST), errors);

        if (!errors.isEmpty())
            return errors;

        long creditRequestNumber = Long.valueOf(
                request.getParameter(Attributes.CREDIT_REQUEST));

        Optional<CreditRequest> creditRequestOpt =
                creditRequestService.findCreditRequestByNumber(creditRequestNumber);

        if (!creditRequestOpt.isPresent()) {
            errors.add(INVALID_CREDIT_REQUEST);
            return errors;
        }

        if (!creditRequestOpt.get().isPending())
            errors.add(INCORRECT_REQUEST_STATUS);

        return errors;
    }
}
