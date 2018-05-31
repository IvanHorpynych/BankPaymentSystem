package controller.command.manager;

import controller.command.ICommand;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.Card;
import entity.User;
import service.CardService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JohnUkraine on 25/5/2018.
 */
public class GetCardsByUserCommand extends ManagerHelper implements ICommand {

    private final CardService cardService = ServiceFactory.getCardService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = new ArrayList<>();

        validateUser(request,errors);

        if (errors.isEmpty()) {
            User user = userService.findById(
                    Long.valueOf(request.getParameter(Attributes.USER))).get();

            List<Card> cards = cardService.findAllByUser(user);

            request.setAttribute(Attributes.CARDS, cards);

            return Views.CARDS_VIEW;
        }

        request.setAttribute(Attributes.ERRORS,errors);

        return Views.INFO_VIEW;
    }
}
