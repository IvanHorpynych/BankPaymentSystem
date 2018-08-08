package service;

import dao.abstraction.DebitAccountDao;
import dao.factory.DaoFactory;
import dao.config.HibernateUtil;
import entity.Account;
import entity.User;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer. Implements operations of finding,
 * creating, deleting entities. DebitAccount dao layer.
 *
 * @author JohnUkraine
 */
public class DebitAccountService implements TransactionServiceInvoker {
  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private DebitAccountService() {}

  private static class Singleton {
    private final static DebitAccountService INSTANCE = new DebitAccountService();
  }

  public static DebitAccountService getInstance() {
    return Singleton.INSTANCE;
  }

  public List<Account> findAllDebitAccounts() {
    return transactionOperation(() -> daoFactory.getDebitAccountDao().findAll());
  }

  public Optional<Account> findAccountByNumber(long accountNumber) {
    return transactionOperation(() -> daoFactory.getDebitAccountDao().findOne(accountNumber));
  }

  public List<Account> findAllByUser(User user) {
    return transactionOperation(() -> daoFactory.getDebitAccountDao().findByUser(user));
  }

  public List<Account> findAllNotClosed() {
    return transactionOperation(() -> daoFactory.getDebitAccountDao().findAllNotClosed());
  }

  public Account createAccount(Account account) {
    return transactionOperation(() -> daoFactory.getDebitAccountDao().insert(account));
  }

  public void updateAccountStatus(Account account, int statusId) {
    try (Session session = HibernateUtil.getCurrentSession()) {
      session.beginTransaction();
      DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao();
      debitAccountDao.updateAccountStatus(account, statusId);
      session.getTransaction().commit();
    }
  }
}

