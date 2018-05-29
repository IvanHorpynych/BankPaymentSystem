package controller.command.authorization;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Views;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public class LogoutCommand implements ICommand {
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getSession().invalidate();
        Util.redirectTo(request, response, ResourceBundle.
                getBundle(Views.PAGES_BUNDLE).getString("login.path"));
        return REDIRECTED;
    }
}
