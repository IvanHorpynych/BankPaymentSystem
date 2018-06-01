package controller;

import controller.command.ICommand;
import controller.i18n.SupportedLocale;
import controller.schedulers.SchedulerInit;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Provide a centralized request handling mechanism to
 * handle all types of requests coming to the application.
 *
 * @author JohnUkraine on 5/13/2018.
 */
public class Controller extends HttpServlet {
    private final static String SUPPORTED_LOCALES = "supportedLocales";


    private ControllerHelper controllerHelper;

    @Override
    public void init() throws ServletException {
        super.init();
        controllerHelper = ControllerHelper.Singleton.getInstance();
        getServletContext().setAttribute(SUPPORTED_LOCALES,
                SupportedLocale.getSupportedLanguages());
        getServletContext().setAttribute(Attributes.USER_LIST,
                new ArrayList<User>());
        SchedulerInit.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request,
                                HttpServletResponse response)
            throws ServletException, IOException {
        ICommand command = controllerHelper.getCommand(
                getPath(request), request.getParameter("command"));

        String path = command.execute(request, response);
        if (!path.equals(ICommand.REDIRECTED)) {
            request.getRequestDispatcher(path).forward(request, response);
        }
    }

    private String getPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.replaceAll(request.getContextPath()+ResourceBundle.
                getBundle(Views.PAGES_BUNDLE).
                getString("site.prefix"), "");
    }

}
