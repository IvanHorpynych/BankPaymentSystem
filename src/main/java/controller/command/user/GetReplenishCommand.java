package controller.command.user;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.AccountNumberValidator;
import entity.Account;
import entity.AccountType;
import entity.Card;
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
import java.util.ResourceBundle;

/**
 * Created by JohnUkraine on 26/5/2018.
 */
public class GetReplenishCommand implements ICommand {
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
            Long refillableAccountNumber = getAccountFromRequest(request);

            Account refillableAccount = accountsService.findAccountByNumber(
                    refillableAccountNumber).get();
            List<Account> refillableAccounts = new ArrayList<>();
            refillableAccounts.add(refillableAccount);

            List<Account> senderAccounts = debitAccountService.findAllByUser(user);


            request.setAttribute(Attributes.SENDER_ACCOUNTS, senderAccounts);
            request.setAttribute(Attributes.REFILLABLE_ACCOUNTS, refillableAccounts);


            if(refillableAccount.getAccountType().getId()==
                    AccountType.TypeIdentifier.CREDIT_TYPE.getId())
                request.setAttribute(Attributes.COMMAND, Attributes.REPLENISH_CREDIT);
            else if(refillableAccount.getAccountType().getId()==
                    AccountType.TypeIdentifier.DEPOSIT_TYPE.getId())
                request.setAttribute(Attributes.COMMAND, Attributes.REPLENISH_DEPOSIT);


            return Views.REPLENISH_VIEW;
        }
        request.setAttribute(Attributes.ERRORS, errors);

        return Views.INFO_VIEW;
    }

    private void validateRequestData(HttpServletRequest request, List<String> errors) {
        Util.validateField(new AccountNumberValidator(),
                request.getParameter(Attributes.REFILLABLE_ACCOUNT), errors);
    }

    private void validateAccount(HttpServletRequest request, List<String> errors) {
        Optional<Account> account = accountsService.findAccountByNumber(
                Long.valueOf(request.getParameter(Attributes.REFILLABLE_ACCOUNT)));

        if (!account.isPresent()) {
            errors.add(NO_SUCH_ACCOUNT);
            return;
        }

        if (!account.get().isNotClosed()) {
            errors.add(ACCOUNT_STATUS_NOT_ACTIVE);
        }
    }

    private Long getAccountFromRequest(HttpServletRequest request) {
        return Long.valueOf(request.getParameter(Attributes.REFILLABLE_ACCOUNT));
    }

    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
    }
}
