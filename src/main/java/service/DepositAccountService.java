package service;

import dao.abstraction.DepositAccountDao;
import dao.factory.DaoFactory;
import dao.config.HibernateUtil;
import entity.DepositAccount;
import entity.User;
import org.hibernate.Session;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer. Implements operations of finding,
 * creating, deleting entities. DepositAccount dao layer.
 *
 * @author JohnUkraine
 */
public class DepositAccountService {
  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private DepositAccountService() {

  }

  private static class Singleton {
    private final static DepositAccountService INSTANCE = new DepositAccountService();
  }

  public static DepositAccountService getInstance() {
    return Singleton.INSTANCE;
  }

  public List<DepositAccount> findAllDepositAccounts() {
    try(Session session = HibernateUtil.getInstance()) {
      return daoFactory.getDepositAccountDao().findAll();
    }
  }

  public Optional<DepositAccount> findAccountByNumber(long accountNumber) {
    try(Session session = HibernateUtil.getInstance()) {
      return daoFactory.getDepositAccountDao().findOne(accountNumber);
    }
  }

  public List<DepositAccount> findAllByUser(User user) {
    try(Session session = HibernateUtil.getInstance()) {
      return daoFactory.getDepositAccountDao().findByUser(user);
    }
  }

  public List<DepositAccount> findAllNotClosed() {
    try(Session session = HibernateUtil.getInstance()) {
      return daoFactory.getDepositAccountDao().findAllNotClosed();
    }
  }

  public DepositAccount createAccount(DepositAccount account) {
    try(Session session = HibernateUtil.getInstance()) {
      DepositAccount inserted = daoFactory.getDepositAccountDao().insert(account);
      return inserted;
    }
  }

  public void updateAccountStatus(DepositAccount account, int statusId) {
    try(Session session = HibernateUtil.getInstance()) {
      session.beginTransaction();
      daoFactory.getDepositAccountDao().updateAccountStatus(account, statusId);
      session.getTransaction().commit();
    }

  }

  public void accrue(DepositAccount account, BigDecimal interestCharges) {
    try(Session session = HibernateUtil.getInstance()) {
      session.beginTransaction();
      DepositAccountDao depositAccountDao = daoFactory.getDepositAccountDao();
      depositAccountDao.update(account);
      depositAccountDao.increaseBalance(account, interestCharges);
      session.getTransaction().commit();
    }
  }
}
