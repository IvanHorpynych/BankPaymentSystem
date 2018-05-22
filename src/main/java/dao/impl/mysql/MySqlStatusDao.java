
package dao.impl.mysql;

import dao.abstraction.StatusDao;
import dao.datasource.PooledConnection;
import dao.impl.mysql.converter.DtoConverter;
import dao.impl.mysql.converter.StatusDtoConverter;
import entity.Status;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Created by JohnUkraine on 5/07/2018.
 */

public class MySqlStatusDao implements StatusDao {
    private final static String SELECT_ALL =
            "SELECT id AS status_id, name AS status_name " +
                    "FROM status ";

    private final static String INSERT =
            "INSERT INTO status (name) " +
                    "VALUES(?);";

    private final static String UPDATE =
            "UPDATE status SET name = ? ";

    private final static String DELETE =
            "DELETE FROM status ";

    private final static String WHERE_ID =
            "WHERE id = ? ";

    private final static String WHERE_NAME =
            "WHERE name = ? ";


    private final DefaultDaoImpl<Status> defaultDao;

    public MySqlStatusDao(Connection connection) {
        this(connection, new StatusDtoConverter());
    }

    public MySqlStatusDao(Connection connection,
                        DtoConverter<Status> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlStatusDao(DefaultDaoImpl<Status> defaultDao) {
        this.defaultDao = defaultDao;
    }

    @Override
    public Optional<Status> findOne(Integer id) {
        return defaultDao.findOne(SELECT_ALL + WHERE_ID, id);
    }

    @Override
    public List<Status> findAll() {
        return defaultDao.findAll(SELECT_ALL);
    }

    @Override
    public Status insert(Status status) {
        Objects.requireNonNull(status, "Status object must be not null");

        int id = (int) defaultDao.executeInsertWithGeneratedPrimaryKey(
                INSERT,
                status.getName()
        );

        status.setId(id);

        return status;
    }

    @Override
    public void update(Status status) {
        Objects.requireNonNull(status);

        defaultDao.executeUpdate(
                UPDATE + WHERE_ID,
                status.getName(),
                status.getId()
        );
    }

    @Override
    public void delete(Integer id) {
        defaultDao.executeUpdate(
                DELETE + WHERE_ID,
                id);
    }

    @Override
    public Optional<Status> findOneByName(String name) {
        return defaultDao.findOne(SELECT_ALL + WHERE_NAME, name);
    }

    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        StatusDao mySqlStatusDao;

        try {
            mySqlStatusDao = new MySqlStatusDao(dataSource.getConnection());
            ((MySqlStatusDao) mySqlStatusDao).printAll(mySqlStatusDao.findAll());
            System.out.println();

            System.out.println("Find one with id 1:");
            System.out.println(mySqlStatusDao.findOne(1));

            System.out.println("Find one by name MANAGER:");
            System.out.println(mySqlStatusDao.findOneByName("MANAGER"));

            System.out.println("Find one by name BLOCKED:");
            System.out.println(mySqlStatusDao.findOneByName("BLOCKED"));

            System.out.println("Insert test:");
            Status accountType = mySqlStatusDao.
                    insert(new Status(0, "TEST"));
            ((MySqlStatusDao) mySqlStatusDao).
                    printAll(mySqlStatusDao.findAll());

            System.out.println("Update:");
            accountType.setName("TEST@222");
            mySqlStatusDao.update(accountType);
            ((MySqlStatusDao) mySqlStatusDao).
                    printAll(mySqlStatusDao.findAll());

            System.out.println("Delete:");
            mySqlStatusDao.delete(accountType.getId());
            ((MySqlStatusDao) mySqlStatusDao).
                    printAll(mySqlStatusDao.findAll());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void printAll(List<Status> list) {
        System.out.println("Find all:");
        for (Status type : list) {
            System.out.println(type);
        }
    }
}

