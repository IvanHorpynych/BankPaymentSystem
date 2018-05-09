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
    private final static String SELECT_ALL_WHERE =
            "SELECT " +
                    "cad.id, cad.balance, cad.credit_limit, " +
                    "cad.interest_rate, cad.last_operation, " +
                    "cad.accrued_interest, cad.validity_date, " +
                    "type.id AS type_id, type.name AS type_name, " +
                    "status.id AS status_id, status.name AS status_name, " +
                    "user.id, user.first_name, " +
                    "user.last_name, user.email, " +
                    "user.password, user.phone_number, " +
                    "role.id AS role_id, role.name AS role_name " +
                    "FROM account " +
                    "JOIN user ON user_id = user.id " +
                    "JOIN role ON role_id = role.id " +
                    "JOIN account_type AS type ON type_id = type.id " +
                    "LEFT JOIN credit_account_details AS cad ON account.id = cad.id " +
                    "LEFT JOIN status ON cad.status_id = status.id " +
                    "WHERE type_id = (select id " +
                    "from account_type where name like 'CREDIT') ";

    private final static String AND_ACCOUNT_NUMBER =
            "and account.id = ? ";

    private final static String AND_USER =
            "and account.user_id = ? ";

    private final static String AND_STATUS =
            "and cad.status_id = ? ";

    private final static String WHERE_ACCOUNT_NUMBER =
            "WHERE id = ? ";

    private final static String WHERE_USER =
            "WHERE user_id = ? ";

    private final static String WHERE_STATUS =
            "WHERE status_id = ? ";

    private final static String INSERT =
            "INSERT INTO account " +
                    "(user_id, type_id) " +
                    "VALUES(?, ?) ";

    private final static String INSERT_DETAILS =
            "INSERT INTO credit_account_details " +
                    "(id, balance, credit_limit, interest_rate, " +
                    "last_operation, accrued_interest, " +
                    "validity_date, status_id) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?) ";

    private final static String UPDATE =
            "UPDATE credit_account_details SET " +
                    "balance = ?, last_operation = ?, " +
                    "accrued_interest = ?, validity_date = ?, " +
                    "status_id = ? ";

    private final static String UPDATE_STATUS =
            "UPDATE credit_account_details SET " +
                    "status_id = ? ";

    private final static String INCREASE_BALANCE =
            "UPDATE credit_account_details Set " +
                    "balance = balance + ? ";

    private final static String DECREASE_BALANCE =
            "UPDATE credit_account_details Set " +
                    "balance = balance - ? ";

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
                SELECT_ALL_WHERE + AND_ACCOUNT_NUMBER,
                accountNumber
        );
    }

    @Override
    public List<CreditAccount> findAll() {
        return defaultDao.findAll(
                SELECT_ALL_WHERE
        );
    }

    @Override
    public CreditAccount insert(CreditAccount account) {
        Objects.requireNonNull(account);

        long accountNumber = defaultDao.executeInsertWithGeneratedPrimaryKey(
                INSERT,
                account.getAccountHolder().getId(),
                account.getAccountType().getId()
        );

        account.setAccountNumber(accountNumber);

        defaultDao.executeUpdate(INSERT_DETAILS,
                account.getAccountNumber(),
                account.getBalance(),
                account.getCreditLimit(),
                account.getInterestRate(),
                TimeConverter.toTimestamp(account.getLastOperationDate()),
                account.getAccruedInterest(),
                TimeConverter.toTimestamp(account.getValidityDate()),
                account.getStatus().getId()
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
                account.getStatus().getId(),
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
                SELECT_ALL_WHERE + AND_USER,
                user.getId()
        );
    }

    @Override
    public List<CreditAccount> findByStatus(Status status) {
        Objects.requireNonNull(status);

        return defaultDao.findAll(
                SELECT_ALL_WHERE + AND_STATUS,
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

    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        CreditAccountDao mySqlCreditAccountDao;
        try {
            System.out.println("Find all:");
            mySqlCreditAccountDao = new MySqlCreditAccountDao(dataSource.getConnection());
            for (CreditAccount creditAccount : mySqlCreditAccountDao.findAll()) {
                System.out.println(creditAccount);
                System.out.println(creditAccount.getAccountType());
                System.out.println(creditAccount.getAccountHolder());
                System.out.println(creditAccount.getStatus());
                System.out.println();
            }

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
            for (CreditAccount ca : mySqlCreditAccountDao.findAll()) {
                System.out.println(ca);
                System.out.println(ca.getAccountType());
                System.out.println(ca.getAccountHolder());
                System.out.println(ca.getStatus());
                System.out.println();
            }

            System.out.println("update:");
            creditAccount.setAccruedInterest(BigDecimal.valueOf(12345));
            mySqlCreditAccountDao.update(creditAccount);

            System.out.println("Find all:");
            for (CreditAccount ca : mySqlCreditAccountDao.findAll()) {
                System.out.println(ca);
                System.out.println(ca.getAccountType());
                System.out.println(ca.getAccountHolder());
                System.out.println(ca.getStatus());
                System.out.println();
            }

            System.out.println("Increase:");
            mySqlCreditAccountDao.increaseBalance(creditAccount, BigDecimal.valueOf(100));

            System.out.println("Find all:");
            for (CreditAccount ca : mySqlCreditAccountDao.findAll()) {
                System.out.println(ca);
                System.out.println(ca.getAccountType());
                System.out.println(ca.getAccountHolder());
                System.out.println(ca.getStatus());
                System.out.println();
            }

            System.out.println("decrease:");
            mySqlCreditAccountDao.decreaseBalance(creditAccount, BigDecimal.valueOf(100));

            System.out.println("Find all:");
            for (CreditAccount ca : mySqlCreditAccountDao.findAll()) {
                System.out.println(ca);
                System.out.println(ca.getAccountType());
                System.out.println(ca.getAccountHolder());
                System.out.println(ca.getStatus());
                System.out.println();
            }

            System.out.println("update status:");
            mySqlCreditAccountDao.updateAccountStatus(creditAccount,new Status(4,"PENDING"));

            System.out.println("Find all:");
            for (CreditAccount ca : mySqlCreditAccountDao.findAll()) {
                System.out.println(ca);
                System.out.println(ca.getAccountType());
                System.out.println(ca.getAccountHolder());
                System.out.println(ca.getStatus());
                System.out.println();
            }

            System.out.println("delete:");
            mySqlCreditAccountDao.delete(creditAccount.getAccountNumber());

            System.out.println("Find all:");
            for (CreditAccount ca : mySqlCreditAccountDao.findAll()) {
                System.out.println(ca);
                System.out.println(ca.getAccountType());
                System.out.println(ca.getAccountHolder());
                System.out.println(ca.getStatus());
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
