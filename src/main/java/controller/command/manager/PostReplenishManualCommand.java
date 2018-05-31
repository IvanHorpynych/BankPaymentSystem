package controller.command.manager;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.AccountNumberValidator;
import controller.util.validator.AmountValidator;
import entity.Account;
import entity.Payment;
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
public class PostReplenishManualCommand implements ICommand {
    private final static String NO_SUCH_ACCOUNT = "account.not.exist";
    private final static String TRANSACTION_COMPLETE = "replenish.complete";
    private final static String ZERO_AMOUNT = "zero.amount";
    private final static String NEGATIVE_AMOUNT = "negative.amount";
    private final static String RECIPIENT_ACCOUNT_ALREADY_CLOSED = "recipient.account.already.closed";
    private final static String ATM_ACCOUNT_NOT_ACTIVE = "atm.account.not.active";
    private final static String IMPOSSIBLE_TRANSACTION  = "impossible.transaction";

    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private final PaymentService paymentService = ServiceFactory.getPaymentService();
    private final AccountsService accountsService = ServiceFactory.getAccountsService();


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<String> errors = validateAllData(request);

        if (!errors.isEmpty()) {

            addMessageDataToRequest(request, Attributes.ERRORS, errors);

            addDataToRequest(request);

            return Views.REPLENISH_VIEW;
        }

        Payment payment = createPayment(request);

        paymentService.createPayment(payment);

        List<String> messages = new ArrayList<>();
        messages.add(TRANSACTION_COMPLETE);

        addMessageDataToSession(request, Attributes.MESSAGES, messages);

        Util.redirectTo(request, response,
                bundle.getString("user.info"));

        return REDIRECTED;

    }

    private List<String> validateAllData(HttpServletRequest request){
        List<String> errors = validateDataFromRequest(request);

        if(!errors.isEmpty()){
            return errors;
        }

        validateAccounts(request, errors);
        if(!errors.isEmpty()){
            return errors;
        }

        validateAmount(request, errors);

        return errors;
    }

    private List<String> validateDataFromRequest(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        Util.validateField(new AccountNumberValidator(),
                request.getParameter(Attributes.REFILLABLE_ACCOUNT), errors);

        Util.validateField(new AccountNumberValidator(),
                getCleanAccountNumber(request,Attributes.SENDER_ACCOUNT), errors);

        Util.validateField(new AmountValidator(),
                request.getParameter(Attributes.AMOUNT), errors);

        return errors;
    }

    private void validateAccounts(HttpServletRequest request, List<String> errors) {

        Optional<Account> senderAccountOptional = accountsService.findAccountByNumber(
                Long.valueOf(getCleanAccountNumber(request, Attributes.SENDER_ACCOUNT)));

        Optional<Account> refillableAccountOptional = accountsService.findAccountByNumber(
                Long.valueOf(request.getParameter(Attributes.REFILLABLE_ACCOUNT)));

        if (!senderAccountOptional.isPresent() ||
                !refillableAccountOptional.isPresent()) {
            errors.add(NO_SUCH_ACCOUNT);
            return;
        }

        if (!senderAccountOptional.get().isActive())
            errors.add(ATM_ACCOUNT_NOT_ACTIVE);
        if (refillableAccountOptional.get().isClosed())
            errors.add(RECIPIENT_ACCOUNT_ALREADY_CLOSED);

    }

    private void validateAmount(HttpServletRequest request, List<String> errors){

        Optional<Account> refillableAccountOptional = accountsService.findAccountByNumber(
                Long.valueOf(request.getParameter(Attributes.REFILLABLE_ACCOUNT)));

        BigDecimal paymentAmount = new BigDecimal(request.getParameter(Attributes.AMOUNT));


        if (paymentAmount.compareTo(BigDecimal.ZERO) == 0)
            errors.add(ZERO_AMOUNT);

        if (paymentAmount.compareTo(BigDecimal.ZERO) < 0)
            errors.add(NEGATIVE_AMOUNT);

        if(paymentAmount.add(refillableAccountOptional.get().getBalance()).compareTo(Account.MAX_BALANCE)>0){
            errors.add(IMPOSSIBLE_TRANSACTION);
        }
    }

    private Payment createPayment(HttpServletRequest request) {
        Account senderAccount = accountsService.findAccountByNumber(
                Long.valueOf(getCleanAccountNumber(request, Attributes.SENDER_ACCOUNT))).get();
        Account refillableAccount = accountsService.findAccountByNumber(
                Long.valueOf(request.getParameter(Attributes.REFILLABLE_ACCOUNT))).get();


        BigDecimal amount = new BigDecimal(request.getParameter(Attributes.AMOUNT));

        return Payment.newBuilder()
                .addAccountFrom(senderAccount)
                .addAccountTo(refillableAccount)
                .addAmount(amount)
                .addDate(new Date())
                .build();
    }

    private String getCleanAccountNumber(HttpServletRequest request, String attribute) {
        return request.getParameter(attribute)
                .substring(0, request.getParameter(attribute).indexOf('(')).
                        replaceAll("\\D+","");
    }


    private void addDataToRequest(HttpServletRequest request) {

        Long refillableAccountNumber = Long.valueOf(
                request.getParameter(Attributes.REFILLABLE_ACCOUNT));
        List<Account> refillableAccounts = new ArrayList<>();
        refillableAccounts.add(accountsService.findAccountByNumber(
                refillableAccountNumber).get());

        Long senderAccountNumber = Long.valueOf(getCleanAccountNumber(
                request,Attributes.SENDER_ACCOUNT));
        List<Account> senderAccounts = new ArrayList<>();
        senderAccounts.add(accountsService.findAccountByNumber(
                senderAccountNumber).get());


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
}
