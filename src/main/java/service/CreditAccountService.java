package service;

import dao.abstraction.AccountDao;
import dao.abstraction.CreditAccountDao;
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
 * CreditAccount dao layer.
 *
 * @author JohnUkraine
 */
public class CreditAccountService {
    private final DaoFactory daoFactory = DaoFactory.getInstance();

    private CreditAccountService() {
    }

    private static class Singleton {
        private final static CreditAccountService INSTANCE = new CreditAccountService();
    }

    public static CreditAccountService getInstance() {
        return Singleton.INSTANCE;
    }

    public List<CreditAccount> findAllCreditAccounts() {
        try (DaoConnection connection = daoFactory.getConnection()) {
            CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(connection);
            return creditAccountDao.findAll();
        }
    }

    public Optional<CreditAccount> findAccountByNumber(long accountNumber) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(connection);
            return creditAccountDao.findOne(accountNumber);
        }
    }

    public List<CreditAccount> findAllByUser(User user) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(connection);
            return creditAccountDao.findByUser(user);
        }
    }

    public List<CreditAccount> findAllByStatus(Status status) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(connection);
            return creditAccountDao.findByStatus(status);
        }
    }

    public CreditAccount createAccount(CreditAccount account) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(connection);
            CreditAccount inserted = creditAccountDao.insert(account);
            return inserted;
        }
    }

    public void updateAccountStatus(CreditAccount account, Status status) {
        try (DaoConnection connection = daoFactory.getConnection()) {
            connection.startSerializableTransaction();
            CreditAccountDao creditAccountDao = daoFactory.getCreditAccountDao(connection);
            creditAccountDao.updateAccountStatus(account, status);
            connection.commit();
        }
    }

}
