package controller.command;

import controller.command.ICommand;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.Account;
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
public class GetPaymentsByAccountCommand implements ICommand {
    private final PaymentService paymentService = ServiceFactory.getPaymentService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Long accountNumber = getAccountFromRequest(request);

        List<Payment> payments = paymentService.findAllByAccount(accountNumber);

        request.setAttribute(Attributes.PAYMENTS, payments);
        request.setAttribute(Attributes.DESIRED_ACCOUNT, accountNumber);

        return Views.PAYMENTS_VIEW;
    }

    private Long getAccountFromRequest(HttpServletRequest request) {
        return Long.valueOf(request.getParameter(Attributes.ACCOUNT));
    }
}
