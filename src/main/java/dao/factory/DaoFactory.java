package dao.factory;

import dao.abstraction.*;
import dao.exception.DaoException;
import entity.AccountType;
import org.apache.log4j.Logger;
import org.hibernate.Session;

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
   * Gets factory class name from certain properties file. Reflection used for more flexibility.
   *
   * @return specific implemented factory
   */
  public static DaoFactory getInstance() {
    if (instance == null) {
      ResourceBundle bundle = ResourceBundle.getBundle(DB_BUNDLE);
      String className = bundle.getString(DB_CLASS);
      try {
        instance = (DaoFactory) Class.forName(className).getConstructor().newInstance();
      } catch (Exception e) {
        logger.error(e);
        throw new DaoException(e);
      }
    }

    return instance;
  }



  public abstract UserDao getUserDao();

  public abstract GenericAccountDao getAccountDao(AccountType accountType);

  public abstract CreditAccountDao getCreditAccountDao();

  public abstract DepositAccountDao getDepositAccountDao();

  public abstract DebitAccountDao getDebitAccountDao();

  public abstract AccountsDao getAccountsDao();

  public abstract RateDao getRateDao();

  public abstract CardDao getCardDao();

  public abstract PaymentDao getPaymentDao();

  public abstract CreditRequestDao getCreditRequestDao();

}
