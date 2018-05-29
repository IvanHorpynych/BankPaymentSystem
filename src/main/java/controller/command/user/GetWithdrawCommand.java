package controller.command.user;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.AccountNumberValidator;
import entity.Account;
import entity.AccountType;
import entity.User;
import service.AccountsService;
import service.DebitAccountService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by JohnUkraine on 26/5/2018.
 */
public class GetWithdrawCommand implements ICommand {
    private final static String NO_SUCH_ACCOUNT = "account.not.exist";
    private final static String ACCOUNT_STATUS_NOT_ACTIVE = "account.not.active";

    private final DebitAccountService debitAccountService = ServiceFactory.getDebitAccountService();
    private final AccountsService accountsService = ServiceFactory.getAccountsService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<String> errors = new ArrayList<>();
        validateRequestData(request, errors);
        validateAccount(request, errors);

        if (errors.isEmpty()) {
            User user = getUserFromSession(request.getSession());
            Long senderAccountNumber = getAccountFromRequest(request);

            List<Account> refillableAccounts = debitAccountService.findAllByUser(user);

            Account senderAccount = accountsService.findAccountByNumber(
                    senderAccountNumber).get();
            List<Account> senderAccounts = new ArrayList<>();
            senderAccounts.add(senderAccount);

            request.setAttribute(Attributes.REFILLABLE_ACCOUNTS, refillableAccounts);
            request.setAttribute(Attributes.SENDER_ACCOUNTS, senderAccounts);

            if(senderAccount.getAccountType().getId()==
                    AccountType.TypeIdentifier.CREDIT_TYPE.getId())
                request.setAttribute(Attributes.COMMAND, Attributes.WITHDRAW_CREDIT);
            else if(senderAccount.getAccountType().getId()==
                    AccountType.TypeIdentifier.DEPOSIT_TYPE.getId())
                request.setAttribute(Attributes.COMMAND, Attributes.WITHDRAW_DEPOSIT);

            return Views.REPLENISH_VIEW;
        }
        request.setAttribute(Attributes.ERRORS, errors);

        return Views.INFO_VIEW;
    }

    private void validateRequestData(HttpServletRequest request, List<String> errors) {
        Util.validateField(new AccountNumberValidator(),
                request.getParameter(Attributes.SENDER_ACCOUNT), errors);
    }

    private void validateAccount(HttpServletRequest request, List<String> errors) {
        Optional<Account> account = accountsService.findAccountByNumber(
                Long.valueOf(request.getParameter(Attributes.SENDER_ACCOUNT)));

        if (!account.isPresent()) {
            errors.add(NO_SUCH_ACCOUNT);
            return;
        }

        if (!account.get().isNotClosed()) {
            errors.add(ACCOUNT_STATUS_NOT_ACTIVE);
        }
    }

    private Long getAccountFromRequest(HttpServletRequest request) {
        return Long.valueOf(request.getParameter(Attributes.SENDER_ACCOUNT));
    }

    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
    }
}
