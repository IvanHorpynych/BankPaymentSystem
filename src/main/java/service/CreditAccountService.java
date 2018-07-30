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
public class CreditAccountService {
  private final DaoFactory daoFactory = DaoFactory.getInstance();
  private final Session session = HibernateUtil.getInstance();

  private CreditAccountService() {}

  private static class Singleton {
    private final static CreditAccountService INSTANCE = new CreditAccountService();
  }

  public static CreditAccountService getInstance() {
    return Singleton.INSTANCE;
  }

  public List<CreditAccount> findAllCreditAccounts() {
    CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(session);
    return creditAccountDao.findAll();
  }

  public Optional<CreditAccount> findAccountByNumber(long accountNumber) {
    CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(session);
    return creditAccountDao.findOne(accountNumber);
  }

  public List<CreditAccount> findAllByUser(User user) {
    CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(session);
    return creditAccountDao.findByUser(user);
  }

  public List<CreditAccount> findAllNotClosed() {
    CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(session);
    return creditAccountDao.findAllNotClosed();
  }

  public CreditAccount createAccount(CreditAccount account) {
    CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(session);
    CreditAccount inserted = creditAccountDao.insert(account);
    return inserted;
  }

  public void updateAccountStatus(CreditAccount account, int statusId) {
    session.beginTransaction();
    CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(session);
    creditAccountDao.updateAccountStatus(account, statusId);
    session.getTransaction().commit();
  }

  public void accrue(CreditAccount creditAccount, BigDecimal accruedInterest) {
    session.beginTransaction();
    CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(session);
    creditAccountDao.increaseBalance(creditAccount, accruedInterest);
    creditAccountDao.increaseAccruedInterest(creditAccount, accruedInterest.abs());
    session.getTransaction().commit();
  }
}

