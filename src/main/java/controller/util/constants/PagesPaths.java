package controller.util.constants;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public interface PagesPaths {
    String SITE_PREFIX = "/site";

    String USER_PREFIX = "/user";
    String ADMIN_PREFIX = "/admin";

    String HOME_PATH = "/home";
    String LOGIN_PATH = "/login";
    String SIGNUP_PATH = "/signup";
    String LOGOUT_PATH = "/logout";

    String USER_ACCOUNTS_PATH = USER_PREFIX + "/accounts";
    String USER_CARDS_PATH = USER_PREFIX + "/cards";
    String USER_PAYMENTS_PATH = USER_PREFIX + "/payments";
    String USER_BLOCK_ACCOUNT = USER_ACCOUNTS_PATH + "/block";
    String USER_CREATE_PAYMENT = USER_PREFIX + "/create";
    String USER_REPLENISH = USER_PREFIX + "/replenish";

    String ADMIN_ACCOUNTS_PATH = ADMIN_PREFIX + "/accounts";
    String ADMIN_CARDS_PATH = ADMIN_PREFIX + "/cards";
    String ADMIN_PAYMENTS_PATH = ADMIN_PREFIX + "/payments";
    String ADMIN_UNBLOCK_PATH = ADMIN_ACCOUNTS_PATH + "/unblock";
    String ADMIN_CONFIRM_PATH = ADMIN_ACCOUNTS_PATH + "/confirm";
    String ADMIN_BLOCK_ACCOUNT = ADMIN_ACCOUNTS_PATH + "/block";
}
