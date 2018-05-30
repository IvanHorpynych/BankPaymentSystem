package controller.command.manager;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.UserIdentifierValidator;
import entity.Account;
import entity.User;
import service.DebitAccountService;
import service.ServiceFactory;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by JohnUkraine on 25/5/2018.
 */
public class GetDebitAccByUserCommand extends ManagerHelper implements ICommand {
    private final DebitAccountService accountService = ServiceFactory.getDebitAccountService();


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = new ArrayList<>();

        validateUser(request,errors);

        if (errors.isEmpty()) {
            User user = userService.findById(
                    Long.valueOf(request.getParameter(Attributes.USER))).get();

            List<Account> debitAccounts = accountService.findAllByUser(user);

            request.setAttribute(Attributes.DEBIT_ACCOUNTS, debitAccounts);

            return Views.DEBIT_ACCOUNTS_VIEW;
        }

        request.setAttribute(Attributes.ERRORS,errors);

        return Views.INFO_VIEW;
    }
}
