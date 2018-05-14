/*
package controller.command;

import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.PagesPaths;
import controller.util.constants.Views;
import entity.Account;
import entity.User;
import service.AccountService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

*/
/**
 * Created by JohnUkraine on 5/13/2018.
 *//*

public class BlockAccountCommand implements ICommand {
    private final static String ACCOUNT_PARAM = "account";
    private final static String ACCOUNT_BLOCKED = "account.successfully.blocked";

    private final AccountService accountService = ServiceFactory.getAccountService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Optional<Account> account = getAccountFromRequest(request);

        if(account.isPresent()) {
            blockAccount(request, account.get());

            addAccountsListToRequest(request);

            return Views.ACCOUNTS_VIEW;
        }

        Util.redirectTo(request, response, PagesPaths.HOME_PATH);
        return REDIRECTED;
    }

    private Optional<Account> getAccountFromRequest(HttpServletRequest request) {
        long accountNumber = Long.valueOf(request.getParameter(ACCOUNT_PARAM));
        return accountService.findAccountByNumber(accountNumber);
    }

    private void blockAccount(HttpServletRequest request, Account account) {
        Account checkAccount = accountService.findAccountByNumber(
                account.getAccountNumber()
        ).get();

            if(checkAccount.isBlocked()) {
                List<String> errors = new ArrayList<>();
                errors.add("account.is.already.blocked");
                request.setAttribute(Attributes.ERRORS, errors);
                return;
            }

        accountService.updateAccountStatus(account, Account.Status.BLOCKED);
        addSuccessMessageToRequest(request);
    }

    private void addSuccessMessageToRequest(HttpServletRequest request) {
        List<String> messages = new ArrayList<>();
        messages.add(ACCOUNT_BLOCKED);
        request.setAttribute(Attributes.MESSAGES, messages);
    }

    private void addAccountsListToRequest(HttpServletRequest request) {
        User user = getUserFromSession(request.getSession());
        List<Account> accounts;

        if(!user.isAdmin()) {
            accounts = accountService.findAllByUser(user);
        } else {
            accounts = accountService.findAllAccounts();
        }

        request.setAttribute(Attributes.ACCOUNTS, accounts);
    }

    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
    }
}
*/
