package service;


import dao.abstraction.AccountsDao;
import dao.factory.DaoFactory;
import dao.config.HibernateUtil;
import entity.Account;
import entity.AccountType;
import entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javax.xml.ws.Provider;

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

  // simple as that. And please, do Ctrl+Alt+L within your classes for auto-format in idea :)
  public Optional<Account> findAccountByNumber(long accountNumber) {
      return transactionOperation(() -> daoFactory.getAccountsDao().findOne(accountNumber));
  }

  // example how to handle session/transaction gracefully
  private <T> T transactionOperation(Supplier<T> transactionResultProvider){

    /* To be able to use underlying session context synchronization you need to specify session context holder.
    Turns out that if your'e using session context holder - you should always call getCurrentSession instead of openSession,
    as it delegates logic of obtaining AND INSTANTIATING to holder, otherwise context won't be synchronized between transactions.
    Complicated as hell. */

    try(Session session = HibernateUtil.getCurrentSession()) {
        // begin transaction
      Transaction transaction = session.beginTransaction();
      // your business logic
      T operationResult = transactionResultProvider.get();
      // commit changes
      transaction.commit();

      return operationResult;
    }
  }

    // with no return, approach the same as TransactionTemplate does
    private void transactionOperation(Runnable transactionOperations){
        try(Session session = HibernateUtil.getCurrentSession()) {
            // begin transaction
            Transaction transaction = session.beginTransaction();
            // your business logic
            transactionOperations.run();
            // commit changes
            transaction.commit();
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
