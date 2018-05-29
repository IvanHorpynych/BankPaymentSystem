package controller.command.user;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.DepositAccount;
import entity.Rate;
import entity.User;
import service.DepositAccountService;
import service.RateService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by JohnUkraine on 28/5/2018.
 */
public class PostCreateDepositCommand implements ICommand {
    private final static String ACCOUNT_CREATE_SUCCESS = "account.create.success";
    private final static String RATE_NOT_FOUND = "rate.not.found";

    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private final DepositAccountService depositAccountService = ServiceFactory.getDepositAccountService();
    private final RateService rateService = ServiceFactory.getRateService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<String> errors = new ArrayList<>();
        validateRate(errors);

        if (errors.isEmpty()) {
            DepositAccount depositAccount = createDefaultAccount(request);
            depositAccountService.createAccount(depositAccount);

            request.getSession().setAttribute(Attributes.MESSAGES, ACCOUNT_CREATE_SUCCESS);

            Util.redirectTo(request, response,
                    bundle.getString("user.info"));

            return REDIRECTED;
        }

        request.setAttribute(Attributes.ERRORS,errors);

        return Views.INFO_VIEW;

    }

    private void validateRate(List<String> errors){
        Optional<Rate> rateOpt = rateService.findValidAnnualRate();

        if(!rateOpt.isPresent())
            errors.add(RATE_NOT_FOUND);
    }

    private DepositAccount createDefaultAccount(HttpServletRequest request) {
        User accountHolder =  getUserFromSession(request.getSession());
        Rate rate = rateService.findValidAnnualRate().get();

        return  DepositAccount.newDepositBuilder().
                addAccountHolder(accountHolder).
                addDefaultAccountType().
                addDefaultStatus().
                addBalance(BigDecimal.ZERO).
                addAnnualRate(rate.getAnnualRate()).
                build();
    }

    private User getUserFromSession(HttpSession session) {
        return (User) session.getAttribute(Attributes.USER);
    }
}
