package controller.command.user;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.AmountValidator;
import entity.Account;
import entity.AccountType;
import entity.Payment;
import entity.User;
import service.AccountsService;
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
    private final static String NOT_ENOUGH_MONEY = "account.insufficient.funds";

    private final AccountsService accountsService = ServiceFactory.getAccountsService();
    private final PaymentService paymentService = ServiceFactory.getPaymentService();


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<String> errors = validateDataFromRequest(request);
        validateAccountBalance(request,errors);

        if (errors.isEmpty()) {
            Payment payment = createPayment(request);
            paymentService.createPayment(payment);

            List<String> messages = new ArrayList<>();
            messages.add(TRANSACTION_COMPLETE);

            addMessageDataToRequest(request, Attributes.MESSAGES, messages);

            //addAccountsListToRequest(request);

            return Views.REPLENISH_VIEW;
        }

        addMessageDataToRequest(request, Attributes.ERRORS, errors);

        //addAccountsListToRequest(request);

        return Views.REPLENISH_VIEW;
    }

    private List<String> validateDataFromRequest(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        Util.validateField(new AmountValidator(),
                request.getParameter(Attributes.AMOUNT), errors);

        return errors;
    }

    private void validateAccountBalance(HttpServletRequest request, List<String> errors) {
        Account senderAccount = accountsService.findAccountByNumber(
                Long.valueOf(getCleanAccountNumber(request,Attributes.SENDER_ACCOUNT))).get();
        BigDecimal paymentAmount = new BigDecimal(request.getParameter(Attributes.AMOUNT));

        BigDecimal accountBalance = senderAccount.getBalance();

        if (accountBalance.compareTo(paymentAmount) < 0) {
            errors.add(NOT_ENOUGH_MONEY);
        }
    }

    private Payment createPayment(HttpServletRequest request) {
        Account senderAccount = accountsService.findAccountByNumber(
                Long.valueOf(getCleanAccountNumber(request,Attributes.SENDER_ACCOUNT))).get();
        Account refillableAccount = accountsService.findAccountByNumber(
                Long.valueOf(request.getParameter(Attributes.REFILLABLE_ACCOUNT))).get();
        BigDecimal amount = new BigDecimal(request.getParameter(Attributes.AMOUNT));

        Payment payment = Payment.newBuilder()
                .addAccountFrom(senderAccount)
                .addAccountTo(refillableAccount)
                .addAmount(amount)
                .addDate(new Date())
                .build();

        return payment;
    }

    private String getCleanAccountNumber(HttpServletRequest request, String attribute){
       return request.getParameter(attribute)
                .substring(0,request.getParameter(attribute).indexOf('(')-1);
    }

    /*private void addAccountsListToRequest(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Attributes.USER);
        List<Account> accounts = accountsService.findAllByUser(user);
        request.setAttribute(Attributes.ACCOUNTS, accounts);
    }*/

    private void addMessageDataToRequest(HttpServletRequest request,
                                         String attribute,
                                         List<String> messages) {
        request.setAttribute(attribute, messages);
    }
}
