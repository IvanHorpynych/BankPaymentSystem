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
import java.util.*;

/**
 * Created by JohnUkraine on 28/5/2018.
 */
public class PostCreateCardCommand implements ICommand {
    private final static String NO_SUCH_ACCOUNT = "account.not.exist";
    private final static String NO_SUCH_TYPE = "card.type.not.exist";
    private final static String ACCOUNT_STATUS_NOT_ACTIVE = "account.not.active";
    private final static String CARD_CREATE_SUCCESS = "card.create.success";

    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private final CardService cardService = ServiceFactory.getCardService();
    private final DebitAccountService debitAccountService = ServiceFactory.getDebitAccountService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<String> errors = new ArrayList<>();

        validateRequestData(request, errors);
        validateAccount(request, errors);
        validateCardType(request, errors);

        if (errors.isEmpty()) {
            Card createdCard = createCard(request);
            cardService.createCard(createdCard);

            request.getSession().setAttribute(Attributes.MESSAGES, CARD_CREATE_SUCCESS);

            Util.redirectTo(request, response,
                    bundle.getString("user.info"));

            return REDIRECTED;
        }

        User user = getUserFromSession(request.getSession());

        List<Account> debitAccounts = debitAccountService.findAllByUser(user);

        request.setAttribute(Attributes.ERRORS,errors);

        request.setAttribute(Attributes.DEBIT_ACCOUNTS, debitAccounts);

        return Views.DEBIT_ACCOUNTS_VIEW;
    }

    private void validateRequestData(HttpServletRequest request, List<String> errors) {
        Util.validateField(new AccountNumberValidator(),
                request.getParameter(Attributes.DEBIT_ACCOUNT), errors);
    }

    private void validateAccount(HttpServletRequest request, List<String> errors) {
        Optional<Account> debitAccount = debitAccountService.findAccountByNumber(
                Long.valueOf(request.getParameter(Attributes.DEBIT_ACCOUNT)));

        if (!debitAccount.isPresent()) {
            errors.add(NO_SUCH_ACCOUNT);
            return;
        }

        if (!debitAccount.get().isActive()) {
            errors.add(ACCOUNT_STATUS_NOT_ACTIVE);
        }
    }

    private Card createCard(HttpServletRequest request) {
        Account debitAccount = debitAccountService.findAccountByNumber(
                Long.valueOf(request.getParameter(Attributes.DEBIT_ACCOUNT))).get();

        String type = request.getParameter(Attributes.CARD_TYPE);

        Card.CardType cardType;
        if (Card.CardType.MASTERCARD.toString().equals(type))
            cardType = Card.CardType.MASTERCARD;
        else
            cardType = Card.CardType.VISA;

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.YEAR, 2);
        Date expireDate = c.getTime();

        return  Card.newBuilder().
                addAccount(debitAccount).
                addPin(CardPassGenerator.getRandomPin()).
                addCvv(CardPassGenerator.getRandomCvv()).
                addExpireDate(expireDate).
                addType(cardType).
                addDefaultStatus().
                build();
    }

    private void validateCardType(HttpServletRequest request, List<String> errors) {
        String cardType = request.getParameter(Attributes.CARD_TYPE);

        if (!Card.CardType.MASTERCARD.toString().equals(cardType) &&
                !Card.CardType.VISA.toString().equals(cardType))
            errors.add(NO_SUCH_TYPE);
    }

    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
    }
}
