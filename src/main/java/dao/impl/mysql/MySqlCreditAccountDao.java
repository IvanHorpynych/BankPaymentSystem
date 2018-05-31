package dao.impl.mysql;

import dao.abstraction.CreditAccountDao;
import dao.datasource.PooledConnection;
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

    private final static String WHERE_NOT_CLOSED =
            "WHERE status_id != " +
                    "(SELECT id FROM status where name = 'CLOSED') ";

    private final static String INSERT =
            "INSERT INTO account " +
                    "(user_id, type_id, status_id, balance) " +
                    "VALUES(?, ?, ?, ?) ";

    private final static String INSERT_DETAILS =
            "INSERT INTO credit_account_details " +
                    "(id, credit_limit, interest_rate, " +
                    "accrued_interest, " +
                    "validity_date) " +
                    "VALUES(?, ?, ?, ?, ?) ";

    private final static String UPDATE =
            "UPDATE credit_account_details SET " +
                    "accrued_interest = ?, validity_date = ? ";

    private final static String UPDATE_STATUS =
            "UPDATE account SET " +
                    "status_id = ? ";

    private final static String INCREASE_BALANCE =
            "UPDATE account SET " +
                    "balance = balance + ? ";

    private final static String DECREASE_BALANCE =
            "UPDATE account SET " +
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
                account.getStatus().getId(),
                account.getBalance()
        );

        account.setAccountNumber(accountNumber);

        defaultDao.executeUpdate(INSERT_DETAILS,
                account.getAccountNumber(),
                account.getCreditLimit(),
                account.getInterestRate(),
                account.getAccruedInterest(),
                TimeConverter.toTimestamp(account.getValidityDate())
        );

        return account;
    }

    @Override
    public void update(CreditAccount account) {
        /*Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE + WHERE_ACCOUNT_NUMBER,
                account.getAccruedInterest(),
                TimeConverter.toTimestamp(account.getValidityDate()),
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
    public List<CreditAccount> findByUser(User user) {
        Objects.requireNonNull(user);

        return defaultDao.findAll(
                SELECT_ALL + WHERE_USER,
                user.getId()
        );
    }

    @Override
    public List<CreditAccount> findAllNotClosed() {
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
    public void updateAccountStatus(CreditAccount account, int statusId) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                UPDATE_STATUS + WHERE_ACCOUNT_NUMBER,
                statusId,
                account.getAccountNumber()
        );
    }


    @Override
    public void increaseAccruedInterest(CreditAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                INCREASE_ACCRUED_INTEREST + WHERE_ACCOUNT_NUMBER,
                amount,
                account.getAccountNumber()
        );
    }

    @Override
    public void decreaseAccruedInterest(CreditAccount account, BigDecimal amount) {
        Objects.requireNonNull(account);

        defaultDao.executeUpdate(
                DECREASE_ACCRUED_INTEREST + WHERE_ACCOUNT_NUMBER,
                amount,
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
            User user = User.newBuilder().addFirstName("first" + random).
                    addId(3).
                    addLastName("last" + random).
                    addEmail("test" + random + "@com").
                    addPassword("123").
                    addPhoneNumber("+123").
                    addRole(new Role(Role.RoleIdentifier.
                            USER_ROLE.getId(), "USER")).
                    build();
            System.out.println(mySqlCreditAccountDao.findByUser(user));

            System.out.println("Insert:");
            CreditAccount creditAccount = (CreditAccount) mySqlCreditAccountDao.insert(
                    CreditAccount.newCreditBuilder().
                            addAccountHolder(user).
                            addAccountType(new AccountType(4,"CREDIT")).
                            addBalance(BigDecimal.ONE).
                            addCreditLimit(BigDecimal.TEN).
                            addInterestRate(2L).
                            addAccruedInterest(BigDecimal.ZERO).
                            addValidityDate(new Date()).
                            addStatus(new Status(1,"ACTIVE")).
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
            mySqlCreditAccountDao.updateAccountStatus(creditAccount,4);

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
            System.out.println("Accrued interest: "+creditAccount.getAccruedInterest()+";");
            System.out.println("Validity date: "+creditAccount.getValidityDate()+";");
            System.out.println();
        }
    }

}
