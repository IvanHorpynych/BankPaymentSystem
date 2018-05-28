package controller.command.authorization;

import controller.command.ICommand;
import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import controller.util.validator.EmailValidator;
import controller.util.validator.PasswordValidator;
import entity.User;
import service.ServiceFactory;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public class PostLoginCommand implements ICommand {
    private final static String EMAIL_PARAM = "email";
    private final static String PASSWORD_PARAM = "password";
    private final static String INVALID_CREDENTIALS =
            "invalid.credentials";
    private final static String ACTIVE_ACCOUNT_IS_EXIST =
            "active.account.exist";
    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private final UserService userService = ServiceFactory.getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if(Util.isAlreadyLoggedIn(request.getSession())) {
            Util.redirectTo(request, response, bundle.
                    getString("home.path"));
            return REDIRECTED;
        }

        User userDto = getDataFromRequest(request);

        List<String> errors = validateData(userDto);
        errors.addAll(
                validateUniquenessActiveUser(request.getSession(),userDto));

        if(errors.isEmpty()) {
            User user = loadUserFromDatabase(userDto.getEmail());
            addUserToContext(request.getSession(),user);
            addUserToSession(request.getSession(), user);
            Util.redirectTo(request, response,  bundle.
                    getString("home.path"));

            return REDIRECTED;
        }

        addInvalidDataToRequest(request, userDto, errors);

        return Views.LOGIN_VIEW;
    }

    private User getDataFromRequest(HttpServletRequest request) {
        return User.newBuilder()
                .addEmail(request.getParameter(EMAIL_PARAM))
                .addPassword(request.getParameter(PASSWORD_PARAM))
                .build();
    }

    private List<String> validateData(User user) {
        List<String> errors = new ArrayList<>();

        Util.validateField(new EmailValidator(), user.getEmail(), errors);
        Util.validateField(new PasswordValidator(), user.getPassword(), errors);

        /* Check if entered password matches with user password only in case,
            when email and password is valid
        */
        if(errors.isEmpty() && !userService.
                isCredentialsValid(user.getEmail(), user.getPassword())) {
            errors.add(INVALID_CREDENTIALS);
        }

        return errors;
    }

    public List<String> validateUniquenessActiveUser(HttpSession session, User user){
        List<String> errors = new ArrayList<>();
        @SuppressWarnings("unchecked")
        List<User> activeUserList = (List<User>) session.getServletContext().
                getAttribute(Attributes.USER_LIST);
        if (activeUserList.contains(user))
            errors.add(ACTIVE_ACCOUNT_IS_EXIST);
        return errors;
    }

    private User loadUserFromDatabase(String email) {
        Optional<User> userOptional = userService.findByEmail(email);
        return userOptional.orElseThrow(IllegalStateException::new);
    }

    private void addUserToSession(HttpSession session, User user)
            throws IOException {
        session.setAttribute(Attributes.USER, user);
    }

    private void addUserToContext(HttpSession session, User user)
            throws IOException {
        @SuppressWarnings("unchecked")
        List<User> activeUserList = (List<User>) session.getServletContext().
                        getAttribute(Attributes.USER_LIST);
        activeUserList.add(user);
    }

    private void addInvalidDataToRequest(HttpServletRequest request,
                                         User user,
                                         List<String> errors) {
        request.setAttribute(Attributes.USER, user);
        request.setAttribute(Attributes.ERRORS, errors);
    }

}
