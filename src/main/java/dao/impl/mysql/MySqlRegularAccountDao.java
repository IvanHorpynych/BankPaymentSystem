
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
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MySqlRegularAccountDao implements RegularAccountDao {
    private final static String SELECT_ALL =
            "SELECT * FROM regular_details ";

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
                SELECT_ALL + WHERE_ACCOUNT_NUMBER,
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
                SELECT_ALL + WHERE_USER,
                user.getId()
        );
    }

    @Override
    public List<RegularAccount> findByStatus(Status status) {
        return defaultDao.findAll(
                SELECT_ALL + WHERE_STATUS,
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
            System.out.println("Account : "+debitAccount+";");
            System.out.println("Balance: "+debitAccount.getBalance()+";");
            System.out.println();
        }
    }

}
