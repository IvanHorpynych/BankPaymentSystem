package controller.command.user;

import controller.command.ICommand;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.CreditRequest;
import entity.User;
import service.CreditRequestService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by JohnUkraine on 27/5/2018.
 */
public class GetCreditRequestsCommand implements ICommand {
    private final CreditRequestService creditRequestService = ServiceFactory.getCreditRequestService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getUserFromSession(request.getSession());

        List<CreditRequest> creditRequests = creditRequestService.findAllByUser(user);

        request.setAttribute(Attributes.CREDIT_REQUESTS, creditRequests);

        return Views.CREDIT_REQUEST_VIEW;
    }

    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
    }
}
