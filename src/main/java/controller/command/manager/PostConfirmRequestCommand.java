package controller.command.manager;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.CreditAccount;
import entity.CreditRequest;
import entity.Status;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by JohnUkraine on 28/5/2018.
 */
public class PostConfirmRequestCommand extends RequestCommand implements ICommand {

    private final static String SUCCESSFUL_CONFIRMED = "request.successful.confirmed";

    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = validateRequest(request);

        if (errors.isEmpty()) {
            long creditRequestNumber = Long.valueOf(
                    request.getParameter(Attributes.CREDIT_REQUEST));

            CreditRequest creditRequest = creditRequestService.
                    findCreditRequestByNumber(creditRequestNumber).get();

            CreditAccount creditAccount = createCreditAccount(creditRequest);

            creditRequest.setStatus(
                    new Status(Status.StatusIdentifier.CONFIRM_STATUS.getId(),
                            Status.StatusIdentifier.CONFIRM_STATUS.toString()));

            creditRequestService.confirmRequest(creditRequest, creditAccount);

            List<String> messages = new ArrayList<>();
            messages.add(SUCCESSFUL_CONFIRMED);

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



    private CreditAccount createCreditAccount(CreditRequest creditRequest) {

        return CreditAccount.newCreditBuilder().
                addAccountHolder(creditRequest.getAccountHolder()).
                addDefaultAccountType().
                addDefaultStatus().
                addBalance(BigDecimal.ZERO).
                addInterestRate(creditRequest.getInterestRate()).
                addAccruedInterest(BigDecimal.ZERO).
                addValidityDate(creditRequest.getValidityDate()).
                addCreditLimit(creditRequest.getCreditLimit()).
                build();
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
