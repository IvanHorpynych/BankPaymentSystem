package dao.impl.mysql;

import dao.abstraction.CardDao;
import dao.abstraction.PaymentDao;
import dao.connectionsource.PooledConnection;
import dao.impl.mysql.converter.DtoConverter;
import dao.impl.mysql.converter.PaymentDtoConverter;
import dao.util.time.TimeConverter;
import entity.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class MySqlPaymentDao implements PaymentDao {
    private final static String SELECT_ALL =
            "SELECT * FROM payment_details " ;

    private final static String WHERE_ID =
            "WHERE id = ? ";

    private final static String WHERE_ACCOUNT =
            "WHERE account_from OR " +
                    "account_to = ? ";

    private final static String WHERE_USER =
            "WHERE acc1_user_id OR " +
                    "acc2_user_id = ?" ;

    private final static String INSERT =
            "INSERT INTO payment (" +
                    "amount, account_from, " +
                    "account_to, operation_date) " +
                    "VALUES (?, ?, ?, ?) ";

    private final static String UPDATE =
            "UPDATE payment SET " +
                    "amount = ?, operation_date = ? ";

    private final static String DELETE =
            "DELETE FROM payment ";


    private final DefaultDaoImpl<Payment> defaultDao;


    public MySqlPaymentDao(Connection connection) {
        this(connection, new PaymentDtoConverter());
    }

    public MySqlPaymentDao(Connection connection,
                           DtoConverter<Payment> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlPaymentDao(DefaultDaoImpl<Payment> defaultDao) {
        this.defaultDao = defaultDao;
    }



    @Override
    public Optional<Payment> findOne(Long id) {
        return defaultDao.findOne(
                SELECT_ALL + WHERE_ID,
                id
        );
    }

    @Override
    public List<Payment> findAll() {
        return defaultDao.findAll(
                SELECT_ALL
        );
    }

    @Override
    public Payment insert(Payment obj) {
        Objects.requireNonNull(obj);

        int id = (int)defaultDao.
                executeInsertWithGeneratedPrimaryKey(
                INSERT,
                        obj.getAmount(),
                        obj.getAccountFrom().getAccountNumber(),
                        obj.getAccountTo().getAccountNumber(),
                        TimeConverter.toTimestamp(obj.getDate())
        );

        obj.setId(id);

        return obj;
    }

    @Override
    public void update(Payment obj) {
        Objects.requireNonNull(obj);

        defaultDao.executeUpdate(
                UPDATE + WHERE_ID,
                obj.getAmount(),
                TimeConverter.toTimestamp(obj.getDate()),
                obj.getId()
        );
    }

    @Override
    public void delete(Long id) {
        defaultDao.executeUpdate(
                DELETE + WHERE_ID,
                id
        );
    }

    @Override
    public List<Payment> findByAccount(Account account) {
        Objects.requireNonNull(account);

        return defaultDao.findAll(
                SELECT_ALL + WHERE_ACCOUNT,
                account.getAccountNumber()
        );
    }

    @Override
    public List<Payment> findByUser(User user) {
        Objects.requireNonNull(user);

        return defaultDao.findAll(
                SELECT_ALL + WHERE_USER,
                user.getId()
        );
    }

    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        PaymentDao mySqlPaymentDao;
        User user = User.newBuilder().setFirstName("first").
                setId(3).
                setLastName("last").
                setEmail("test@com").
                setPassword("123").
                setPhoneNumber("+123").
                setRole(new Role(Role.USER_ROLE_ID, "USER")).
                build();

        CreditAccount creditAccount1 = CreditAccount.newBuilder().
                setAccountNumber(3).
                setAccountHolder(user).
                setAccountType(new AccountType(4, "CREDIT")).
                setBalance(BigDecimal.ONE).
                setCreditLimit(BigDecimal.TEN).
                setInterestRate(2L).
                setLastOperationDate(new Date()).
                setAccruedInterest(BigDecimal.ZERO).
                setValidityDate(new Date()).
                setStatus(new Status(1, "ACTIVE")).
                build();

        CreditAccount creditAccount2 = CreditAccount.newBuilder().
                setAccountNumber(1).
                setAccountHolder(user).
                setAccountType(new AccountType(4, "CREDIT")).
                setBalance(BigDecimal.ONE).
                setCreditLimit(BigDecimal.TEN).
                setInterestRate(2L).
                setLastOperationDate(new Date()).
                setAccruedInterest(BigDecimal.ZERO).
                setValidityDate(new Date()).
                setStatus(new Status(1, "ACTIVE")).
                build();

        try {
            System.out.println("Find all:");
            mySqlPaymentDao = new MySqlPaymentDao(dataSource.getConnection());

            for (Payment payment : mySqlPaymentDao.findAll()) {
                System.out.println(payment);
            }

            System.out.println("Find one:");
            System.out.println(mySqlPaymentDao.findOne(2L));

            System.out.println("find dy user:");
            System.out.println(mySqlPaymentDao.findByUser(user));

            System.out.println("Find by account");
            for (Payment payment : mySqlPaymentDao.findByAccount(creditAccount1)) {
                System.out.println(payment);
            }

            System.out.println("Insert:");
            Payment payment =  mySqlPaymentDao.insert(
                    Payment.newBuilder().
                            setAccountFrom(creditAccount1).
                            setAccountTo(creditAccount2).
                            setAmount(BigDecimal.TEN).
                            setDate(new Date()).
                            build()
            );
            System.out.println("Find one:");
            System.out.println(mySqlPaymentDao.findOne(payment.getId()));

            for (Payment temp : mySqlPaymentDao.findAll()) {
                System.out.println(temp);
            }
            System.out.println("update:");

            payment.setAmount(BigDecimal.ONE);
            mySqlPaymentDao.update(payment);

            System.out.println("Find one:");
            System.out.println(mySqlPaymentDao.findOne(payment.getId()));

            System.out.println("Delete");
            mySqlPaymentDao.delete(payment.getId());

            for (Payment temp : mySqlPaymentDao.findAll()) {
                System.out.println(temp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
