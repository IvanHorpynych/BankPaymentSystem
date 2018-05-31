package service;

import dao.abstraction.CreditAccountDao;
import dao.abstraction.DebitAccountDao;
import dao.factory.DaoFactory;
import dao.factory.connection.DaoConnection;
import entity.Account;
import entity.CreditAccount;
import entity.Status;
import entity.User;

import java.util.List;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer.
 * Implements operations of finding, creating, deleting entities.
 * DebitAccount dao layer.
 *
 * @author JohnUkraine
 */
public class DebitAccountService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    private DebitAccountService() {
    }

    private static class Singleton {
        private final static DebitAccountService INSTANCE = new DebitAccountService();
    }

    public static DebitAccountService getInstance() {
        return Singleton.INSTANCE;
    }

    public List<Account> findAllDebitAccounts() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao(connection);
            return debitAccountDao.findAll();
        }
    }

    public Optional<Account> findAccountByNumber(long accountNumber) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao(connection);
            return debitAccountDao.findOne(accountNumber);
        }
    }

    public List<Account> findAllByUser(User user) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao(connection);
            return debitAccountDao.findByUser(user);
        }
    }

    public List<Account> findAllNotClosed() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao(connection);
            return debitAccountDao.findAllNotClosed();
        }
    }

    public Account createAccount(Account account) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao(connection);
            Account inserted = debitAccountDao.insert(account);
            return inserted;
        }
    }

    public void updateAccountStatus(Account account, int statusId) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            connection.startSerializableTransaction();
            DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao(connection);
            debitAccountDao.updateAccountStatus(account, statusId);
            connection.commit();
        }
    }

}
