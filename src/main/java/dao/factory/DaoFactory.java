package dao.factory;

import dao.abstraction.*;
import dao.exception.DaoException;
import dao.factory.connection.DaoConnection;
import entity.AccountType;
import org.apache.log4j.Logger;

import java.util.ResourceBundle;

/**
 * Factory that creates DAO entities
 *
 * @author JohnUkraine
 */
public abstract class DaoFactory {
    private static final String DB_BUNDLE = "database";
    private static final String DB_CLASS = "factory.class";
    private final static Logger logger = Logger.getLogger(DaoFactory.class);

    private static DaoFactory instance;

    /**
     * Gets factory class name from certain properties file.
     * Reflection used for more flexibility.
     *
     * @return specific implemented factory
     */
    public static DaoFactory getInstance() {
        if (instance == null) {
            ResourceBundle bundle = ResourceBundle.getBundle(DB_BUNDLE);
            String className = bundle.getString(DB_CLASS);
            try {
                instance = (DaoFactory) Class.forName(className).
                        getConstructor().newInstance();
            } catch (Exception e) {
                logger.error(e);
                throw new DaoException(e);
            }
        }

        return instance;
    }

    public abstract DaoConnection getConnection();

    public abstract RoleDao getRoleDao(DaoConnection connection);

    public abstract UserDao getUserDao(DaoConnection connection);

    public abstract GenericAccountDao getAccountDao(DaoConnection connection,
                                                    AccountType accountType);

    public abstract CreditAccountDao getCreditAccountDao(DaoConnection connection);

    public abstract DepositAccountDao getDepositAccountDao(DaoConnection connection);

    public abstract DebitAccountDao getDebitAccountDao(DaoConnection connection);

    public abstract AccountsDao getAccountsDao(DaoConnection connection);

    public abstract AccountTypeDao getAccountTypeDao(DaoConnection connection);

    public abstract StatusDao getStatusDao(DaoConnection connection);

    public abstract RateDao getRateDao(DaoConnection connection);

    public abstract CardDao getCardDao(DaoConnection connection);

    public abstract PaymentDao getPaymentDao(DaoConnection connection);

    public abstract CreditRequestDao getCreditRequestDao(DaoConnection connection);

}
