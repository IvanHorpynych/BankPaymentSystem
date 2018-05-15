package controller;

import controller.command.DefaultCommand;
import controller.command.HomeCommand;
import controller.command.ICommand;
import controller.command.authorization.*;
import controller.util.constants.PagesPaths;
import controller.util.constants.Views;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public class ControllerHelper {
    private final static String DELIMITER = ":";
    private final DefaultCommand DEFAULT_COMMAND = new DefaultCommand();
    private Map<String, ICommand> commands = new HashMap<>();
    private static final ResourceBundle bundle = ResourceBundle.
            getBundle(Views.PAGES_BUNDLE);

    private ControllerHelper() {
        init();
    }

    private void init() {
        commands.put(buildKey(bundle.getString("home.path"), null),
                new HomeCommand());
        commands.put(buildKey(bundle.getString("home.path"), "home"),
                new HomeCommand());
        commands.put(buildKey(bundle.getString("login.path"), null),
                new GetLoginCommand());
        commands.put(buildKey(bundle.getString("signup.path"), "signup"),
                new GetSignupCommand());
        commands.put(buildKey(bundle.getString("login.path"), "login_post"),
                new PostLoginCommand());
        commands.put(buildKey(bundle.getString("signup.path"), "signup_post"),
                new PostSignupCommand());
        commands.put(buildKey(bundle.getString("logout.path"), "logout"),
                new LogoutCommand());
        /*commands.put(buildKey(PagesPaths.USER_ACCOUNTS_PATH, Method.GET),
                new GetAccountsCommand());
        commands.put(buildKey(PagesPaths.USER_CARDS_PATH, Method.GET),
                new GetCardsCommand());
        commands.put(buildKey(PagesPaths.USER_PAYMENTS_PATH, Method.GET),
                new ShowPaymentsCommand());
        commands.put(buildKey(PagesPaths.USER_BLOCK_ACCOUNT, Method.POST),
                new BlockAccountCommand());
        commands.put(buildKey(PagesPaths.USER_CREATE_PAYMENT, Method.GET),
                new GetNewPaymentCommand());
        commands.put(buildKey(PagesPaths.USER_CREATE_PAYMENT, Method.POST),
                new PostNewPaymentCommand());
        commands.put(buildKey(PagesPaths.USER_REPLENISH, Method.GET),
                new GetReplenishCommand());
        commands.put(buildKey(PagesPaths.USER_REPLENISH, Method.POST),
                new PostReplenishCommand());
        commands.put(buildKey(PagesPaths.ADMIN_ACCOUNTS_PATH, Method.GET),
                new GetAllAccountsCommand());
        commands.put(buildKey(PagesPaths.ADMIN_CARDS_PATH, Method.GET),
                new GetAllCardsCommand());
        commands.put(buildKey(PagesPaths.ADMIN_PAYMENTS_PATH, Method.GET),
                new GetAllPaymentsCommand());
        commands.put(buildKey(PagesPaths.ADMIN_UNBLOCK_PATH, Method.POST),
                new UnblockAccountCommand());
        commands.put(buildKey(PagesPaths.ADMIN_CONFIRM_PATH, Method.POST),
                new ConfirmAccountCommand());
        commands.put(buildKey(PagesPaths.ADMIN_BLOCK_ACCOUNT, Method.POST),
                new BlockAccountCommand());*/
    }

    public ICommand getCommand(String path, String command) {
        return commands.getOrDefault(buildKey(path, command), DEFAULT_COMMAND);
    }

    private String buildKey(String path, String command) {
        return path + DELIMITER + command;
    }


    public static class Singleton {
        private final static ControllerHelper INSTANCE =
                new ControllerHelper();

        public static ControllerHelper getInstance() {
            return INSTANCE;
        }
    }


}
