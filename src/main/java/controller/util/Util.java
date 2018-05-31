package controller.util;

import controller.util.constants.Attributes;
import controller.util.validator.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public class Util {
    /**
     * Add next page to redirect
     *
     * @param request
     * @param response
     * @param pageToRedirect
     * @throws IOException
     */
    public static void redirectTo(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String pageToRedirect) throws IOException {
        response.sendRedirect(request.getContextPath()+
                request.getServletPath() + pageToRedirect);
    }

    /**
     * Check if the user is logged in
     * @param session
     * @return {@code true} if logged in else {@code false}
     */
    public static boolean isAlreadyLoggedIn(HttpSession session) {
        return session.getAttribute(Attributes.USER) != null;
    }

    /**
     * Performs validation of given field with provided validator.
     * If error occurs add error message to list of errors.
     *
     * @param validator
     * @param field
     * @param errors
     * @param <T> type of field for validation
     */
    public static <T> void validateField(Validator<T> validator,
                                         T field,
                                         List<String> errors) {
        if(!validator.isValid(field)) {
            errors.add(validator.getErrorKey());
        }
    }


}
