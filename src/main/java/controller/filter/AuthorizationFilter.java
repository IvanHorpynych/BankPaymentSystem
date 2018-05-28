package controller.filter;

import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.User;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public class AuthorizationFilter implements Filter {
    private final static Logger logger = Logger.getLogger(AuthorizationFilter.class);
    private final static String ACCESS_DENIED = "Access denied for page: ";

    private static final String SITE_PREFIX = "site.prefix";
    private static final String USER_PREFIX = "user.prefix";
    private static final String MANAGER_PREFIX = "manager.prefix";
    private static final String HOME_PATH = "home.path";
    private static final String LOGIN_PATH = "login.path";
    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    public final static int MANAGER_ROLE_ID = 2;
    public final static int USER_ROLE_ID = 10;


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if(!isUserLoggedIn(request)) {
            Util.redirectTo(
                    request,
                    (HttpServletResponse) servletResponse,
                    bundle.getString(LOGIN_PATH)
            );
            logInfoAboutAccessDenied(request.getRequestURI());
            return;
        }

        User user = getUserFromSession(request.getSession());

        if(isUserRoleInvalidForRequestedPage(request, user)) {
            Util.redirectTo(
                    request,
                    (HttpServletResponse) servletResponse,
                    bundle.getString(HOME_PATH)
            );
            logInfoAboutAccessDenied(request.getRequestURI());
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }

    private boolean isUserLoggedIn(HttpServletRequest request) {
        return request.getSession().getAttribute(Attributes.USER) != null;
    }

    private User getUserFromSession(HttpSession session) {
        return (User)session.getAttribute(Attributes.USER);
    }

    private boolean isUserRoleInvalidForRequestedPage(HttpServletRequest request,
                                                      User user) {
        return (isUserPage(request) && user.getRole().getId() != USER_ROLE_ID) ||
                (isManagerPage(request) && user.getRole().getId() != MANAGER_ROLE_ID);
    }

    private boolean isUserPage(HttpServletRequest request) {
        return request
                .getRequestURI()
                .startsWith(bundle.getString(SITE_PREFIX) +
                        bundle.getString(USER_PREFIX));
    }

    private boolean isManagerPage(HttpServletRequest request) {
        return request
                .getRequestURI()
                .startsWith(bundle.getString(SITE_PREFIX) +
                        bundle.getString(MANAGER_PREFIX));
    }

    private void logInfoAboutAccessDenied(String uri) {
        logger.info(ACCESS_DENIED + uri);
    }
}
