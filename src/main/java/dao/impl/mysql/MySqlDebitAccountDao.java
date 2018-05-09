
package dao.impl.mysql;

import dao.abstraction.DebitAccountDao;
import dao.connectionsource.PooledConnection;
import dao.impl.mysql.converter.DebitAccountDtoConverter;
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

public class MySqlDebitAccountDao implements DebitAccountDao {
    private final static String SELECT_ALL =
            "SELECT " +
                    "dad.id, dad.balance, " +
                    "dad.annual_rate, dad.last_operation, " +
                    "dad.min_balance, " +
                    "type.id AS type_id, type.name AS type_name, " +
                    "status.id AS status_id, " +
                    "status.name AS status_name, " +
                    "user.id AS user_id, user.first_name, " +
                    "user.last_name, user.email, " +
                    "user.password, user.phone_number, " +
                    "role.id AS role_id, role.name AS role_name " +
                    "FROM account " +
                    "JOIN user ON user_id = user.id " +
                    "JOIN role ON role_id = role.id " +
                    "JOIN account_type AS type ON type_id = type.id " +
                    "JOIN debit_account_details AS dad ON account.id = dad.id " +
                    "JOIN status ON account.status_id = status.id " +
                    "WHERE type_id = (select id " +
                    "from account_type where name like 'DEBIT') ";

    private final static String AND_ACCOUNT_NUMBER =
            "and account.id = ? ";

    private final static String AND_USER =
            "and account.user_id = ? ";

    private final static String AND_STATUS =
            "and account.status_id = ? ";

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
            "INSERT INTO debit_account_details " +
                    "(id, balance, last_operation, " +
                    "min_balance, annual_rate)" +
                    "VALUES(?, ?, ?, ?, ?) ";

    private final static String UPDATE =
            "UPDATE debit_account_details SET " +
                    "balance = ?, last_operation = ?, " +
                    "min_balance = ? ";

    private final static String UPDATE_STATUS =
            "UPDATE account SET " +
                    "status_id = ? ";

    private final static String UPDATE_MIN_BALANCE =
            "UPDATE debit_account_details SET " +
                    "min_balance = ? ";

    private final static String INCREASE_BALANCE =
            "UPDATE debit_account_details SET " +
                    "balance = balance + ? ";

    private final static String DECREASE_BALANCE =
            "UPDATE debit_account_details SET " +
                    "balance = balance - ? ";

    private final static String DELETE =
            "DELETE details, account FROM " +
                    "debit_account_details AS details " +
                    "JOIN account  USING(id) ";


    private final DefaultDaoImpl<DebitAccount> defaultDao;


    public MySqlDebitAccountDao(Connection connection) {
        this(connection, new DebitAccountDtoConverter());
    }

    public MySqlDebitAccountDao(Connection connection,
                                DtoConverter<DebitAccount> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlDebitAccountDao(DefaultDaoImpl<DebitAccount> defaultDao) {
        this.defaultDao = defaultDao;
    }

    @Override
    public Optional<DebitAccount> findOne(Long accountNumber) {
        return defaultDao.findOne(
                SELECT_ALL + AND_ACCOUNT_NUMBER,
                accountNumber
        );
    }

    @Override
    public List<DebitAccount> findAll() {
        return defaultDao.findAll(
                SELECT_ALL
        );
    }

    @Override
    public DebitAccount insert(DebitAccount account) {
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
    public void update(DebitAccount account) {
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
    public List<DebitAccount> findByUser(User user) {
       Objects.requireNonNull(user);

        return defaultDao.findAll(
                SELECT_ALL + AND_USER,
                user.getId()
        );
    }

    @Override
    public List<DebitAccount> findByStatus(Status status) {
        return defaultDao.findAll(
                SELECT_ALL + AND_STATUS,
                status.getId()
        );
    }

    @Override
    public void increaseBalance(DebitAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                INCREASE_BALANCE + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber()
        );
    }

    @Override
    public void decreaseBalance(DebitAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                DECREASE_BALANCE + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber()
        );
    }

    @Override
    public void updateAccountStatus(DebitAccount account, Status status) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE_STATUS + WHERE_ACCOUNT_NUMBER,
                status.getId(),
                account.getAccountNumber()
        );
    }

    @Override
    public void updateMinBalance(DebitAccount account, BigDecimal minBalance) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE_MIN_BALANCE + WHERE_ACCOUNT_NUMBER,
                minBalance,
                account.getAccountNumber()
        );
    }

    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        DebitAccountDao mySqlDebitAccountDao;
        try {
            System.out.println("Find all:");
            mySqlDebitAccountDao = new MySqlDebitAccountDao(dataSource.getConnection());
            ((MySqlDebitAccountDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            int random = (int) (Math.random() * 100);

            System.out.println("Find one:");
            System.out.println(mySqlDebitAccountDao.findOne(1L));

            System.out.println("find dy user:");
            User user = User.newBuilder().setFirstName("first" + random).
                    setId(3).
                    setLastName("last" + random).
                    setEmail("test" + random + "@com").
                    setPassword("123").
                    setPhoneNumber("+123").
                    setRole(new Role(Role.USER_ROLE_ID, "USER")).
                    build();
            System.out.println(mySqlDebitAccountDao.findByUser(user));

            System.out.println("Insert:");
            DebitAccount debitAccount = (DebitAccount) mySqlDebitAccountDao.insert(
                    DebitAccount.newBuilder().
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
            ((MySqlDebitAccountDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("update:");
            debitAccount.setBalance(BigDecimal.valueOf(12345));
            mySqlDebitAccountDao.update(debitAccount);

            System.out.println("Find all:");
            ((MySqlDebitAccountDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("Increase:");
            mySqlDebitAccountDao.increaseBalance(debitAccount, BigDecimal.valueOf(100));

            System.out.println("Find all:");
            ((MySqlDebitAccountDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("decrease:");
            mySqlDebitAccountDao.decreaseBalance(debitAccount, BigDecimal.valueOf(2000));

            System.out.println("Find all:");
            ((MySqlDebitAccountDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("update status:");
            mySqlDebitAccountDao.updateAccountStatus(debitAccount,new Status(4,"PENDING"));

            System.out.println("Find all:");
            ((MySqlDebitAccountDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("update min balance:");
            mySqlDebitAccountDao.updateMinBalance(debitAccount,BigDecimal.ZERO);

            System.out.println("Find all:");
            ((MySqlDebitAccountDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("delete:");
            mySqlDebitAccountDao.delete(debitAccount.getAccountNumber());

            System.out.println("Find all:");
            ((MySqlDebitAccountDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void printAccount(List<DebitAccount> list){
        for (DebitAccount debitAccount : list) {
            System.out.println("Account Number: "+debitAccount.getAccountNumber()+";");
            System.out.println("Account Holder: "+debitAccount.getAccountHolder()+"; "+debitAccount.getAccountHolder().getRole()+"; ");
            System.out.println("Account type: "+debitAccount.getAccountType()+";");
            System.out.println("Balance: "+debitAccount.getBalance()+";");
            System.out.println("Annual Rate: "+debitAccount.getAnnualRate()+";");
            System.out.println("Last operation: "+debitAccount.getLastOperationDate()+";");
            System.out.println("Min Balance: "+debitAccount.getMinBalance()+";");
            System.out.println("Status: "+debitAccount.getStatus()+";");
            System.out.println();
        }
    }
}
