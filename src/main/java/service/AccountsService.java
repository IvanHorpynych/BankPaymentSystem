package service;


import dao.abstraction.AccountsDao;
import dao.factory.DaoFactory;
import dao.config.HibernateUtil;
import entity.Account;
import entity.AccountType;
import entity.User;
import org.hibernate.Session;

import java.util.List;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer. Implements operations of finding,
 * creating, deleting entities. Account dao layer.
 *
 * @author JohnUkraine
 */
public class AccountsService {
  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private AccountsService() {}

  private static class Singleton {
    private final static AccountsService INSTANCE = new AccountsService();
  }

  public static AccountsService getInstance() {
    return Singleton.INSTANCE;
  }

  public List<Account> findAllAccounts() {
  try(Session session = HibernateUtil.getInstance()) {
    AccountsDao accountsDao = daoFactory.getAccountsDao();
    return accountsDao.findAll();
  }
  }

  public Optional<Account> findAccountByNumber(long accountNumber) {
    try(Session session = HibernateUtil.getInstance()) {
      AccountsDao accountsDao = daoFactory.getAccountsDao();
      return accountsDao.findOne(accountNumber);
    }
  }

  public List<Account> findAllByUser(User user) {
    try(Session session = HibernateUtil.getInstance()) {
      AccountsDao accountsDao = daoFactory.getAccountsDao();
      return accountsDao.findByUser(user);
    }

  }


  public Account createAccount(Account account) {
    try(Session session = HibernateUtil.getInstance()) {
      AccountsDao accountsDao = daoFactory.getAccountsDao();
      Account inserted = accountsDao.insert(account);
      return inserted;
    }

  }

  public void updateAccountStatus(Account account, int statusId) {
    try(Session session = HibernateUtil.getInstance()) {
      session.beginTransaction();
      AccountsDao accountsDao = daoFactory.getAccountsDao();
      accountsDao.updateAccountStatus(account, statusId);
      session.getTransaction().commit();
    }
  }


  public Optional<Account> findAtmAccount() {
    try(Session session = HibernateUtil.getInstance()) {
      AccountsDao accountsDao = daoFactory.getAccountsDao();
      return accountsDao.findOneByType(AccountType.TypeIdentifier.ATM_TYPE.getId());
    }
  }

}
