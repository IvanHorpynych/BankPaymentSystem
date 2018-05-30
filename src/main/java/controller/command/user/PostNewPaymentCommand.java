package controller.command.user;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.*;
import entity.Account;
import entity.Card;
import entity.Payment;
import entity.User;
import service.CardService;
import service.DebitAccountService;
import service.PaymentService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by JohnUkraine on 27/5/2018.
 */
public class PostNewPaymentCommand implements ICommand {
    private final static String NO_SUCH_CARD = "card.not.exist";
    private final static String NOT_ENOUGH_MONEY = "account.insufficient.funds";
    private final static String CARD_STATUS_NOT_ACTIVE = "card.status.not.active";
    private final static String SENDER_ACCOUNT_NOT_ACTIVE = "sender.account.not.active";
    private final static String NEGATIVE_AMOUNT = "negative.amount";
    private final static String CVV_PIN_INCORRECT = "cvv.pin.incorrect";
    private final static String RECIPIENT_ACCOUNT_ALREADY_CLOSED = "recipient.account.already.closed";
    private final static String TRANSACTION_COMPLETE = "payment.success";
    private final static String COINCIDENCE_ACCOUNTS  = "coincidence.accounts";
    private final static String IMPOSSIBLE_TRANSACTION  = "impossible.transaction";

    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private final CardService cardService = ServiceFactory.getCardService();
    private final DebitAccountService accountService = ServiceFactory.getDebitAccountService();
    private final PaymentService paymentService = ServiceFactory.getPaymentService();


    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = validateAllData(request);

        if (errors.isEmpty()) {
            Payment payment = createPayment(request);
            paymentService.createPayment(payment);

            List<String> messages = new ArrayList<>();
            messages.add(TRANSACTION_COMPLETE);

            addMessageDataToSession(request, Attributes.MESSAGES, messages);

            Util.redirectTo(request, response,  bundle.
                    getString("user.info"));

            return REDIRECTED;
        }

        addMessageDataToRequest(request, Attributes.ERRORS, errors);

        addCardsToRequest(request);

        request.setAttribute(Attributes.CARD_TO,
                request.getParameter(Attributes.CARD_TO));

