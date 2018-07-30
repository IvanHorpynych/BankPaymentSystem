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
  Session session = HibernateUtil.getInstance();

  private DepositAccountService() {

  }

  private static class Singleton {
    private final static DepositAccountService INSTANCE = new DepositAccountService();
  }

  public static DepositAccountService getInstance() {
    return Singleton.INSTANCE;
  }

  public List<DepositAccount> findAllDepositAccounts() {
    return daoFactory.getDepositAccountDao(session).findAll();
  }

  public Optional<DepositAccount> findAccountByNumber(long accountNumber) {
    return daoFactory.getDepositAccountDao(session).findOne(accountNumber);
  }

  public List<DepositAccount> findAllByUser(User user) {
    return daoFactory.getDepositAccountDao(session).findByUser(user);
  }

  public List<DepositAccount> findAllNotClosed() {
    return daoFactory.getDepositAccountDao(session).findAllNotClosed();
  }

  public DepositAccount createAccount(DepositAccount account) {
    DepositAccount inserted = daoFactory.getDepositAccountDao(session).insert(account);
    return inserted;
  }

  public void updateAccountStatus(DepositAccount account, int statusId) {
    session.beginTransaction();
    daoFactory.getDepositAccountDao(session).updateAccountStatus(account, statusId);
    session.getTransaction().commit();

  }

  public void accrue(DepositAccount account, BigDecimal interestCharges) {
    session.beginTransaction();
    DepositAccountDao depositAccountDao = daoFactory.getDepositAccountDao(session);
    depositAccountDao.update(account);
    depositAccountDao.increaseBalance(account, interestCharges);
    session.getTransaction().commit();
  }
}
