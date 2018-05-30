package controller.command.user;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.AccountNumberValidator;
import controller.util.validator.AmountValidator;
import entity.*;
import service.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by JohnUkraine on 26/5/2018.
 */
public class PostWithdrawDepositCommand implements ICommand {
    private final static String NO_SUCH_ACCOUNT = "account.not.exist";
    private final static String TRANSACTION_COMPLETE = "replenish.complete";
    private final static String NOT_ENOUGH_MONEY = "account.insufficient.funds";
    private final static String ZERO_AMOUNT = "zero.amount";
    private final static String NEGATIVE_AMOUNT = "negative.amount";
    private final static String IMPOSSIBLE_TRANSACTION  = "impossible.transaction";

    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private final DepositAccountService depositAccountService = ServiceFactory.getDepositAccountService();
    private final PaymentService paymentService = ServiceFactory.getPaymentService();
    private final DebitAccountService debitAccountService = ServiceFactory.getDebitAccountService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<String> errors = validateDataFromRequest(request);
        validateAccountsBalance(request, errors);

        if (errors.isEmpty()) {
            Payment payment = createPayment(request);

            paymentService.createPaymentWithUpdate(payment);

            List<String> messages = new ArrayList<>();
            messages.add(TRANSACTION_COMPLETE);

            addMessageDataToSession(request, Attributes.MESSAGES, messages);

            Util.redirectTo(request, response,
                    bundle.getString("user.info"));

            return REDIRECTED;
        }

        addDataToRequest(request);

        addMessageDataToRequest(request, Attributes.ERRORS, errors);

        return Views.REPLENISH_VIEW;
    }

    private List<String> validateDataFromRequest(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        Util.validateField(new AmountValidator(),
                request.getParameter(Attributes.AMOUNT), errors);

        Util.validateField(new AccountNumberValidator(),
                request.getParameter(Attributes.REFILLABLE_ACCOUNT), errors);

        Util.validateField(new AccountNumberValidator(),
                getCleanAccountNumber(request,Attributes.SENDER_ACCOUNT), errors);

        return errors;
    }

    private void validateAccountsBalance(HttpServletRequest request, List<String> errors) {

        Optional<DepositAccount> senderAccountOptional = depositAccountService.findAccountByNumber(
                Long.valueOf(getCleanAccountNumber(request, Attributes.SENDER_ACCOUNT)));

        Optional<Account> refillableAccountOptional = debitAccountService.findAccountByNumber(
                Long.valueOf(request.getParameter(Attributes.REFILLABLE_ACCOUNT)));

        if(!senderAccountOptional.isPresent() ||
                !refillableAccountOptional.isPresent()){
            errors.add(NO_SUCH_ACCOUNT);
            return;
        }


        BigDecimal paymentAmount = new BigDecimal(request.getParameter(Attributes.AMOUNT));

        BigDecimal senderAccountBalance = senderAccountOptional.get().getBalance();

        if(paymentAmount.compareTo(BigDecimal.ZERO)==0)
            errors.add(ZERO_AMOUNT);

        if(paymentAmount.compareTo(BigDecimal.ZERO)<0)
            errors.add(NEGATIVE_AMOUNT);

        if (senderAccountBalance.compareTo(paymentAmount) < 0)
            errors.add(NOT_ENOUGH_MONEY);

        if(paymentAmount.add(refillableAccountOptional.get().getBalance()).compareTo(Account.MAX_BALANCE)>0){
            errors.add(IMPOSSIBLE_TRANSACTION);
        }
    }

    private Payment createPayment(HttpServletRequest request) {
        DepositAccount senderAccount = depositAccountService.findAccountByNumber(
                Long.valueOf(getCleanAccountNumber(request, Attributes.SENDER_ACCOUNT))).get();
        senderAccount.setMinBalance(BigDecimal.ZERO);
        senderAccount.setLastOperationDate(new Date());

        Account refillableAccount = debitAccountService.findAccountByNumber(
                Long.valueOf(request.getParameter(Attributes.REFILLABLE_ACCOUNT))).get();
        BigDecimal amount = new BigDecimal(request.getParameter(Attributes.AMOUNT));

        return Payment.newBuilder()
                .addAccountFrom(senderAccount)
                .addAccountTo(refillableAccount)
                .addAmount(amount)
                .addDate(new Date())
                .build();
    }


    private void addDataToRequest(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Attributes.USER);

        Long senderAccountNumber = Long.valueOf(
                getCleanAccountNumber(request, Attributes.SENDER_ACCOUNT));

        List<Account> refillableAccounts = debitAccountService.findAllByUser(user);

        Account senderAccount = depositAccountService.findAccountByNumber(
                senderAccountNumber).get();
        List<Account> senderAccounts = new ArrayList<>();
        senderAccounts.add(senderAccount);

        request.setAttribute(Attributes.REFILLABLE_ACCOUNTS, refillableAccounts);
        request.setAttribute(Attributes.SENDER_ACCOUNTS, senderAccounts);
        request.setAttribute(Attributes.COMMAND,request.getParameter(Attributes.COMMAND));
    }


    private void addMessageDataToRequest(HttpServletRequest request,
                                         String attribute,
                                         List<String> messages) {
        request.setAttribute(attribute, messages);
    }

    private void addMessageDataToSession(HttpServletRequest request,
                                         String attribute,
                                         List<String> messages) {
        request.getSession().setAttribute(attribute, messages);
    }

    private String getCleanAccountNumber(HttpServletRequest request, String attribute) {
        return request.getParameter(attribute)
                .substring(0, request.getParameter(attribute).indexOf('(')).
                        replaceAll("\\D+","");
    }
}
