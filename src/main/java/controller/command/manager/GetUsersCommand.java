package controller.command.manager;

import controller.command.ICommand;
import controller.util.constants.Attributes;
import controller.util.constants.Views;
import entity.User;
import service.ServiceFactory;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by JohnUkraine on 30/5/2018.
 */
public class GetUsersCommand implements ICommand {

    private final UserService userService = ServiceFactory.getUserService();

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<User> users = userService.findAllUsers();

        request.setAttribute(Attributes.USERS, users);

        return Views.USERS_VIEW;
    }

}
