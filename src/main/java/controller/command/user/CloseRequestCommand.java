package controller.command.user;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.AccountNumberValidator;
import entity.CreditRequest;
import entity.Status;
import service.CreditRequestService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Created by JohnUkraine on 5/13/2018.
 */

public class CloseRequestCommand implements ICommand {
    private final static String REQUEST_CLOSED = "request.successfully.closed";
    private final static String NO_SUCH_REQUEST = "request.not.exist";
    private final static String CLOSED_REQUEST = "request.cant.closed";

    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private final CreditRequestService creditRequestService = ServiceFactory.getCreditRequestService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        validateRequest(request,errors);

        if(errors.isEmpty()) {
            CreditRequest creditRequest = getCreditRequestFromRequest(request).get();

            creditRequestService.updateRequestStatus(creditRequest,
                    Status.StatusIdentifier.CLOSED_STATUS.getId());

            addMessageToSession(request);

            Util.redirectTo(request, response,
                    bundle.getString("user.info"));

            return REDIRECTED;
        }

        request.setAttribute(Attributes.ERRORS,errors);

        return Views.INFO_VIEW;
    }

    private void validateRequest(HttpServletRequest request, List<String> errors){
        Util.validateField(new AccountNumberValidator(),
                request.getParameter(Attributes.CREDIT_REQUEST), errors);

        Optional<CreditRequest> requestOpt = getCreditRequestFromRequest(request);
        if(!requestOpt.isPresent()) {
            errors.add(NO_SUCH_REQUEST);
        return;
        }

        CreditRequest creditRequest = requestOpt.get();

        if(!creditRequest.isPending()){
            errors.add(REQUEST_CLOSED);
            return;
        }
    }

    private Optional<CreditRequest> getCreditRequestFromRequest(HttpServletRequest request) {
        long requestNumber = Long.valueOf(request.getParameter(Attributes.CREDIT_REQUEST));
        return creditRequestService.findCreditRequestByNumber(requestNumber);
    }

    private void closeAccount(CreditRequest request) {

        ;
    }

    private void addMessageToSession(HttpServletRequest request) {
        List<String> messages = new ArrayList<>();
        messages.add(REQUEST_CLOSED);
        request.getSession().setAttribute(Attributes.MESSAGES, messages);
    }

}

