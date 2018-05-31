package service;

import dao.abstraction.DepositAccountDao;
import dao.factory.DaoFactory;
import dao.factory.connection.DaoConnection;
import entity.DepositAccount;
import entity.Status;
import entity.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer.
 * Implements operations of finding, creating, deleting entities.
 * DepositAccount dao layer.
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
        try (DaoConnection connection = daoFactory.getConnection()) {
            DepositAccountDao depositAccountDao = daoFactory.getDepositAccountDao(connection);
            return depositAccountDao.findAll();
        }
    }

    public Optional<DepositAccount> findAccountByNumber(long accountNumber) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DepositAccountDao depositAccountDao = daoFactory.getDepositAccountDao(connection);
            return depositAccountDao.findOne(accountNumber);
        }
    }

    public List<DepositAccount> findAllByUser(User user) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DepositAccountDao depositAccountDao = daoFactory.getDepositAccountDao(connection);
            return depositAccountDao.findByUser(user);
        }
    }

    public List<DepositAccount> findAllNotClosed() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DepositAccountDao depositAccountDao = daoFactory.getDepositAccountDao(connection);
            return depositAccountDao.findAllNotClosed();
        }
    }

    public DepositAccount createAccount(DepositAccount account) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            DepositAccountDao depositAccountsDao = daoFactory.getDepositAccountDao(connection);
            DepositAccount inserted = depositAccountsDao.insert(account);
            return inserted;
        }
    }

    public void updateAccountStatus(DepositAccount account, int statusId) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            connection.startSerializableTransaction();
            DepositAccountDao depositAccountDao = daoFactory.getDepositAccountDao(connection);
            depositAccountDao.updateAccountStatus(account, statusId);
            connection.commit();
        }
    }

    public void accrue(DepositAccount account, BigDecimal interestCharges) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            connection.startSerializableTransaction();
            DepositAccountDao depositAccountDao = daoFactory.getDepositAccountDao(connection);
            depositAccountDao.update(account);
            depositAccountDao.increaseBalance(account,interestCharges);
            connection.commit();
        }
    }

}
