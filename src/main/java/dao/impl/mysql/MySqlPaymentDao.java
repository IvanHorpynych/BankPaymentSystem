package dao.impl.mysql;

import dao.abstraction.PaymentDao;
import dao.datasource.PooledConnection;
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
            "SELECT * FROM payment_details ";

    private final static String WHERE_ID =
            "WHERE id = ? ";

    private final static String WHERE_ACCOUNT =
            "WHERE (account_from  = ?) OR " +
                    "(account_to = ?)";

    private final static String WHERE_USER =
            "WHERE (acc1_user_id = ?) OR " +
                    "(acc2_user_id = ?)";

    private final static String WHERE_CARD_NUMBER =
            "WHERE card_number_from = ?";

    private final static String INSERT =
            "INSERT INTO payment (" +
                    "amount, account_from, card_number_from, " +
                    "account_to, operation_date) " +
                    "VALUES (?, ?, ?, ?, ?) ";

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

        int id = (int) defaultDao.
                executeInsertWithGeneratedPrimaryKey(
                        INSERT,
                        obj.getAmount(),
                        obj.getAccountFrom().getAccountNumber(),
                        (obj.getCardNumberFrom() == 0L ? null : obj.getCardNumberFrom()),
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
    public List<Payment> findByAccount(Long accountNumber) {
        Objects.requireNonNull(accountNumber);

        return defaultDao.findAll(
                SELECT_ALL + WHERE_ACCOUNT,
                accountNumber,
                accountNumber
        );
    }

    @Override
    public List<Payment> findByUser(User user) {
        Objects.requireNonNull(user);

        return defaultDao.findAll(
                SELECT_ALL + WHERE_USER,
                user.getId(),
                user.getId()
        );
    }

    @Override
    public List<Payment> findByCardNumber(long cardNumber) {
        return defaultDao.findAll(SELECT_ALL +
                WHERE_CARD_NUMBER, cardNumber);
    }

    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        PaymentDao mySqlPaymentDao;
        User user = User.newBuilder().addFirstName("first").
                addId(3).
                addLastName("last").
                addEmail("test@com").
                addPassword("123").
                addPhoneNumber("+123").
                addRole(new Role(Role.RoleIdentifier.
                        USER_ROLE.getId(), "USER")).
                build();

        CreditAccount creditAccount1 = CreditAccount.newCreditBuilder().
                addAccountNumber(3).
                addAccountHolder(user).
                addAccountType(new AccountType(4, "CREDIT")).
                addBalance(BigDecimal.ONE).
                addCreditLimit(BigDecimal.TEN).
                addInterestRate(2L).
                addAccruedInterest(BigDecimal.ZERO).
                addValidityDate(new Date()).
                addStatus(new Status(1, "ACTIVE")).
                build();

        CreditAccount creditAccount2 = CreditAccount.newCreditBuilder().
                addAccountNumber(1).
                addAccountHolder(user).
                addAccountType(new AccountType(4, "CREDIT")).
                addBalance(BigDecimal.ONE).
                addCreditLimit(BigDecimal.TEN).
                addInterestRate(2L).
                addAccruedInterest(BigDecimal.ZERO).
                addValidityDate(new Date()).
                addStatus(new Status(1, "ACTIVE")).
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
            for (Payment payment : mySqlPaymentDao.findByAccount(creditAccount1.getAccountNumber())) {
                System.out.println(payment);
            }

            System.out.println("Insert:");
            Payment payment = mySqlPaymentDao.insert(
                    Payment.newBuilder().
                            addAccountFrom(creditAccount1).
                            addAccountTo(creditAccount2).
                            addAmount(BigDecimal.TEN).
                            addDate(new Date()).
                            addCardNumberFrom(0L).
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
