package controller.command.manager;

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
public class GetAnnualRateCommand implements ICommand {

    private final RateService rateService = ServiceFactory.getRateService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Rate rate = rateService.findValidAnnualRate().get();

        request.setAttribute(Attributes.VALID_RATE,rate);

        return Views.RATE_VIEW;
    }

}
