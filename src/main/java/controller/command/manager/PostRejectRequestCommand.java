package controller.command.manager;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
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
import java.util.ResourceBundle;

/**
 * Created by JohnUkraine on 28/5/2018.
 */
public class PostRejectRequestCommand extends RequestCommand implements ICommand {

    private final static String SUCCESSFUL_REJECT = "request.successful.reject";

    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private final CreditRequestService creditRequestService =
            ServiceFactory.getCreditRequestService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = validateRequest(request);

        if (errors.isEmpty()) {
            long creditRequestNumber = Long.valueOf(
                    request.getParameter(Attributes.CREDIT_REQUEST));

            int statusId = Status.StatusIdentifier.REJECT_STATUS.getId();

            CreditRequest creditRequest = CreditRequest.newBuilder().
                    addRequestNumber(creditRequestNumber).
                    build();

            creditRequestService.updateRequestStatus(creditRequest, statusId);

            List<String> messages = new ArrayList<>();
            messages.add(SUCCESSFUL_REJECT);

            addMessageDataToSession(request, Attributes.MESSAGES, messages);

            Util.redirectTo(request, response, bundle.
                    getString("user.info"));

            return REDIRECTED;
        }

        addMessageDataToRequest(request, Attributes.ERRORS, errors);

        List<CreditRequest> creditRequests = creditRequestService.
                findAllPendingRequests();

        request.setAttribute(Attributes.CREDIT_REQUESTS, creditRequests);

        return Views.CREDIT_REQUEST_LIST_VIEW;
    }


    private void addMessageDataToRequest(HttpServletRequest request,
                                         String attribute,
                                         List<String> messages) {
        request.setAttribute(attribute, messages);
    }

    private void addMessageDataToSession(HttpServletRequest request,
                                         String attribute,
                                         List<String> messages) {
        request.getSession().setAttribute(attribute, messages);
    }
}
