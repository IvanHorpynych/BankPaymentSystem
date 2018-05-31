package controller.command.manager;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.AccountNumberValidator;
import entity.Account;
import entity.AccountType;
import service.AccountsService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by JohnUkraine on 26/5/2018.
 */
public class GetReplenishManualCommand implements ICommand {
    private final static String NO_SUCH_ACCOUNT = "account.not.exist";
    private final static String NO_SUCH_ATM = "atm.not.exist";
    private final static String ACCOUNT_STATUS_NOT_ACTIVE = "account.not.active";
    private final static String ACCOUNT_NOT_DEBIT = "account.not.debit";

    private final AccountsService accountsService = ServiceFactory.getAccountsService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<String> errors = new ArrayList<>();
        validateRequestData(request, errors);
        validateAccount(request, errors);
        validateAtm(errors);

        if (errors.isEmpty()) {
            Long refillableAccountNumber = getAccountFromRequest(request);

            List<Account> refillableAccounts = new ArrayList<>();
            refillableAccounts.add(accountsService.findAccountByNumber(
                    refillableAccountNumber).get());

            List<Account> senderAccounts = new ArrayList<>();
            senderAccounts.add(accountsService.findAtmAccount().get());

            request.setAttribute(Attributes.SENDER_ACCOUNTS, senderAccounts);
            request.setAttribute(Attributes.REFILLABLE_ACCOUNTS, refillableAccounts);
            request.setAttribute(Attributes.COMMAND,Attributes.REPLENISH_MANUAL);


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
            return;
        }

        if (account.get().getAccountType().getId() !=
                AccountType.TypeIdentifier.DEBIT_TYPE.getId()) {
            errors.add(ACCOUNT_NOT_DEBIT);
        }
    }

    private void validateAtm(List<String> errors) {
        Optional<Account> atm = accountsService.findAtmAccount();

        if (!atm.isPresent())
            errors.add(NO_SUCH_ATM);
    }

    private Long getAccountFromRequest(HttpServletRequest request) {
        return Long.valueOf(request.getParameter(Attributes.REFILLABLE_ACCOUNT));
    }

}
