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
public class PostReplenishDepositCommand implements ICommand {
    private final static String NO_SUCH_ACCOUNT = "account.not.exist";
    private final static String TRANSACTION_COMPLETE = "replenish.complete";
    private final static String NOT_ENOUGH_MONEY = "account.insufficient.funds";
    private final static String ZERO_AMOUNT = "zero.amount";
    private final static String NEGATIVE_AMOUNT = "negative.amount";

    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private final PaymentService paymentService = ServiceFactory.getPaymentService();
    private final DebitAccountService debitAccountService = ServiceFactory.getDebitAccountService();
    private final DepositAccountService depositAccountService = ServiceFactory.getDepositAccountService();


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<String> errors = validateDataFromRequest(request);
        validateAccountsBalance(request, errors);

        if (errors.isEmpty()) {
            Payment payment = createPayment(request);

            paymentService.createPayment(payment);

            checkChangingAccountData(payment);

            List<String> messages = new ArrayList<>();
            messages.add(TRANSACTION_COMPLETE);

            addMessageDataToSession(request, Attributes.MESSAGES, messages);

            Util.redirectTo(request, response,
                    bundle.getString("user.info"));


            return REDIRECTED;
        }

        addMessageDataToRequest(request, Attributes.ERRORS, errors);

        addDataToRequest(request);

        return Views.REPLENISH_VIEW;
    }

    private List<String> validateDataFromRequest(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        Util.validateField(new AccountNumberValidator(),
                getCleanAccountNumber(request,Attributes.SENDER_ACCOUNT), errors);

        Util.validateField(new AccountNumberValidator(),
                request.getParameter(Attributes.REFILLABLE_ACCOUNT), errors);

        Util.validateField(new AmountValidator(),
                request.getParameter(Attributes.AMOUNT), errors);

        return errors;
    }

    private void validateAccountsBalance(HttpServletRequest request, List<String> errors) {

        Optional<Account> senderAccountOptional = debitAccountService.findAccountByNumber(
                Long.valueOf(getCleanAccountNumber(request, Attributes.SENDER_ACCOUNT)));

        Optional<DepositAccount> refillableAccountOptional = depositAccountService.findAccountByNumber(
                Long.valueOf(request.getParameter(Attributes.REFILLABLE_ACCOUNT)));

        if (!senderAccountOptional.isPresent() ||
                !refillableAccountOptional.isPresent()) {
            errors.add(NO_SUCH_ACCOUNT);
            return;
        }

        BigDecimal paymentAmount = new BigDecimal(request.getParameter(Attributes.AMOUNT));

        BigDecimal senderAccountBalance = senderAccountOptional.get().getBalance();

        if (paymentAmount.compareTo(BigDecimal.ZERO) == 0)
            errors.add(ZERO_AMOUNT);

        if (paymentAmount.compareTo(BigDecimal.ZERO) < 0)
            errors.add(NEGATIVE_AMOUNT);


        if (senderAccountBalance.compareTo(paymentAmount) < 0)
            errors.add(NOT_ENOUGH_MONEY);
    }

    private Payment createPayment(HttpServletRequest request) {
        Account senderAccount = debitAccountService.findAccountByNumber(
                Long.valueOf(getCleanAccountNumber(request, Attributes.SENDER_ACCOUNT))).get();
        DepositAccount refillableAccount = depositAccountService.findAccountByNumber(
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
                .substring(0, request.getParameter(attribute).indexOf('(') - 1);
    }

    private void checkChangingAccountData(Payment payment) {
         /*if (payment.getAccountTo().getAccountType().getId() ==
                AccountType.TypeIdentifier.DEPOSIT_TYPE.getId()) {
            ((DepositAccount) payment.getAccountTo()).setMinBalance(
                    ((DepositAccount) payment.getAccountTo()).getMinBalance().
                            add(payment.getAmount())
            );
        }*/
    }


    private void addDataToRequest(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Attributes.USER);

        Long refillableAccountNumber = Long.valueOf(
                request.getParameter(Attributes.REFILLABLE_ACCOUNT));
        List<Account> refillableAccounts = new ArrayList<>();
        refillableAccounts.add(depositAccountService.findAccountByNumber(
                refillableAccountNumber).get());

        List<Account> senderAccounts = debitAccountService.findAllByUser(user);

        request.setAttribute(Attributes.SENDER_ACCOUNTS, senderAccounts);
        request.setAttribute(Attributes.REFILLABLE_ACCOUNTS, refillableAccounts);
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
