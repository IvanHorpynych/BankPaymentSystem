package controller.command.user;

import controller.command.ICommand;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.Account;
import entity.User;
import service.CreditAccountService;
import service.DebitAccountService;
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
public class GetDebitAccountsCommand implements ICommand {

    private final DebitAccountService accountService = ServiceFactory.getDebitAccountService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getUserFromSession(request.getSession());

        List<Account> debitAccounts = accountService.findAllByUser(user);

        request.setAttribute(Attributes.DEBIT_ACCOUNTS, debitAccounts);

        return Views.DEBIT_ACCOUNTS_VIEW;
    }

    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
    }
}
