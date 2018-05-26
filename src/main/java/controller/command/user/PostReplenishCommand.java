package controller.command.user;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.AmountValidator;
import entity.Account;
import entity.Payment;
import entity.User;
import service.AccountService;
import service.PaymentService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JohnUkraine on 26/5/2018.
 */
public class PostReplenishCommand implements ICommand {
    private final static String TRANSACTION_COMPLETE = "replenish.complete";

    private final AccountService accountService = ServiceFactory.getAccountService();
    private final PaymentService paymentService = ServiceFactory.getPaymentService();


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<String> errors = validateDataFromRequest(request);

        if (errors.isEmpty()) {
            Payment payment = createPayment(request);
            paymentService.createPayment(payment);

            List<String> messages = new ArrayList<>();
            messages.add(TRANSACTION_COMPLETE);

            addMessageDataToRequest(request, Attributes.MESSAGES, messages);

            addAccountsListToRequest(request);

            return Views.REPLENISH_VIEW;
        }

        addMessageDataToRequest(request, Attributes.ERRORS, errors);

        addAccountsListToRequest(request);

        return Views.REPLENISH_VIEW;
    }

    private List<String> validateDataFromRequest(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        Util.validateField(new AmountValidator(),
                request.getParameter(AMOUNT_PARAM), errors);

        return errors;
    }

    private Payment createPayment(HttpServletRequest request) {
        Account atmAccount = accountService.findAccountByNumber(ATM_ACCOUNT_PARAM).get();
        Account accountRecipient = accountService.findAccountByNumber(
                Long.valueOf(request.getParameter(ACCOUNT_PARAM))).get();
        BigDecimal amount = new BigDecimal(request.getParameter(AMOUNT_PARAM));

        Payment payment = Payment.newBuilder()
                .setAccountFrom(atmAccount)
                .setAccountTo(accountRecipient)
                .setAmount(amount)
                .setDate(new Date())
                .build();

        return payment;
    }

    private void addAccountsListToRequest(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Attributes.USER);
        List<Account> accounts = accountService.findAllByUser(user);
        request.setAttribute(Attributes.ACCOUNTS, accounts);
    }

    private void addMessageDataToRequest(HttpServletRequest request,
                                         String attribute,
                                         List<String> messages) {
        request.setAttribute(attribute, messages);
    }

}
