
package dao.impl.mysql;

import dao.abstraction.AccountsDao;
import dao.datasource.PooledConnection;
import dao.impl.mysql.converter.DtoConverter;
import dao.impl.mysql.converter.AccountDtoConverter;
import entity.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class MySqlAccountsDao implements AccountsDao {


    private String SELECT_ALL;

    private final static String MAIN_QUERY =
            "SELECT * FROM account_details ";

    private final static String WHERE_ACCOUNT_NUMBER =
            "WHERE id = ? ";

    private final static String WHERE_USER =
            "WHERE user_id = ? ";

    private final static String WHERE_NOT_CLOSED =
            "WHERE status_id != " +
                    "(SELECT id FROM status where name = 'CLOSED') ";

    private final static String WHERE_TYPE =
            "WHERE type_id = ? ";

    private final static String INSERT =
            "INSERT INTO account " +
                    "(user_id, type_id, status_id, balance) " +
                    "VALUES(?, ?, ?, ?) ";


    private final static String UPDATE =
            "UPDATE account SET " +
                    "balance = ? ";

    private final static String UPDATE_STATUS =
            "UPDATE account SET " +
                    "status_id = ? ";

    private final static String INCREASE_BALANCE =
            "UPDATE account SET " +
                    "balance = balance + ? ";

    private final static String DECREASE_BALANCE =
            "UPDATE account SET " +
                    "balance = balance - ? ";

    private final static String DELETE =
            "DELETE FROM account ";


    private  DefaultDaoImpl<Account> defaultDao;


    public MySqlAccountsDao(Connection connection) {
        this(connection, new AccountDtoConverter(), MAIN_QUERY);
    }

    public MySqlAccountsDao(Connection connection, String query) {
        this(connection, new AccountDtoConverter(), query);
    }

    public MySqlAccountsDao(Connection connection,
                            DtoConverter<Account> converter, String query) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
        this.SELECT_ALL = query;
    }

    public MySqlAccountsDao(DefaultDaoImpl<Account> defaultDao) {
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
                account.getAccountType().getId(),
                account.getStatus().getId(),
                account.getBalance()
        );

        account.setAccountNumber(accountNumber);

        return account;
    }

    @Override
    public void update(Account account) {
        /*Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE + WHERE_ACCOUNT_NUMBER,
                account.getBalance(),
                account.getAccountNumber()
        );*/
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
    public List<Account> findAllNotClosed() {
        return defaultDao.findAll(
                SELECT_ALL + WHERE_NOT_CLOSED
        );
    }

    @Override
    public void increaseBalance(Account account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                INCREASE_BALANCE + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber()
        );
    }

    @Override
    public void decreaseBalance(Account account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                DECREASE_BALANCE + WHERE_ACCOUNT_NUMBER,
                amount, account.getAccountNumber()
        );
    }

    @Override
    public void updateAccountStatus(Account account, int statusId) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE_STATUS + WHERE_ACCOUNT_NUMBER,
                statusId,
                account.getAccountNumber()
        );
    }

    @Override
    public Optional<Account> findOneByType(int typeId) {
        return defaultDao.findOne(SELECT_ALL+WHERE_TYPE, typeId);
    }


    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        AccountsDao mySqlDebitAccountDao;
        try {
            System.out.println("Find all:");
            mySqlDebitAccountDao = new MySqlAccountsDao(dataSource.getConnection());
            ((MySqlAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            int random = (int) (Math.random() * 100);

            System.out.println("Find one:");
            System.out.println(mySqlDebitAccountDao.findOne(3L));

            System.out.println("find dy user:");
            User user = User.newBuilder().addFirstName("first" + random).
                    addId(3).
                    addLastName("last" + random).
                    addEmail("test" + random + "@com").
                    addPassword("123").
                    addPhoneNumber("+123").
                    addRole(new Role(Role.RoleIdentifier.
                            USER_ROLE.getId(), "USER")).
                    build();
            System.out.println(mySqlDebitAccountDao.findByUser(user));

            System.out.println("Insert:");
            Account debitAccount = (Account) mySqlDebitAccountDao.insert(
                    Account.newBuilder().
                            addAccountHolder(user).
                            addAccountType(new AccountType(16,"DEBIT")).
                            addBalance(BigDecimal.TEN).
                            addStatus(new Status(1,"ACTIVE")).
                            build()
            );

            System.out.println("Find all:");
            ((MySqlAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("update:");
            debitAccount.setBalance(BigDecimal.valueOf(12345));
            mySqlDebitAccountDao.update(debitAccount);

            System.out.println("Find all:");
            ((MySqlAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("Increase:");
            mySqlDebitAccountDao.increaseBalance(debitAccount, BigDecimal.valueOf(100));

            System.out.println("Find all:");
            ((MySqlAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("decrease:");
            mySqlDebitAccountDao.decreaseBalance(debitAccount, BigDecimal.valueOf(2000));

            System.out.println("Find all:");
            ((MySqlAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("update status:");
            mySqlDebitAccountDao.updateAccountStatus(debitAccount,4);

            System.out.println("Find all:");
            ((MySqlAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());

            System.out.println("delete:");
            mySqlDebitAccountDao.delete(debitAccount.getAccountNumber());

            System.out.println("Find all:");
            ((MySqlAccountsDao) mySqlDebitAccountDao).printAccount(mySqlDebitAccountDao.findAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    protected void printAccount(List<Account> list){
        for (Account debitAccount : list) {
            System.out.println("Account : "+debitAccount+";");
            System.out.println("Balance: "+debitAccount.getBalance()+";");
            System.out.println();
        }
    }

}
