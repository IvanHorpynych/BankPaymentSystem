package controller.command.user;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.AccountNumberValidator;
import dao.util.card.CardPassGenerator;
import entity.Account;
import entity.Card;
import entity.User;
import service.CardService;
import service.DebitAccountService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by JohnUkraine on 28/5/2018.
 */
public class PostCreateDebitCommand implements ICommand {
    private final static String ACCOUNT_CREATE_SUCCESS = "account.create.success";

    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private final DebitAccountService debitAccountService = ServiceFactory.getDebitAccountService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

            Account debitAccount = createDefaultAccount(request);
            debitAccountService.createAccount(debitAccount);

            request.getSession().setAttribute(Attributes.MESSAGES, ACCOUNT_CREATE_SUCCESS);

            Util.redirectTo(request, response,
                    bundle.getString("user.info"));

            return REDIRECTED;

    }


    private Account createDefaultAccount(HttpServletRequest request) {
        User accountHolder =  getUserFromSession(request.getSession());

        return  Account.newBuilder().
                addAccountHolder(accountHolder).
                addDefaultAccountType().
                addDefaultStatus().
                addBalance(BigDecimal.ZERO).
                build();
    }

    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
    }
}
