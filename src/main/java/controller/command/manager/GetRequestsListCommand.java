package controller.command.manager;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.AccountNumberValidator;
import controller.util.validator.AmountValidator;
import entity.CreditRequest;
import entity.User;
import service.CreditRequestService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JohnUkraine on 27/5/2018.
 */
public class GetRequestsListCommand implements ICommand {
    private final CreditRequestService creditRequestService = ServiceFactory.getCreditRequestService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


        List<CreditRequest> creditRequests = creditRequestService.
                findAllPendingRequests();

        request.setAttribute(Attributes.CREDIT_REQUESTS, creditRequests);

        return Views.CREDIT_REQUEST_LIST_VIEW;
    }

}
