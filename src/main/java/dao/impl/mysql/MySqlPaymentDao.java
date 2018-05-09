package dao.impl.mysql;

import dao.abstraction.PaymentDao;
import dao.impl.mysql.converter.DtoConverter;
import dao.impl.mysql.converter.PaymentDtoConverter;
import dao.util.time.TimeConverter;
import entity.Account;
import entity.Payment;
import entity.User;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class MySqlPaymentDao implements PaymentDao {
    private final static String SELECT_ALL =
            "SELECT payments.payment_id, payments.amount, payments.account_from, " +
                    "payments.account_to, payments.date, " +
                    "acc1.account_number, acc1.account_holder, " +
                    "acc1.balance, acc1.status, " +
                    "acc2.account_number, acc2.account_holder, " +
                    "acc2.balance, acc2.status, " +
                    "user1.user_id, user1.user_type, user1.first_name, " +
                    "user1.last_name, user1.email, " +
                    "user1.passhash, user1.phone_number, " +
                    "user2.user_id, user2.user_type, user2.first_name, " +
                    "user2.last_name, user2.email, " +
                    "user2.passhash, user2.phone_number, " +
                    "role1.role_id, role1.role_name, " +
                    "role2.role_id, role2.role_name " +
                    "FROM payments " +
                    "JOIN accounts AS acc1 ON " +
                    "payments.account_from = acc1.account_number " +
                    "JOIN accounts AS acc2 ON " +
                    "payments.account_to = acc2.account_number " +
                    "JOIN users AS user1 ON " +
                    "acc1.account_holder = user1.user_id " +
                    "JOIN users AS user2 ON " +
                    "acc2.account_holder = user2.user_id " +
                    "JOIN roles AS role1 ON " +
                    "user1.user_type = role1.role_id " +
                    "JOIN roles AS role2 ON " +
                    "user2.user_type = role2.role_id " ;

    private final static String WHERE_ID =
            "WHERE payments.payment_id = ? ";

    private final static String WHERE_ACCOUNT =
            "WHERE payments.account_from OR " +
                    "payments.account_to = ? ";

    private final static String WHERE_USER =
            "WHERE payments.account_from IN " +
                    "(SELECT accounts.account_number FROM accounts " +
                    "WHERE accounts.account_holder = ?) " +
                    "OR payments.account_to IN " +
                    "(SELECT accounts.account_number FROM accounts " +
                    "WHERE accounts.account_holder = ?)" ;

    private final static String INSERT =
            "INSERT INTO payments(" +
                    "payments.amount, payments.account_from, " +
                    "payments.account_to, payments.date) " +
                    "VALUES (?, ?, ?, ?) ";

    private final static String UPDATE =
            "UPDATE payments SET " +
                    "payments.amount = ?, payments.account_from = ?, " +
                    "payments.account_to = ?, payments.date = ? ";

    private final static String DELETE =
            "DELETE FROM payments ";


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
    public Optional<Payment> findOne(Integer id) {
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
                obj.getAccountFrom().getAccountNumber(),
                obj.getAccountTo().getAccountNumber(),
                TimeConverter.toTimestamp(obj.getDate()),
                obj.getId()
        );
    }

    @Override
    public void delete(Integer id) {
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
                user.getId(),
                user.getId()
        );
    }
}
