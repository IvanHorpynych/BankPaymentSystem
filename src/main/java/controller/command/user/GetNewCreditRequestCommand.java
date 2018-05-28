package controller.command.user;

import controller.command.ICommand;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.Account;
import entity.CreditAccount;
import entity.CreditRequest;
import entity.User;
import service.CreditAccountService;
import service.CreditRequestService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JohnUkraine on 28/5/2018.
 */
public class GetNewCreditRequestCommand implements ICommand {
    private final static String ACTIVE_CREDIT_OR_REQUEST = "active.credit.or.request";

    private final CreditRequestService creditRequestService = ServiceFactory.getCreditRequestService();
    private final CreditAccountService creditAccountService = ServiceFactory.getCreditAccountService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getUserFromSession(request.getSession());

        List<CreditRequest> creditRequests = creditRequestService.findAllByUser(user);
        List<CreditAccount> creditAccounts = creditAccountService.findAllByUser(user);


        if (creditRequests.stream().anyMatch(CreditRequest::isPending) ||
                creditAccounts.stream().anyMatch(Account::isNotClosed)) {

            List<String> messages = new ArrayList<>();
            messages.add(ACTIVE_CREDIT_OR_REQUEST);

            request.setAttribute(Attributes.MESSAGES, messages);
            request.setAttribute(Attributes.CREDIT_REQUESTS,creditRequests);
            return Views.CREDIT_REQUEST_VIEW;
        }

        request.setAttribute(Attributes.VALIDITY_DATE, new Date());

        return Views.NEW_CREDIT_REQUEST_VIEW;
    }

    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
    }
}
