package controller.command.manager;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.CardNumberValidator;
import entity.Card;
import entity.Status;
import service.CardService;
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

public class BlockCardCommand implements ICommand {
    private final static String CARD_BLOCKED = "card.successfully.blocked";
    private final static String BLOCKED_CARD_EXSIST = "card.already.blocked";
    private final static String NO_SHUCH_CARD = "card.not.exist";

    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private final CardService cardService = ServiceFactory.getCardService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = new ArrayList<>();
        validateCard(request,errors);

        if(errors.isEmpty()) {
            blockCard(getCardFromRequest(request).get());

            addMessageToSession(request);

            Util.redirectTo(request, response,
                    bundle.getString("user.info"));

            return REDIRECTED;
        }

        request.setAttribute(Attributes.ERRORS,errors);

        return Views.INFO_VIEW;
    }

    private void validateCard(HttpServletRequest request, List<String> errors){
        Util.validateField(new CardNumberValidator(),
                request.getParameter(Attributes.CARD), errors);

        Optional<Card> cardOpt = getCardFromRequest(request);
        if(!cardOpt.isPresent()) {
            errors.add(NO_SHUCH_CARD);
        return;
        }

        Card card = cardOpt.get();

        if(card.isBlocked())
            errors.add(BLOCKED_CARD_EXSIST);

    }

    private Optional<Card> getCardFromRequest(HttpServletRequest request) {
        long cardNumber = Long.valueOf(request.getParameter(Attributes.CARD));
        return cardService.findCardByNumber(cardNumber);
    }

    private void blockCard(Card card) {

        cardService.updateCardStatus(card, Status.StatusIdentifier.BLOCKED_STATUS.getId());
    }

    private void addMessageToSession(HttpServletRequest request) {
        List<String> messages = new ArrayList<>();
        messages.add(CARD_BLOCKED);
        request.getSession().setAttribute(Attributes.MESSAGES, messages);
    }

}

