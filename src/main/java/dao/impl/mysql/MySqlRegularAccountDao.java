
package dao.impl.mysql;

import dao.abstraction.DebitAccountDao;
import dao.abstraction.RegularAccountDao;
import dao.connectionsource.PooledConnection;
import dao.impl.mysql.converter.CreditAccountDtoConverter;
import dao.impl.mysql.converter.DebitAccountDtoConverter;
import dao.impl.mysql.converter.DtoConverter;
import dao.impl.mysql.converter.RegularAccountDtoConverter;
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

public class MySqlRegularAccountDao implements RegularAccountDao {
    private final static String SELECT_ALL =
            "SELECT\n" +
                    "rad.id, rad.balance, " +
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
                    "JOIN regular_account_details AS rad ON account.id = rad.id " +
                    "JOIN status ON account.status_id = status.id " +
                    "WHERE type_id = (select id " +
                    "from account_type where name like 'REGULAR') ";

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
            "INSERT INTO regular_account_details " +
                    "(id, balance)" +
                    "VALUES(?, ?) ";

    private final static String UPDATE =
            "UPDATE regular_account_details SET " +
                    "balance = ? ";

    private final static String UPDATE_STATUS =
            "UPDATE account SET " +
                    "status_id = ? ";

    private final static String INCREASE_BALANCE =
            "UPDATE regular_account_details SET " +
                    "balance = balance + ? ";

    private final static String DECREASE_BALANCE =
            "UPDATE regular_account_details SET " +
                    "balance = balance - ? ";

    private final static String DELETE =
            "DELETE details, account FROM " +
                    "regular_account_details AS details " +
                    "JOIN account  USING(id) ";


    private final DefaultDaoImpl<RegularAccount> defaultDao;


    public MySqlRegularAccountDao(Connection connection) {
        this(connection, new RegularAccountDtoConverter());
    }

    public MySqlRegularAccountDao(Connection connection,
                                DtoConverter<RegularAccount> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlRegularAccountDao(DefaultDaoImpl<RegularAccount> defaultDao) {
        this.defaultDao = defaultDao;
    }

    @Override
    public Optional<RegularAccount> findOne(Long accountNumber) {
        return defaultDao.findOne(
                SELECT_ALL + AND_ACCOUNT_NUMBER,
                accountNumber
        );
    }

    @Override
    public List<RegularAccount> findAll() {
        return defaultDao.findAll(
                SELECT_ALL
        );
    }

    @Override
    public RegularAccount insert(RegularAccount account) {
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
                account.getBalance()
        );
        return account;
    }

    @Override
    public void update(RegularAccount account) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE + WHERE_ACCOUNT_NUMBER,
                account.getBalance(),
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
    public List<RegularAccount> findByUser(User user) {
        Objects.requireNonNull(user);

        return defaultDao.findAll(
                SELECT_ALL + AND_USER,
                user.getId()
        );
    }

    @Override
    public List<RegularAccount> findByStatus(Status status) {
        return defaultDao.findAll(
                SELECT_ALL + AND_STATUS,
                status.getId()
        );
    }

    @Override
    public void increaseBalance(RegularAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                INCREASE_BALANCE + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber()
        );
    }

    @Override
    public void decreaseBalance(RegularAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                DECREASE_BALANCE + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber()
        );
    }

    @Override
    public void updateAccountStatus(RegularAccount account, Status status) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE_STATUS + WHERE_ACCOUNT_NUMBER,
                status.getId(),
                account.getAccountNumber()
        );
    }


    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        RegularAccountDao mySqlRegularAccountDao;
        try {
            System.out.println("Find all:");
            mySqlRegularAccountDao = new MySqlRegularAccountDao(dataSource.getConnection());
            ((MySqlRegularAccountDao) mySqlRegularAccountDao).printAccount(mySqlRegularAccountDao.findAll());

            int random = (int) (Math.random() * 100);

            System.out.println("Find one:");
            System.out.println(mySqlRegularAccountDao.findOne(3L));

            System.out.println("find dy user:");
            User user = User.newBuilder().setFirstName("first" + random).
                    setId(3).
                    setLastName("last" + random).
                    setEmail("test" + random + "@com").
                    setPassword("123").
                    setPhoneNumber("+123").
                    setRole(new Role(Role.USER_ROLE_ID, "USER")).
                    build();
            System.out.println(mySqlRegularAccountDao.findByUser(user));

            System.out.println("Insert:");
            RegularAccount debitAccount = (RegularAccount) mySqlRegularAccountDao.insert(
                    RegularAccount.newBuilder().
                            setAccountHolder(user).
                            setAccountType(new AccountType(16,"REGULAR")).
                            setBalance(BigDecimal.TEN).
                            setStatus(new Status(1,"ACTIVE")).
                            build()
            );

            System.out.println("Find all:");
            ((MySqlRegularAccountDao) mySqlRegularAccountDao).printAccount(mySqlRegularAccountDao.findAll());

            System.out.println("update:");
            debitAccount.setBalance(BigDecimal.valueOf(12345));
            mySqlRegularAccountDao.update(debitAccount);

            System.out.println("Find all:");
            ((MySqlRegularAccountDao) mySqlRegularAccountDao).printAccount(mySqlRegularAccountDao.findAll());

            System.out.println("Increase:");
            mySqlRegularAccountDao.increaseBalance(debitAccount, BigDecimal.valueOf(100));

            System.out.println("Find all:");
            ((MySqlRegularAccountDao) mySqlRegularAccountDao).printAccount(mySqlRegularAccountDao.findAll());

            System.out.println("decrease:");
            mySqlRegularAccountDao.decreaseBalance(debitAccount, BigDecimal.valueOf(2000));

            System.out.println("Find all:");
            ((MySqlRegularAccountDao) mySqlRegularAccountDao).printAccount(mySqlRegularAccountDao.findAll());

            System.out.println("update status:");
            mySqlRegularAccountDao.updateAccountStatus(debitAccount,new Status(4,"PENDING"));

            System.out.println("Find all:");
            ((MySqlRegularAccountDao) mySqlRegularAccountDao).printAccount(mySqlRegularAccountDao.findAll());

            System.out.println("delete:");
            mySqlRegularAccountDao.delete(debitAccount.getAccountNumber());

            System.out.println("Find all:");
            ((MySqlRegularAccountDao) mySqlRegularAccountDao).printAccount(mySqlRegularAccountDao.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void printAccount(List<RegularAccount> list){
        for (RegularAccount debitAccount : list) {
            System.out.println("Account Number: "+debitAccount.getAccountNumber()+";");
            System.out.println("Account Holder: "+debitAccount.getAccountHolder()+"; "+debitAccount.getAccountHolder().getRole()+"; ");
            System.out.println("Account type: "+debitAccount.getAccountType()+";");
            System.out.println("Balance: "+debitAccount.getBalance()+";");
            System.out.println("Status: "+debitAccount.getStatus()+";");
            System.out.println();
        }
    }

}
