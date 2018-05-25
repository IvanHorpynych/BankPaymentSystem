package controller.command.user;

import controller.command.ICommand;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.Payment;
import entity.User;
import service.PaymentService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by JohnUkraine on 25/5/2018.
 */
public class GetPaymentsByUserCommand implements ICommand {
    private final PaymentService paymentService = ServiceFactory.getPaymentService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getUserFromSession(request.getSession());

        List<Payment> payments = paymentService.findAllByUser(user);

        request.setAttribute(Attributes.PAYMENTS, payments);

        return Views.PAYMENTS_VIEW;
    }

    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
    }
}
