package controller.command.user;

import controller.command.ICommand;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.CreditAccount;
import entity.User;
import service.CreditAccountService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by JohnUkraine on 25/5/2018.
 */
public class GetCreditAccountsCommand implements ICommand {

    private final CreditAccountService accountService = ServiceFactory.getCreditAccountService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getUserFromSession(request.getSession());

        List<CreditAccount> creditAccounts = accountService.findAllByUser(user);

        request.setAttribute(Attributes.CREDIT_ACCOUNTS, creditAccounts);

        return Views.CREDIT_ACCOUNTS_VIEW;
    }

    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
    }
}
