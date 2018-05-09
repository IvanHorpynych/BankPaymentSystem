/*
package dao.impl.mysql;

import dao.abstraction.RegularAccountDao;
import dao.impl.mysql.DefaultDaoImpl;
import dao.impl.mysql.converter.CreditAccountDtoConverter;
import dao.impl.mysql.converter.DtoConverter;
import entity.Account;
import entity.User;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MySqlCreditRequestDao implements RegularAccountDao {
    private final static String SELECT_ALL =
            "SELECT accounts.account_number, accounts.account_holder, " +
                    "accounts.balance, accounts.status, " +
                    "users.user_id, users.user_type, users.first_name, " +
                    "users.last_name, users.email, users.passhash, " +
                    "users.phone_number, " +
                    "roles.role_id, roles.role_name " +
                    "FROM accounts " +
                    "JOIN users ON account_holder = user_id " +
                    "JOIN roles ON user_type = role_id ";

    private final static String WHERE_ACCOUNT_NUMBER = "" +
            "WHERE accounts.account_number = ? ";

    private final static String WHERE_USER =
            "WHERE accounts.account_holder = ? ";

    private final static String WHERE_STATUS =
            "WHERE accounts.status = ? ";

    private final static String INSERT =
            "INSERT INTO accounts" +
                    "(account_holder, " +
                    "balance, status) " +
                    "VALUES(?, ?, ?) ";

    private final static String UPDATE =
            "UPDATE accounts SET " +
                    "account_holder = ?, balance = ?, " +
                    "status = ? ";

    private final static String UPDATE_STATUS =
            "UPDATE accounts SET " +
                    "status = ? ";

    private final static String INCREASE_AMOUNT =
            "UPDATE accounts Set " +
                    "balance = balance + ? ";

    private final static String DECREASE_AMOUNT =
            "UPDATE accounts Set " +
                    "balance = balance - ? ";

    private final static String DELETE =
            "DELETE FROM accounts ";


    private final DefaultDaoImpl<Account> defaultDao;


    public MySqlCreditRequestDao(Connection connection) {
        this(connection, new CreditAccountDtoConverter());
    }

    public MySqlCreditRequestDao(Connection connection,
                                 DtoConverter<Account> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlCreditRequestDao(DefaultDaoImpl<Account> defaultDao) {
        this.defaultDao = defaultDao;
    }

    @Override
    public Optional<Account> findOne(Long accountNumber) {
        return defaultDao.findOne(
                SELECT_ALL + WHERE_ACCOUNT_NUMBER,
                accountNumber
        );
    }

    @Override
    public List<Account> findAll() {
        return defaultDao.findAll(
                SELECT_ALL
        );
    }

    @Override
    public Account insert(Account account) {
        Objects.requireNonNull(account);

        long accountNumber = defaultDao.executeInsertWithGeneratedPrimaryKey(
                INSERT,
                account.getAccountHolder().getId(),
                account.getBalance(),
                account.getStatus().toString()
        );

        account.setAccountNumber(accountNumber);

        return account;
    }

    @Override
    public void update(Account account) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE + WHERE_ACCOUNT_NUMBER,
                account.getAccountHolder().getId(),
                account.getBalance(),
                account.getStatus().toString(),
                account.getAccountNumber()
        );
    }

    @Override
    public void delete(Long accountNumber) {
        defaultDao.executeUpdate(
                DELETE + WHERE_ACCOUNT_NUMBER,
                accountNumber
        );
    }

    @Override
    public List<Account> findByUser(User user) {
        Objects.requireNonNull(user);

        return defaultDao.findAll(
                SELECT_ALL + WHERE_USER,
                user.getId()
        );
    }

    @Override
    public List<Account> findByStatus(Account.Status status) {
        return defaultDao.findAll(
                SELECT_ALL + WHERE_STATUS,
                status.toString()
        );
    }

    @Override
    public void increaseBalance(Account account, BigDecimal amount) {
        defaultDao.executeUpdate(
                INCREASE_AMOUNT + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber());
    }

    @Override
    public void decreaseBalance(Account account, BigDecimal amount) {
        defaultDao.executeUpdate(
                DECREASE_AMOUNT + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber()
        );
    }

    @Override
    public void updateAccountStatus(Account account, Account.Status status) {
        defaultDao.executeUpdate(
                UPDATE_STATUS + WHERE_ACCOUNT_NUMBER,
                status.toString(), account.getAccountNumber()
        );
    }

}
*/
