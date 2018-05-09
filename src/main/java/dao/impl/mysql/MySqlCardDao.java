/*
package dao.impl.mysql;

import dao.abstraction.CardDao;
import dao.connectionsource.PooledConnection;
import dao.exception.DaoException;
import dao.impl.mysql.converter.CardDtoConverter;
import dao.impl.mysql.converter.DtoConverter;
import dao.util.time.TimeConverter;
import entity.Account;
import entity.Card;
import entity.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

*/
/**
 * Created by JohnUkraine on 5/07/2018.
 *//*

public class MySqlCardDao implements CardDao {
    private final static String SELECT_ALL =
            "SELECT cards.card_number, cards.card_holder, cards.fk_account_number, " +
                    "cards.pin, cards.cvv, cards.expire_date, cards.type, " +
                    "accounts.account_number, accounts.account_holder, " +
                    "accounts.balance, accounts.status, " +
                    "users.user_id, users.user_type, users.first_name, " +
                    "users.last_name, users.email, users.passhash, " +
                    "users.phone_number, " +
                    "roles.role_id, roles.role_name " +
                    "FROM cards " +
                    "JOIN accounts ON cards.fk_account_number = " +
                    "accounts.account_number " +
                    "JOIN users ON cards.card_holder = users.user_id " +
                    "JOIN roles ON users.user_type = roles.role_id ";

    private final static String WHERE_CARD_NUMBER =
            "WHERE card_number = ? ";

    private final static String WHERE_USER =
            "WHERE cards.card_holder = ? ";

    private final static String WHERE_ACCOUNT =
            "WHERE cards.fk_account_number = ? ";

    private final static String INSERT =
            "INSERT INTO cards" +
                    "(cards.card_holder, " +
                    "cards.fk_account_number, cards.pin, " +
                    "cards.cvv, cards.expire_date, cards.type) " +
                    "VALUES (?, ?, ?, ?, ?, ?) ";

    private final static String UPDATE =
            "UPDATE cards SET " +
                    "cards.card_holder = ?, " +
                    "cards.fk_account_number = ?, cards.pin = ?, " +
                    "cards.cvv = ?, cards.expire_date = ?, cards.type = ? ";

    private final static String DELETE =
            "DELETE FROM cards ";

    private final DefaultDaoImpl<Card> defaultDao;


    public MySqlCardDao(Connection connection) {
        this(connection, new CardDtoConverter());
    }

    public MySqlCardDao(Connection connection,
                        DtoConverter<Card> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlCardDao(DefaultDaoImpl<Card> defaultDao) {
        this.defaultDao = defaultDao;
    }


    @Override
    public Optional<Card> findOne(Long cardNumber) {
        return defaultDao.findOne(
                SELECT_ALL + WHERE_CARD_NUMBER,
                cardNumber
        );
    }

    @Override
    public List<Card> findAll() {
        return defaultDao.findAll(
                SELECT_ALL
        );
    }

    @Override
    public Card insert(Card obj) {
        Objects.requireNonNull(obj);

        defaultDao.executeInsertWithGeneratedPrimaryKey(
                INSERT,
                obj.getCardHolder().getId(),
                obj.getAccount().getAccountNumber(),
                obj.getPin(),
                obj.getCvv(),
                TimeConverter.toTimestamp(obj.getExpireDate()),
                obj.getType().toString()
        );

        return obj;
    }

    @Override
    public void update(Card obj) {
        Objects.requireNonNull(obj);

        defaultDao.executeUpdate(
                UPDATE + WHERE_CARD_NUMBER,
                obj.getCardHolder().getId(),
                obj.getAccount().getAccountNumber(),
                obj.getPin(),
                obj.getCvv(),
                TimeConverter.toTimestamp(
                        obj.getExpireDate()),
                obj.getType().toString(),
                obj.getCardNumber()
        );
    }

    @Override
    public void delete(Long cardNumber) {
        defaultDao.executeUpdate(
                DELETE + WHERE_CARD_NUMBER,
                cardNumber
        );
    }

    @Override
    public List<Card> findByUser(User user) {
        Objects.requireNonNull(user);

        return defaultDao.findAll(
                SELECT_ALL + WHERE_USER,
                user.getId()
        );
    }

    @Override
    public List<Card> findByAccount(Account account) {
        Objects.requireNonNull(account);

        return defaultDao.findAll(
                SELECT_ALL + WHERE_ACCOUNT,
                account.getAccountNumber()
        );
    }

    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();

        try {
            dataSource.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}*/
