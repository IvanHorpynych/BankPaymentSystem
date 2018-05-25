package service;

import dao.abstraction.CreditAccountDao;
import dao.abstraction.DebitAccountDao;
import dao.factory.DaoFactory;
import dao.factory.connection.DaoConnection;
import entity.CreditAccount;
import entity.DebitAccount;
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

    public List<DebitAccount> findAllDebitAccounts() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao(connection);
            return debitAccountDao.findAll();
        }
    }

    public Optional<DebitAccount> findAccountByNumber(long accountNumber) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao(connection);
            return debitAccountDao.findOne(accountNumber);
        }
    }

    public List<DebitAccount> findAllByUser(User user) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao(connection);
            return debitAccountDao.findByUser(user);
        }
    }

    public List<DebitAccount> findAllByStatus(Status status) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao(connection);
            return debitAccountDao.findByStatus(status);
        }
    }

    public DebitAccount createAccount(DebitAccount account) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao(connection);
            DebitAccount inserted = debitAccountDao.insert(account);
            return inserted;
        }
    }

    public void updateAccountStatus(DebitAccount account, Status status) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            connection.startSerializableTransaction();
            DebitAccountDao debitAccountDao = daoFactory.getDebitAccountDao(connection);
            debitAccountDao.updateAccountStatus(account, status);
            connection.commit();
        }
    }

}
