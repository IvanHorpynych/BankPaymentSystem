package controller.util.constants;

/**
 * Created by JohnUkraine on 5/13/2018.
 */
public interface Views {
    String PAGES_BUNDLE = "pagespath";

    String FOLDER = "/WEB-INF/views/";

    String HOME_VIEW = FOLDER + "index.jsp";
    String LOGIN_VIEW = FOLDER + "login.jsp";
    String SIGNUP_VIEW = FOLDER + "signup.jsp";
    /*String ACCOUNTS_VIEW = FOLDER + "accountsList.jsp";*/
    String CREDIT_ACCOUNTS_VIEW = FOLDER + "creditAccounts.jsp";
    String DEBIT_ACCOUNTS_VIEW = FOLDER + "debitAccounts.jsp";
    String DEPOSIT_ACCOUNTS_VIEW = FOLDER + "depositAccounts.jsp";
    String CARDS_VIEW = FOLDER + "cards.jsp";
    String PAYMENTS_VIEW = FOLDER + "payments.jsp";
    String NEW_PAYMENT_VIEW = FOLDER + "createPayment.jsp";
    String REPLENISH_VIEW = FOLDER + "replenish.jsp";
}
