package controller;

import controller.command.DefaultCommand;
import controller.command.HomeCommand;
import controller.command.ICommand;
import controller.command.authorization.*;
import controller.command.user.*;
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
        commands.put(buildKey(bundle.getString("signup.path"), null),
                new GetSignupCommand());
        commands.put(buildKey(bundle.getString("login.path"), "login_post"),
                new PostLoginCommand());
        commands.put(buildKey(bundle.getString("signup.path"), "signup_post"),
                new PostSignupCommand());
        commands.put(buildKey(bundle.getString("logout.path"), "logout"),
                new LogoutCommand());
        commands.put(buildKey(bundle.getString("user.credit.account.path"), null),
                new GetCreditAccountsCommand());
        commands.put(buildKey(bundle.getString("user.debit.account.path"), null),
                new GetDebitAccountsCommand());
        commands.put(buildKey(bundle.getString("user.deposit.account.path"), null),
                new GetDepositAccountsCommand());
        commands.put(buildKey(bundle.getString("user.card.path"), null),
                new GetCardsCommand());
        commands.put(buildKey(bundle.getString("user.payment.path"), "accountPayments"),
                new GetPaymentsByAccountCommand());
        commands.put(buildKey(bundle.getString("user.payment.path"), null),
                new GetPaymentsByUserCommand());
        commands.put(buildKey(bundle.getString("user.payment.path"), "cardPayments"),
                new GetPaymentsByCardCommand());
        commands.put(buildKey(bundle.getString("user.replenish"), "credit_replenish"),
                new GetReplenishCommand());

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
