package controller.command.user;

import controller.command.ICommand;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.Account;
import entity.DebitAccount;
import entity.Payment;
import entity.User;
import service.DebitAccountService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by JohnUkraine on 26/5/2018.
 */
public class GetReplenishCommand implements ICommand {
    private final DebitAccountService accountService = ServiceFactory.getDebitAccountService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = getUserFromSession(request.getSession());

        Long accountNumber = getAccountFromRequest(request);

        List<DebitAccount> accounts = accountService.findAllByUser(user);

        request.setAttribute(Attributes.ACCOUNTS, accounts);
        request.setAttribute(Attributes.REFILLABLE_ACCOUNT, accountNumber);

        return Views.REPLENISH_VIEW;
    }

    private Long getAccountFromRequest(HttpServletRequest request) {
        return Long.valueOf(request.getParameter(Attributes.REFILLABLE_ACCOUNT));
    }
    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
    }
}
