package controller.command.user;

import controller.command.ICommand;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.CreditAccount;
import entity.DepositAccount;
import entity.User;
import service.CreditAccountService;
import service.DepositAccountService;
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
public class GetDepositAccountsCommand implements ICommand {

    private final DepositAccountService accountService = ServiceFactory.getDepositAccountService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getUserFromSession(request.getSession());

        List<DepositAccount> depositAccounts = accountService.findAllByUser(user);

        request.setAttribute(Attributes.DEPOSIT_ACCOUNTS, depositAccounts);

        return Views.DEPOSIT_ACCOUNTS_VIEW;
    }

    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
    }
}
