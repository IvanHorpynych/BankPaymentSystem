package controller.command.manager;

import controller.command.ICommand;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.Rate;
import service.RateService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
