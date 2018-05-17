
package dao.impl.mysql;

import dao.abstraction.DepositAccountDao;
import dao.connectionsource.PooledConnection;
import dao.impl.mysql.converter.DepositAccountDtoConverter;
import dao.impl.mysql.converter.DtoConverter;
import dao.util.time.TimeConverter;
import entity.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MySqlDepositAccountDao implements DepositAccountDao {
    private final static String SELECT_ALL =
            "SELECT * FROM deposit_details ";

    private final static String WHERE_ACCOUNT_NUMBER =
            "WHERE id = ? ";

    private final static String WHERE_USER =
            "WHERE user_id = ? ";

    private final static String WHERE_STATUS =
            "WHERE status_id = ? ";

    private final static String INSERT =
            "INSERT INTO account " +
                    "(user_id, type_id, status_id) " +
                    "VALUES(?, ?, ?) ";

    private final static String INSERT_DETAILS =
            "INSERT INTO deposit_account_details " +
                    "(id, balance, last_operation, " +
                    "min_balance, annual_rate)" +
                    "VALUES(?, ?, ?, ?, ?) ";

    private final static String UPDATE =
            "UPDATE deposit_account_details SET " +
                    "balance = ?, last_operation = ?, " +
                    "min_balance = ? ";

    private final static String UPDATE_STATUS =
            "UPDATE account SET " +
                    "status_id = ? ";

    private final static String UPDATE_MIN_BALANCE =
            "UPDATE deposit_account_details SET " +
                    "min_balance = ? ";

    private final static String INCREASE_BALANCE =
            "UPDATE deposit_account_details SET " +
                    "balance = balance + ? ";

    private final static String DECREASE_BALANCE =
            "UPDATE deposit_account_details SET " +
                    "balance = balance - ? ";

    private final static String DELETE =
            "DELETE details, account FROM " +
                    "deposit_account_details AS details " +
                    "JOIN account  USING(id) ";


    private final DefaultDaoImpl<DepositAccount> defaultDao;


    public MySqlDepositAccountDao(Connection connection) {
        this(connection, new DepositAccountDtoConverter());
    }

    public MySqlDepositAccountDao(Connection connection,
                                  DtoConverter<DepositAccount> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlDepositAccountDao(DefaultDaoImpl<DepositAccount> defaultDao) {
        this.defaultDao = defaultDao;
    }

    @Override
    public Optional<DepositAccount> findOne(Long accountNumber) {
        return defaultDao.findOne(
                SELECT_ALL + WHERE_ACCOUNT_NUMBER,
                accountNumber
        );
    }

    @Override
    public List<DepositAccount> findAll() {
        return defaultDao.findAll(
                SELECT_ALL
        );
    }

    @Override
    public DepositAccount insert(DepositAccount account) {
        Objects.requireNonNull(account);

        long accountNumber = defaultDao.executeInsertWithGeneratedPrimaryKey(
                INSERT,
                account.getAccountHolder().getId(),
                account.getAccountType().getId(),
                account.getStatus().getId()
        );

        account.setAccountNumber(accountNumber);

        defaultDao.executeUpdate(INSERT_DETAILS,
                account.getAccountNumber(),
                account.getBalance(),
                TimeConverter.toTimestamp(account.getLastOperationDate()),
                account.getMinBalance(),
                account.getAnnualRate()
        );

        return account;
    }

    @Override
    public void update(DepositAccount account) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE + WHERE_ACCOUNT_NUMBER,
                account.getBalance(),
                TimeConverter.toTimestamp(account.getLastOperationDate()),
                account.getMinBalance(),
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
    public List<DepositAccount> findByUser(User user) {
        Objects.requireNonNull(user);

        return defaultDao.findAll(
                SELECT_ALL + WHERE_USER,
                user.getId()
        );
    }

    @Override
    public List<DepositAccount> findByStatus(Status status) {
        return defaultDao.findAll(
                SELECT_ALL + WHERE_STATUS,
                status.getId()
        );
    }

    @Override
    public void increaseBalance(DepositAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                INCREASE_BALANCE + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber()
        );
    }

    @Override
    public void decreaseBalance(DepositAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                DECREASE_BALANCE + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber()
        );
    }

    @Override
    public void updateAccountStatus(DepositAccount account, Status status) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE_STATUS + WHERE_ACCOUNT_NUMBER,
                status.getId(),
                account.getAccountNumber()
        );
    }

    @Override
    public void updateMinBalance(DepositAccount account, BigDecimal minBalance) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE_MIN_BALANCE + WHERE_ACCOUNT_NUMBER,
                minBalance,
                account.getAccountNumber()
        );
    }

    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        DepositAccountDao mySqlDepositAccountDao;
        try {
            System.out.println("Find all:");
            mySqlDepositAccountDao = new MySqlDepositAccountDao(dataSource.getConnection());
            ((MySqlDepositAccountDao) mySqlDepositAccountDao).printAccount(mySqlDepositAccountDao.findAll());

            int random = (int) (Math.random() * 100);

            System.out.println("Find one:");
            System.out.println(mySqlDepositAccountDao.findOne(1L));

            System.out.println("find dy user:");
            User user = User.newBuilder().setFirstName("first" + random).
                    setId(3).
                    setLastName("last" + random).
                    setEmail("test" + random + "@com").
                    setPassword("123").
                    setPhoneNumber("+123").
                    setRole(new Role(Role.USER_ROLE_ID, "USER")).
                    build();
            System.out.println(mySqlDepositAccountDao.findByUser(user));

            System.out.println("Insert:");
            DepositAccount depositAccount = (DepositAccount) mySqlDepositAccountDao.insert(
                    DepositAccount.newBuilder().
                            setAccountHolder(user).
                            setAccountType(new AccountType(8,"DEBIT")).
                            setBalance(BigDecimal.TEN).
                            setAnnualRate(2.5f).
                            setLastOperationDate(new Date()).
                            setMinBalance(BigDecimal.ONE).
                            setStatus(new Status(1,"ACTIVE")).
                            build()
            );

            System.out.println("Find all:");
            ((MySqlDepositAccountDao) mySqlDepositAccountDao).printAccount(mySqlDepositAccountDao.findAll());

            System.out.println("update:");
            depositAccount.setBalance(BigDecimal.valueOf(12345));
            mySqlDepositAccountDao.update(depositAccount);

            System.out.println("Find all:");
            ((MySqlDepositAccountDao) mySqlDepositAccountDao).printAccount(mySqlDepositAccountDao.findAll());

            System.out.println("Increase:");
            mySqlDepositAccountDao.increaseBalance(depositAccount, BigDecimal.valueOf(100));

            System.out.println("Find all:");
            ((MySqlDepositAccountDao) mySqlDepositAccountDao).printAccount(mySqlDepositAccountDao.findAll());

            System.out.println("decrease:");
            mySqlDepositAccountDao.decreaseBalance(depositAccount, BigDecimal.valueOf(2000));

            System.out.println("Find all:");
            ((MySqlDepositAccountDao) mySqlDepositAccountDao).printAccount(mySqlDepositAccountDao.findAll());

            System.out.println("update status:");
            mySqlDepositAccountDao.updateAccountStatus(depositAccount,new Status(4,"PENDING"));

            System.out.println("Find all:");
            ((MySqlDepositAccountDao) mySqlDepositAccountDao).printAccount(mySqlDepositAccountDao.findAll());

            System.out.println("update min balance:");
            mySqlDepositAccountDao.updateMinBalance(depositAccount,BigDecimal.ZERO);

            System.out.println("Find all:");
            ((MySqlDepositAccountDao) mySqlDepositAccountDao).printAccount(mySqlDepositAccountDao.findAll());

            System.out.println("delete:");
            mySqlDepositAccountDao.delete(depositAccount.getAccountNumber());

            System.out.println("Find all:");
            ((MySqlDepositAccountDao) mySqlDepositAccountDao).printAccount(mySqlDepositAccountDao.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void printAccount(List<DepositAccount> list){
        for (DepositAccount depositAccount : list) {
            System.out.println("Account: "+ depositAccount +";");
            System.out.println("Balance: "+ depositAccount.getBalance()+";");
            System.out.println("Annual Rate: "+ depositAccount.getAnnualRate()+";");
            System.out.println("Last operation: "+ depositAccount.getLastOperationDate()+";");
            System.out.println("Min Balance: "+ depositAccount.getMinBalance()+";");
            System.out.println();
        }
    }
}