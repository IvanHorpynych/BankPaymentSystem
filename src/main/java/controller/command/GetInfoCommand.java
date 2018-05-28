package controller.command;

import controller.util.constants.Attributes;
import controller.util.constants.Views;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.events.Attribute;
import java.io.IOException;

/**
 * Created by JohnUkraine on 5/28/2018.
 */
public class GetInfoCommand implements ICommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.getAttribute(Attributes.MESSAGES);
        request.getAttribute(Attributes.ERRORS);

        return Views.INFO_VIEW;
    }
}
