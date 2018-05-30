package controller.command.user;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.*;
import entity.*;
import org.apache.log4j.Logger;
import service.CreditRequestService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by JohnUkraine on 28/5/2018.
 */
public class PostNewCreditRequestCommand implements ICommand {
    Logger logger = Logger.getLogger(PostNewCreditRequestCommand.class);
    private final static String INVALID_CREDIT_LIMIT = "invalid.credit.limit";
    private final static String INVALID_DATE = "invalid.date";
    private final static String INVALID_INTEREST_RATE = "invalid.interest.rate";
    private final static String SUCCESSFUL_CREATION = "request.successful.creation";

    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private final CreditRequestService creditRequestService =
            ServiceFactory.getCreditRequestService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<String> errors = validateAllData(request);

        if (errors.isEmpty()) {
            CreditRequest creditRequest = createCreditRequest(request);
            creditRequestService.createRequest(creditRequest);

            List<String> messages = new ArrayList<>();
            messages.add(SUCCESSFUL_CREATION);

            addMessageDataToSession(request, Attributes.MESSAGES, messages);

            Util.redirectTo(request, response, bundle.
                    getString("user.info"));

            return REDIRECTED;
        }

        addMessageDataToRequest(request, Attributes.ERRORS, errors);

        request.setAttribute(Attributes.INTEREST_RATE,
                request.getParameter(Attributes.INTEREST_RATE));

        request.setAttribute(Attributes.VALIDITY_DATE, new Date());

        request.setAttribute(Attributes.CREDIT_LIMIT,
                request.getParameter(Attributes.CREDIT_LIMIT));

        return Views.NEW_CREDIT_REQUEST_VIEW;
    }

    private List<String> validateAllData(HttpServletRequest request) {
        List<String> errors = new ArrayList<>();

        validateDataFromRequest(request, errors);
        if (!errors.isEmpty()) {
            return errors;
        }

        validateDate(request, errors);
        if (!errors.isEmpty()) {
            return errors;
        }

        validateNumbers(request,errors);

        return errors;
    }

    private void validateDataFromRequest(HttpServletRequest request,
                                         List<String> errors) {

        Util.validateField(new CreditLimitValidator(),
                request.getParameter(Attributes.CREDIT_LIMIT), errors);

        Util.validateField(new RateValidator(),
                request.getParameter(Attributes.INTEREST_RATE), errors);

        Util.validateField(new DateValidator(),
                request.getParameter(Attributes.VALIDITY_DATE), errors);

    }

    private void validateNumbers(HttpServletRequest request,
                                 List<String> errors) {
        BigDecimal creditLimit = new BigDecimal(request.getParameter(Attributes.CREDIT_LIMIT));

        Float interestRate = Float.valueOf(request.getParameter(Attributes.INTEREST_RATE));

        if (creditLimit.compareTo(BigDecimal.ZERO) < 0) {
            errors.add(INVALID_CREDIT_LIMIT);
        }
        if (interestRate.compareTo((float) 0) < 0)
            errors.add(INVALID_INTEREST_RATE);
    }

    private void validateDate(HttpServletRequest request,
                              List<String> errors){
        DateFormat dateConverter = new SimpleDateFormat("yyyy-MM-dd");
        Date validityDate;
        try {
            validityDate = dateConverter.parse(request.getParameter(Attributes.VALIDITY_DATE));
        } catch (ParseException e) {
            logger.debug(e);
            errors.add(INVALID_DATE);
            return;
        }
        if(new Date().compareTo(validityDate) >= 0)
            errors.add(INVALID_DATE);
    }


    private CreditRequest createCreditRequest(HttpServletRequest request) {
        User user = getUserFromSession(request.getSession());

        DateFormat dateConverter = new SimpleDateFormat("yyyy-MM-dd");
        Date validityDate = null;

        try {
            validityDate = dateConverter.parse(request.getParameter(Attributes.VALIDITY_DATE));
        } catch (ParseException e) {
            logger.error(e);
        }

        BigDecimal creditLimit = new BigDecimal(request.getParameter(Attributes.CREDIT_LIMIT));

        Float interestRate = Float.valueOf(request.getParameter(Attributes.INTEREST_RATE));

        return CreditRequest.newBuilder().
                addAccountHolder(user).
                addInterestRate(interestRate).
                addDefaultStatus().
                addValidityDate(validityDate).
                addCreditLimit(creditLimit).
                build();
    }


    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
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
