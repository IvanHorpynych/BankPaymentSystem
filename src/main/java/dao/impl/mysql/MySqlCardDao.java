
package dao.impl.mysql;

import dao.abstraction.CardDao;
import dao.connectionsource.PooledConnection;
import dao.impl.mysql.converter.CardDtoConverter;
import dao.impl.mysql.converter.DtoConverter;
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

public class MySqlCardDao implements CardDao {
    private final static String SELECT_ALL =
            "SELECT * FROM card_details ";

    private final static String WHERE_CARD_NUMBER =
            "WHERE card_number = ? ";

    private final static String WHERE_ACCOUNT =
            "WHERE id = ? ";

    private final static String WHERE_USER =
            "WHERE user_id = ? ";

    private final static String AND_STATUS =
            " and status_id = ?";

    private final static String AND_TYPE =
            " and type_id = ?";

    private final static String INSERT =
            "INSERT INTO card" +
                    "(account_id, pin, cvv, expire_date, type)" +
                    "VALUES (?, ?, ?, ?, ?) ";

    private final static String UPDATE =
            "UPDATE card SET " +
                    "pin = ?, cvv = ?, expire_date = ?, type = ? ";

    private final static String DELETE =
            "DELETE FROM card ";

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

        obj.setCardNumber(defaultDao.executeInsertWithGeneratedPrimaryKey(
                INSERT,
                obj.getAccount().getAccountNumber(),
                obj.getPin(),
                obj.getCvv(),
                TimeConverter.toTimestamp(obj.getExpireDate()),
                obj.getType().toString()
        ));

        return obj;
    }

    @Override
    public void update(Card obj) {
        Objects.requireNonNull(obj);

        defaultDao.executeUpdate(
                UPDATE + WHERE_CARD_NUMBER,
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
        return defaultDao.findAll(SELECT_ALL + WHERE_USER,
                user.getId());
    }

    @Override
    public List<Card> findByAccount(Account account) {
        return defaultDao.findAll(SELECT_ALL + WHERE_ACCOUNT,
                account.getAccountNumber());
    }

    @Override
    public List<Card> findByUserAndType(User user, AccountType accountType) {
        return defaultDao.
                findAll(SELECT_ALL + WHERE_USER + AND_TYPE,
                        user.getId(), accountType.getId());
    }

    @Override
    public List<Card> findByUserAndStatus(User user, Status status) {
        return defaultDao.
                findAll(SELECT_ALL + WHERE_USER + AND_STATUS,
                        user.getId(), status.getId());
    }

    @Override
    public List<Card> findByUserAndTypeAndStatus(User user, AccountType accountType,
                                                 Status status) {
        return defaultDao.findAll(SELECT_ALL + WHERE_USER + AND_TYPE + AND_STATUS,
                user.getId(), accountType.getId(), status.getId()
        );
    }


    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        CardDao mySqlCardDao;
        User user = User.newBuilder().setFirstName("first").
                setId(3).
                setLastName("last").
                setEmail("test@com").
                setPassword("123").
                setPhoneNumber("+123").
                setRole(new Role(Role.USER_ROLE_ID, "USER")).
                build();

        CreditAccount creditAccount = CreditAccount.newBuilder().
                setAccountNumber(2).
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
            mySqlCardDao = new MySqlCardDao(dataSource.getConnection());
            ((MySqlCardDao) mySqlCardDao).printCard(mySqlCardDao.findAll());

            int random = (int) (Math.random() * 100);

            System.out.println("Find one:");
            System.out.println(mySqlCardDao.findOne(2222222222222222L));

            System.out.println("find dy user:");
            System.out.println(mySqlCardDao.findByUser(user));

            System.out.println("Find by account");
            for (Card card : mySqlCardDao.findByAccount(creditAccount)) {
                System.out.println(card);
            }

            System.out.println("Insert:");
            Card card = ((MySqlCardDao) mySqlCardDao).insert(
                    Card.newBuilder().
                            setAccount(creditAccount).
                            setPin(1111).
                            setCvv(444).
                            setExpireDate(new Date()).
                            setType(Card.CardType.MASTERCARD).
                            build()
            );
            List<Card> temp = new ArrayList<>();
            card = mySqlCardDao.findOne(card.getCardNumber()).get();
            temp.add(card);
            System.out.println("Find one:");
            ((MySqlCardDao) mySqlCardDao).printCard(temp);

            System.out.println("Find by account");
            for (Card temp1 : mySqlCardDao.findByAccount(creditAccount)) {
                System.out.println(temp1);
            }

            System.out.println("update:");
            card.setCvv(333);
            card.setType(Card.CardType.VISA);
            mySqlCardDao.update(card);

            temp.clear();
            card = mySqlCardDao.findOne(card.getCardNumber()).get();
            temp.add(card);
            System.out.println("Find one:");
            ((MySqlCardDao) mySqlCardDao).printCard(temp);

            System.out.println("Find by user and status:");
            ((MySqlCardDao) mySqlCardDao).printCard(mySqlCardDao.findByUserAndStatus(user, new Status(1,"ACTIVE")));
            System.out.println("Find by user and type:");
            ((MySqlCardDao) mySqlCardDao).printCard(mySqlCardDao.findByUserAndType(user, new AccountType(16,"REGULAR")));
            System.out.println("Find by user and status and type:");
            ((MySqlCardDao) mySqlCardDao).printCard(mySqlCardDao.findByUserAndTypeAndStatus(user,new AccountType(8,"DEBIT"), new Status(1,"ACTIVE")));
            System.out.println("Delete:");
            mySqlCardDao.delete(card.getCardNumber());
            ((MySqlCardDao) mySqlCardDao).printCard(mySqlCardDao.findAll());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void printCard(List<Card> list){
        for (Card card : list) {
            System.out.println("Card: "+card);
            System.out.println();
        }
    }
}
