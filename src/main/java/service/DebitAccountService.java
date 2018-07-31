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
public class DebitAccountService {
  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private DebitAccountService() {}

  private static class Singleton {
    private final static DebitAccountService INSTANCE = new DebitAccountService();
  }

  public static DebitAccountService getInstance() {
    return Singleton.INSTANCE;
  }

  public List<Account> findAllDebitAccounts() {
    try(Session session = HibernateUtil.getInstance()) {
      DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao();
      return debitAccountDao.findAll();
    }
  }

  public Optional<Account> findAccountByNumber(long accountNumber) {
    try(Session session = HibernateUtil.getInstance()) {
      DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao();
      return debitAccountDao.findOne(accountNumber);
    }
  }

  public List<Account> findAllByUser(User user) {
    try(Session session = HibernateUtil.getInstance()) {
      DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao();
      return debitAccountDao.findByUser(user);
    }
  }

  public List<Account> findAllNotClosed() {
    try(Session session = HibernateUtil.getInstance()) {
      DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao();
      return debitAccountDao.findAllNotClosed();
    }
  }

  public Account createAccount(Account account) {
    try(Session session = HibernateUtil.getInstance()) {
      DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao();
      Account inserted = debitAccountDao.insert(account);
      return inserted;
    }
  }

  public void updateAccountStatus(Account account, int statusId) {
    try(Session session = HibernateUtil.getInstance()) {
      session.beginTransaction();
      DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao();
      debitAccountDao.updateAccountStatus(account, statusId);
      session.getTransaction().commit();
    }
  }
}

