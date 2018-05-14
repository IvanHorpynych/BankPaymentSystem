package dao.impl.mysql;

import dao.abstraction.CreditAccountDao;
import dao.connectionsource.PooledConnection;
import dao.impl.mysql.converter.CreditAccountDtoConverter;
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

public class MySqlCreditAccountDao implements CreditAccountDao {
    private final static String SELECT_ALL =
            "SELECT * FROM credit_details ";

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
            "INSERT INTO credit_account_details " +
                    "(id, balance, credit_limit, interest_rate, " +
                    "last_operation, accrued_interest, " +
                    "validity_date) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?) ";

    private final static String UPDATE =
            "UPDATE credit_account_details SET " +
                    "balance = ?, last_operation = ?, " +
                    "accrued_interest = ?, validity_date = ? ";

    private final static String UPDATE_STATUS =
            "UPDATE account SET " +
                    "status_id = ? ";

    private final static String INCREASE_BALANCE =
            "UPDATE credit_account_details SET " +
                    "balance = balance + ? ";

    private final static String DECREASE_BALANCE =
            "UPDATE credit_account_details SET " +
                    "balance = balance - ? ";

    private final static String INCREASE_ACCRUED_INTEREST =
            "UPDATE credit_account_details SET " +
                    "accrued_interest = accrued_interest + ? ";

    private final static String DECREASE_ACCRUED_INTEREST =
            "UPDATE credit_account_details SET " +
                    "accrued_interest = accrued_interest - ? ";

    private final static String DELETE =
            "DELETE details, account FROM " +
                    "credit_account_details AS details " +
                    "JOIN account  USING(id) ";


    private final DefaultDaoImpl<CreditAccount> defaultDao;


    public MySqlCreditAccountDao(Connection connection) {
        this(connection, new CreditAccountDtoConverter());
    }

    public MySqlCreditAccountDao(Connection connection,
                                 DtoConverter<CreditAccount> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlCreditAccountDao(DefaultDaoImpl<CreditAccount> defaultDao) {
        this.defaultDao = defaultDao;
    }


    @Override
    public Optional<CreditAccount> findOne(Long accountNumber) {
        return defaultDao.findOne(
                SELECT_ALL + WHERE_ACCOUNT_NUMBER,
                accountNumber
        );
    }

    @Override
    public List<CreditAccount> findAll() {
        return defaultDao.findAll(
                SELECT_ALL
        );
    }

    @Override
    public CreditAccount insert(CreditAccount account) {
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
                account.getCreditLimit(),
                account.getInterestRate(),
                TimeConverter.toTimestamp(account.getLastOperationDate()),
                account.getAccruedInterest(),
                TimeConverter.toTimestamp(account.getValidityDate())
        );

        return account;
    }

    @Override
    public void update(CreditAccount account) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE + WHERE_ACCOUNT_NUMBER,
                account.getBalance(),
                TimeConverter.toTimestamp(account.getLastOperationDate()),
                account.getAccruedInterest(),
                TimeConverter.toTimestamp(account.getValidityDate()),
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
    public List<CreditAccount> findByUser(User user) {
        Objects.requireNonNull(user);

        return defaultDao.findAll(
                SELECT_ALL + WHERE_USER,
                user.getId()
        );
    }

    @Override
    public List<CreditAccount> findByStatus(Status status) {
        Objects.requireNonNull(status);

        return defaultDao.findAll(
                SELECT_ALL + WHERE_STATUS,
                status.getId()
        );

    }

    @Override
    public void increaseBalance(CreditAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                INCREASE_BALANCE + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber()
        );
    }

    @Override
    public void decreaseBalance(CreditAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                DECREASE_BALANCE + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber()
        );
    }

    @Override
    public void updateAccountStatus(CreditAccount account, Status status) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE_STATUS + WHERE_ACCOUNT_NUMBER,
                status.getId(),
                account.getAccountNumber()
        );
    }

    @Override
    public void increaseAccruedInterest(CreditAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                INCREASE_ACCRUED_INTEREST + WHERE_ACCOUNT_NUMBER,
                account,
                account.getAccountNumber()
        );
    }

    @Override
    public void decreaseAccruedInterest(CreditAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                DECREASE_ACCRUED_INTEREST + WHERE_ACCOUNT_NUMBER,
                account,
                account.getAccountNumber()
        );
    }


    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        CreditAccountDao mySqlCreditAccountDao;
        try {
            System.out.println("Find all:");
            mySqlCreditAccountDao = new MySqlCreditAccountDao(dataSource.getConnection());
            ((MySqlCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());

            int random = (int) (Math.random() * 100);

            System.out.println("Find one:");
            System.out.println(mySqlCreditAccountDao.findOne(2L));

            System.out.println("find dy user:");
            User user = User.newBuilder().setFirstName("first" + random).
                    setId(3).
                    setLastName("last" + random).
                    setEmail("test" + random + "@com").
                    setPassword("123").
                    setPhoneNumber("+123").
                    setRole(new Role(Role.USER_ROLE_ID, "USER")).
                    build();
            System.out.println(mySqlCreditAccountDao.findByUser(user));

            System.out.println("Insert:");
            CreditAccount creditAccount = (CreditAccount) mySqlCreditAccountDao.insert(
                    CreditAccount.newBuilder().
                            setAccountHolder(user).
                            setAccountType(new AccountType(4,"CREDIT")).
                            setBalance(BigDecimal.ONE).
                            setCreditLimit(BigDecimal.TEN).
                            setInterestRate(2L).
                            setLastOperationDate(new Date()).
                            setAccruedInterest(BigDecimal.ZERO).
                            setValidityDate(new Date()).
                            setStatus(new Status(1,"ACTIVE")).
                            build()
            );

            System.out.println("Find all:");
            ((MySqlCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());

            System.out.println("update:");
            creditAccount.setAccruedInterest(BigDecimal.valueOf(12345));
            mySqlCreditAccountDao.update(creditAccount);

            System.out.println("Find all:");
            ((MySqlCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());

            System.out.println("Increase:");
            mySqlCreditAccountDao.increaseBalance(creditAccount, BigDecimal.valueOf(100));

            System.out.println("Find all:");
            ((MySqlCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());

            System.out.println("decrease:");
            mySqlCreditAccountDao.decreaseBalance(creditAccount, BigDecimal.valueOf(100));

            System.out.println("Find all:");
            ((MySqlCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());

            System.out.println("update status:");
            mySqlCreditAccountDao.updateAccountStatus(creditAccount,new Status(4,"PENDING"));

            System.out.println("Find all:");
            ((MySqlCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());

            System.out.println("delete:");
            mySqlCreditAccountDao.delete(creditAccount.getAccountNumber());

            System.out.println("Find all:");
            ((MySqlCreditAccountDao) mySqlCreditAccountDao).printAccount(mySqlCreditAccountDao.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void printAccount(List<CreditAccount> list){
        for (CreditAccount creditAccount : list) {
            System.out.println("Account: "+creditAccount+";");
            System.out.println("Balance: "+creditAccount.getBalance()+";");
            System.out.println("Credit limit: "+creditAccount.getCreditLimit()+";");
            System.out.println("Interest Rate: "+creditAccount.getInterestRate()+";");
            System.out.println("Last operation: "+creditAccount.getLastOperationDate()+";");
            System.out.println("Accrued interest: "+creditAccount.getAccruedInterest()+";");
            System.out.println("Validity date: "+creditAccount.getValidityDate()+";");
            System.out.println();
        }
    }

}
