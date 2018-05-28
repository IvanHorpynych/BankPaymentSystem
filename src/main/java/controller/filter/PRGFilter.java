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
public class PRGFilter implements Filter {
    private final static Logger logger = Logger.getLogger(PRGFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpSession session = request.getSession();

        request.setAttribute(Attributes.ERRORS,session.getAttribute(Attributes.ERRORS));
        session.removeAttribute(Attributes.ERRORS);

        request.setAttribute(Attributes.MESSAGES,session.getAttribute(Attributes.MESSAGES));
        session.removeAttribute(Attributes.MESSAGES);

        request.setAttribute(Attributes.WARNING,session.getAttribute(Attributes.WARNING));
        session.removeAttribute(Attributes.WARNING);

        request.setAttribute(Attributes.AMOUNT,session.getAttribute(Attributes.AMOUNT));
        session.removeAttribute(Attributes.AMOUNT);

        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }

}
