package controller;

import controller.command.ICommand;
import controller.i18n.SupportedLocale;
import controller.util.constants.PagesPaths;
import controller.util.constants.Views;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
@WebServlet(name = "test", urlPatterns = "/site/*")

/**
 * Provide a centralized request handling mechanism to
 * handle all types of requests coming to the application.
 *
 */
public class FrontController extends HttpServlet {
    private final static String SUPPORTED_LOCALES = "supportedLocales";


    private ControllerHelper controllerHelper;

    @Override
    public void init() throws ServletException {
        super.init();
        controllerHelper = ControllerHelper.Singleton.getInstance();
        getServletContext().setAttribute(SUPPORTED_LOCALES,
                SupportedLocale.getSupportedLanguages());
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
        return uri.replaceAll(ResourceBundle.
                getBundle(Views.PAGES_BUNDLE).
                getString("site.prefix"), "");
    }

}
