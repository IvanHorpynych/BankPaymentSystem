package service;

import dao.abstraction.CreditAccountDao;
import dao.factory.DaoFactory;
import dao.config.HibernateUtil;
import entity.CreditAccount;
import entity.User;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer. Implements operations of finding,
 * creating, deleting entities. CreditAccount dao layer.
 *
 * @author JohnUkraine
 */
public class CreditAccountService implements TransactionServiceInvoker {
  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private CreditAccountService() {}

  private static class Singleton {
    private final static CreditAccountService INSTANCE = new CreditAccountService();
  }

  public static CreditAccountService getInstance() {
    return Singleton.INSTANCE;
  }

  public List<CreditAccount> findAllCreditAccounts() {
    return transactionOperation(() -> daoFactory.getCreditAccountDao().findAll());
  }

  public Optional<CreditAccount> findAccountByNumber(long accountNumber) {
    return transactionOperation(() -> daoFactory.getCreditAccountDao().findOne(accountNumber));
  }

  public List<CreditAccount> findAllByUser(User user) {
    return transactionOperation(() -> daoFactory.getCreditAccountDao().findByUser(user));
  }

  public List<CreditAccount> findAllNotClosed() {
    return transactionOperation(() -> daoFactory.getCreditAccountDao().findAllNotClosed());
  }

  public CreditAccount createAccount(CreditAccount account) {
    return transactionOperation(() -> daoFactory.getCreditAccountDao().insert(account));
  }

  public void updateAccountStatus(CreditAccount account, int statusId) {
    transactionOperation(
        () -> daoFactory.getCreditAccountDao().updateAccountStatus(account, statusId));
  }

  public void accrue(CreditAccount creditAccount, BigDecimal accruedInterest) {
    try (Session session = HibernateUtil.getCurrentSession()) {
      session.beginTransaction();
      CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao();
      creditAccountDao.increaseBalance(creditAccount, accruedInterest);
      creditAccountDao.increaseAccruedInterest(creditAccount, accruedInterest.abs());
      session.getTransaction().commit();
    }
  }
}

