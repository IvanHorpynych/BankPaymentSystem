package controller.command.manager;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.RateValidator;
import entity.Rate;
import service.RateService;
import service.ServiceFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by JohnUkraine on 28/5/2018.
 */
public class PostAnnualRateCommand implements ICommand {

    private final RateService rateService = ServiceFactory.getRateService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<String> errors = new ArrayList<>();

        Util.validateField(new RateValidator(),
                request.getParameter(Attributes.ANNUAL_RATE), errors);

        if(errors.isEmpty()){
            float annualRate = Float.valueOf(
                    request.getParameter(Attributes.ANNUAL_RATE));

            Rate rate = new Rate(annualRate, new Date());

            rateService.updateAnnualRate(rate);
        }

        request.setAttribute(Attributes.ERRORS,errors);
        request.setAttribute(Attributes.VALID_RATE,rateService.findValidAnnualRate().get());

        return Views.RATE_VIEW;
    }

}