        return Views.NEW_PAYMENT_VIEW;
    }

    private List<String> validateAllData(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        validateDataFromRequest(request, errors);

        Optional<Card> cardToOptional = Optional.empty();
        Optional<Card> cardFromOptional = Optional.empty();

        if (errors.isEmpty()) {
            cardFromOptional = getCardOptFromRequest(request, Attributes.CARD_FROM);
            cardToOptional = getCardOptFromRequest(request, Attributes.CARD_TO);
        }

        if (!cardFromOptional.isPresent() ||
                !cardToOptional.isPresent()) {
            errors.add(NO_SUCH_CARD);
            return errors;
        }

        validateCardCredentials(request,cardFromOptional.get(),errors);
        if(!errors.isEmpty()){
            return errors;
        }

        validateCoincidenceAccounts(cardFromOptional.get(), cardToOptional.get(), errors);
        if(!errors.isEmpty()){
            return errors;
        }

        validateCardsStatus(cardFromOptional.get(), cardToOptional.get(), errors);
        if(!errors.isEmpty()){
            return errors;
        }

        validateAccounts(cardFromOptional.get(), cardToOptional.get(), errors);
        if(!errors.isEmpty()){
            return errors;
        }

        validateBalance(request, cardFromOptional.get(),cardToOptional.get(), errors);

        return errors;
    }

    private void validateDataFromRequest(HttpServletRequest request,
                                         List<String> errors) {

        Util.validateField(new CardNumberValidator(),
                getCleanCardNumber(request, Attributes.CARD_FROM), errors);

        Util.validateField(new CardNumberValidator(),
                request.getParameter(Attributes.CARD_TO), errors);

        Util.validateField(new CvvValidator(),
                request.getParameter(Attributes.CVV), errors);

        Util.validateField(new PinValidator(),
                request.getParameter(Attributes.PIN), errors);

        Util.validateField(new AmountValidator(),
                request.getParameter(Attributes.AMOUNT), errors);
    }

    private void validateCardCredentials(HttpServletRequest request,
                                            Card card, List<String> errors){
        if(!(card.getPin() == Integer.valueOf(request.getParameter(Attributes.PIN))) ||
                !(card.getCvv() == Integer.valueOf(request.getParameter(Attributes.CVV)))){
            errors.add(CVV_PIN_INCORRECT);
        }
    }

    private void validateCardsStatus(Card cardFrom, Card cardTo, List<String> errors) {
        if (!cardFrom.isActive() ||
                !cardTo.isActive())
            errors.add(CARD_STATUS_NOT_ACTIVE);
    }

    private void validateAccounts(Card cardFrom, Card cardTo,
                                  List<String> errors) {
        Account senderAccount = cardFrom.getAccount();

        Account refillableAccount = cardTo.getAccount();

        if (!senderAccount.isActive())
            errors.add(SENDER_ACCOUNT_NOT_ACTIVE);
        if (refillableAccount.isClosed())
            errors.add(RECIPIENT_ACCOUNT_ALREADY_CLOSED);
    }

    private void validateBalance(HttpServletRequest request,
                                 Card cardFrom, Card cardTo, List<String> errors) {
        Account senderAccount = cardFrom.getAccount();

        BigDecimal paymentAmount = new BigDecimal(request.getParameter(Attributes.AMOUNT));

        BigDecimal accountBalance = senderAccount.getBalance();

        if (accountBalance.compareTo(paymentAmount) < 0) {
            errors.add(NOT_ENOUGH_MONEY);
            return;
        }

        if (paymentAmount.compareTo(BigDecimal.ZERO) < 0) {
            errors.add(NEGATIVE_AMOUNT);
            return;
        }

        if(paymentAmount.add(cardTo.getAccount().getBalance()).compareTo(Account.MAX_BALANCE)>0){
            errors.add(IMPOSSIBLE_TRANSACTION);
        }
    }

    private void validateCoincidenceAccounts(Card cardFrom, Card cardTo,
                                             List<String> errors){
        if(cardFrom.getAccount().equals(cardTo.getAccount())){
            errors.add(COINCIDENCE_ACCOUNTS);
        }
    }

    private Payment createPayment(HttpServletRequest request) {

        Card cardFrom = getCardOptFromRequest(request, Attributes.CARD_FROM).get();
        Account accountFrom = cardFrom.getAccount();
        Account accountTo = getCardOptFromRequest(request, Attributes.CARD_TO).get().getAccount();


        BigDecimal amount = new BigDecimal(request.getParameter(Attributes.AMOUNT));

        Payment payment = Payment.newBuilder()
                .addAccountFrom(accountFrom)
                .addCardNumberFrom(cardFrom.getCardNumber())
                .addAccountTo(accountTo)
                .addAmount(amount)
                .addDate(new Date())
                .build();

        return payment;
    }


    private Optional<Card> getCardOptFromRequest(HttpServletRequest request,
                                                 String attribute) {
        long cardNumber;
        if (request.getParameter(attribute).contains("(") &&
                request.getParameter(attribute).contains(")"))
            cardNumber = Long.valueOf(getCleanCardNumber(request, attribute));
        else
            cardNumber = Long.valueOf(request.getParameter(attribute));

        return cardService.findCardByNumber(cardNumber);
    }

    private String getCleanCardNumber(HttpServletRequest request, String attribute) {
        return request.getParameter(attribute)
                .substring(0, request.getParameter(attribute).indexOf('(')).
                        replaceAll("\\D+","");
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

    private void addCardsToRequest(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Attributes.USER);

        List<Card> cards = cardService.findAllByUser(user);

        request.setAttribute(Attributes.CARDS, cards);
    }

}
