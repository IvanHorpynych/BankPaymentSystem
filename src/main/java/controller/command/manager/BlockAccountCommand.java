package controller.command.manager;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.AccountNumberValidator;
import entity.Account;
import entity.Status;
import service.AccountsService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * Created by JohnUkraine on 5/13/2018.
 */

public class BlockAccountCommand implements ICommand {
    private final static String ACCOUNT_BLOCKED = "account.successfully.blocked";
    private final static String NO_SUCH_ACCOUNT = "account.not.exist";
    private final static String CLOSED_ACCOUNT_EXSIST = "account.already.closed";
    private final static String BLOCKED_ACCOUNT_EXSIST = "account.already.blocked";

    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private final AccountsService accountsService = ServiceFactory.getAccountsService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        validateAccount(request,errors);

        if(errors.isEmpty()) {
            blockAccount(getAccountFromRequest(request).get());

            addMessageToSession(request);

            Util.redirectTo(request, response,
                    bundle.getString("user.info"));

            return REDIRECTED;
        }

        request.setAttribute(Attributes.ERRORS,errors);

        return Views.INFO_VIEW;
    }

    private void validateAccount(HttpServletRequest request, List<String> errors){
        Util.validateField(new AccountNumberValidator(),
                request.getParameter(Attributes.ACCOUNT), errors);

        Optional<Account> accountOpt = getAccountFromRequest(request);
        if(!accountOpt.isPresent()) {
            errors.add(NO_SUCH_ACCOUNT);
        return;
        }

        Account account = accountOpt.get();

        if(account.isClosed()){
            errors.add(CLOSED_ACCOUNT_EXSIST);
            return;
        }

        if(account.isBlocked()){
            errors.add(BLOCKED_ACCOUNT_EXSIST);
            return;
        }
    }

    private Optional<Account> getAccountFromRequest(HttpServletRequest request) {
        long accountNumber = Long.valueOf(request.getParameter(Attributes.ACCOUNT));
        return accountsService.findAccountByNumber(accountNumber);
    }

    private void blockAccount(Account account) {

        accountsService.updateAccountStatus(account, Status.StatusIdentifier.BLOCKED_STATUS.getId());
    }

    private void addMessageToSession(HttpServletRequest request) {
        List<String> messages = new ArrayList<>();
        messages.add(ACCOUNT_BLOCKED);
        request.getSession().setAttribute(Attributes.MESSAGES, messages);
    }

}

