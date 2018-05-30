package dao.impl.mysql;

import dao.abstraction.CreditRequestDao;
import dao.datasource.PooledConnection;
import dao.impl.mysql.converter.CreditRequestDtoConverter;
import dao.impl.mysql.converter.DtoConverter;
import entity.*;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class MySqlCreditRequestDao implements CreditRequestDao {
    private final static String SELECT_ALL =
            "SELECT * FROM credit_request_details ";

    private final static String WHERE_REQUEST_NUMBER =
            "WHERE id = ? ";

    private final static String WHERE_USER =
            "WHERE user_id = ? ";

    private final static String WHERE_STATUS =
            "WHERE status_id = ? ";

    private final static String INSERT =
            "INSERT INTO credit_request " +
                    "(user_id, interest_rate, status_id, " +
                    "validity_date, credit_limit) " +
                    "VALUES(?, ?, ?, ?, ?) ";

    private final static String UPDATE =
            "UPDATE credit_request SET " +
                    "interest_rate = ?, status_id = ?, " +
                    "validity_date = ?, credit_limit = ? ";

    private final static String UPDATE_STATUS =
            "UPDATE credit_request SET " +
                    "status_id = ? ";

    private final static String DELETE =
            "DELETE FROM credit_request ";


    private final DefaultDaoImpl<CreditRequest> defaultDao;


    public MySqlCreditRequestDao(Connection connection) {
        this(connection, new CreditRequestDtoConverter());
    }

    public MySqlCreditRequestDao(Connection connection,
                                 DtoConverter<CreditRequest> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlCreditRequestDao(DefaultDaoImpl<CreditRequest> defaultDao) {
        this.defaultDao = defaultDao;
    }

    @Override
    public Optional<CreditRequest> findOne(Long requestNumber) {
        return defaultDao.findOne(SELECT_ALL + WHERE_REQUEST_NUMBER, requestNumber);
    }

    @Override
    public List<CreditRequest> findAll() {
        return defaultDao.findAll(SELECT_ALL);
    }

    @Override
    public CreditRequest insert(CreditRequest obj) {
        Objects.requireNonNull(obj);

        long requestNumber = defaultDao.executeInsertWithGeneratedPrimaryKey(
                INSERT,
                obj.getAccountHolder().getId(),
                obj.getInterestRate(),
                obj.getStatus().getId(),
                obj.getValidityDate(),
                obj.getCreditLimit()
        );

        obj.setRequestNumber(requestNumber);

        return obj;
    }

    @Override
    public void update(CreditRequest obj) {
        Objects.requireNonNull(obj);

        defaultDao.executeUpdate(
                UPDATE + WHERE_REQUEST_NUMBER,
                obj.getInterestRate(),
                obj.getStatus().getId(),
                obj.getValidityDate(),
                obj.getCreditLimit(),
                obj.getRequestNumber()
        );
    }

    @Override
    public void delete(Long requestNumber) {
        defaultDao.executeUpdate(
                DELETE + WHERE_REQUEST_NUMBER,
                requestNumber
        );
    }

    @Override
    public List<CreditRequest> findByUser(User user) {
        Objects.requireNonNull(user);

        return defaultDao.findAll(
                SELECT_ALL + WHERE_USER,
                user.getId()
        );
    }

    @Override
    public List<CreditRequest> findByStatus(long statusId) {

        return defaultDao.findAll(
                SELECT_ALL + WHERE_STATUS,
                statusId
        );
    }

    @Override
    public void updateRequestStatus(CreditRequest request, int statusId) {
        Objects.requireNonNull(request);

        defaultDao.executeUpdate(
                UPDATE_STATUS + WHERE_REQUEST_NUMBER,
                statusId,
                request.getRequestNumber()
        );
    }

    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        CreditRequestDao mySqlCreditRequestDao;
        try {
            System.out.println("Find all:");
            mySqlCreditRequestDao = new MySqlCreditRequestDao(dataSource.getConnection());
            for (CreditRequest temp : mySqlCreditRequestDao.findAll()) {
                System.out.println(temp);
            }

            System.out.println("Find one:");
            System.out.println(mySqlCreditRequestDao.findOne(1L));

            System.out.println("Find empty:");
            System.out.println(mySqlCreditRequestDao.findOne(4L));

            int random = (int) (Math.random() * 100);
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
            System.out.println(mySqlCreditRequestDao.findByUser(user));

            System.out.println("Insert:");
            CreditRequest creditRequest = mySqlCreditRequestDao.insert(
                    CreditRequest.newBuilder().
                            addAccountHolder(user).
                            addInterestRate(41.12f).
                            addStatus(new Status(8, "REGECT")).
                            addValidityDate(new Date()).
                            addCreditLimit(BigDecimal.TEN).
                            build()
            );

            System.out.println("Find all:");
            for (CreditRequest temp : mySqlCreditRequestDao.findAll()) {
                System.out.println(temp);
            }

            System.out.println("update:");

            creditRequest.setStatus(new Status(4, "PENDING"));
            creditRequest.setInterestRate(12);
            mySqlCreditRequestDao.update(creditRequest);

            System.out.println("Find one:");
            System.out.println(mySqlCreditRequestDao.findOne(creditRequest.getRequestNumber()));

            System.out.println("update status:");
            mySqlCreditRequestDao.updateRequestStatus(creditRequest,1);

            System.out.println("Find one:");
            System.out.println(mySqlCreditRequestDao.findOne(creditRequest.getRequestNumber()));

            System.out.println("delete:");
            mySqlCreditRequestDao.delete(creditRequest.getRequestNumber());

            System.out.println("Find all:");
            for (CreditRequest temp : mySqlCreditRequestDao.findAll()) {
                System.out.println(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
