package service;


import dao.factory.DaoFactory;
import entity.Account;
import entity.AccountType;
import entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer. Implements operations of finding,
 * creating, deleting entities. Account dao layer.
 *
 * @author JohnUkraine
 */
public class AccountsService implements TransactionServiceInvoker {
  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private AccountsService() {}

  private static class Singleton {
    private final static AccountsService INSTANCE = new AccountsService();
  }

  public static AccountsService getInstance() {
    return Singleton.INSTANCE;
  }



  public List<Account> findAllAccounts() {
    return transactionOperation(() -> daoFactory.getAccountsDao().findAll());
  }

  public Optional<Account> findAccountByNumber(long accountNumber) {
    return transactionOperation(() -> daoFactory.getAccountsDao().findOne(accountNumber));
  }


  public List<Account> findAllByUser(User user) {
    return transactionOperation(() -> daoFactory.getAccountsDao().findByUser(user));
  }


  public Account createAccount(Account account) {
    return transactionOperation(() -> daoFactory.getAccountsDao().insert(account));
  }

  public void updateAccountStatus(Account account, int statusId) {
    transactionOperation(() -> daoFactory.getAccountsDao().updateAccountStatus(account, statusId));
  }


  public Optional<Account> findAtmAccount() {
    return transactionOperation(() -> daoFactory.getAccountsDao()
        .findOneByType(AccountType.TypeIdentifier.ATM_TYPE.getId()));
  }

}
