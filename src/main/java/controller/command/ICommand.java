package controller.command;

import controller.Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Specialize interface for command in front controller pattern.
 *
 * @see Controller
 * @author JohnUkraine
 */
public interface ICommand {
    /**
     * Return from execute in case when redirecting action happens
     */
    String REDIRECTED ="REDIRECTED";

    /**
     * Process request of user.
     *
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    String execute(HttpServletRequest request,
                   HttpServletResponse response)
            throws ServletException, IOException;
}
