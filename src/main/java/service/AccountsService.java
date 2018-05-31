package service;


import dao.abstraction.AccountsDao;
import dao.factory.DaoFactory;
import dao.factory.connection.DaoConnection;
import entity.Account;
import entity.AccountType;
import entity.Status;
import entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer.
 * Implements operations of finding, creating, deleting entities.
 * Account dao layer.
 *
 * @author JohnUkraine
 */
public class AccountsService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    private AccountsService() {
    }

    private static class Singleton {
        private final static AccountsService INSTANCE = new AccountsService();
    }

    public static AccountsService getInstance() {
        return Singleton.INSTANCE;
    }

    public List<Account> findAllAccounts() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            AccountsDao accountsDao = daoFactory.getAccountsDao(connection);
            return accountsDao.findAll();
        }
    }

    public Optional<Account> findAccountByNumber(long accountNumber) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            AccountsDao accountsDao = daoFactory.getAccountsDao(connection);
            return accountsDao.findOne(accountNumber);
        }
    }

    public List<Account> findAllByUser(User user) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            AccountsDao accountsDao = daoFactory.getAccountsDao(connection);
            return accountsDao.findByUser(user);
        }
    }


    public Account createAccount(Account account) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            AccountsDao accountsDao = daoFactory.getAccountsDao(connection);
            Account inserted = accountsDao.insert(account);
            return inserted;
        }
    }

    public void updateAccountStatus(Account account, int statusId) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            connection.startSerializableTransaction();
            AccountsDao accountsDao = daoFactory.getAccountsDao(connection);
            accountsDao.updateAccountStatus(account, statusId);
            connection.commit();
        }
    }
    public Optional<Account> findAtmAccount() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            AccountsDao accountsDao = daoFactory.getAccountsDao(connection);
            return accountsDao.findOneByType(AccountType.TypeIdentifier.ATM_TYPE.getId());
        }
    }

}
