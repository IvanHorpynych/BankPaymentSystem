package controller.command.manager;

import controller.util.Util;
import controller.util.constants.Attributes;
import controller.util.validator.UserIdentifierValidator;
import entity.User;
import service.ServiceFactory;
import service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public abstract class ManagerHelper {
    private final static String NO_SUCH_USER = "user.not.exist";

    protected final UserService userService = ServiceFactory.getUserService();

    protected void validateUser(HttpServletRequest request, List<String> errors){
        Util.validateField(new UserIdentifierValidator(),
                request.getParameter(Attributes.USER),errors);

        if(!errors.isEmpty())
            return;

        long userIdentifier = Long.valueOf(request.getParameter(Attributes.USER));
        Optional<User> userOpt = userService.findById(userIdentifier);

        if(!userOpt.isPresent())
            errors.add(NO_SUCH_USER);

    }
}
