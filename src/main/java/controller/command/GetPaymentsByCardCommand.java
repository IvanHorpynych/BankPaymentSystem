package controller.command;

import controller.command.ICommand;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.Payment;
import service.PaymentService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by JohnUkraine on 26/5/2018.
 */
public class GetPaymentsByCardCommand implements ICommand {
    private final PaymentService paymentService = ServiceFactory.getPaymentService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long cardNumber = getCardFromRequest(request);

        List<Payment> payments = paymentService.findAllByCard(cardNumber);

        request.setAttribute(Attributes.PAYMENTS, payments);

        return Views.PAYMENTS_VIEW;
    }

    private Long getCardFromRequest(HttpServletRequest request) {
        return Long.valueOf(request.getParameter(Attributes.CARD_NUMBER));
    }
}
