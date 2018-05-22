
package dao.impl.mysql;

import dao.abstraction.AccountTypeDao;
import dao.datasource.PooledConnection;
import dao.impl.mysql.converter.AccountTypeDtoConverter;
import dao.impl.mysql.converter.DtoConverter;
import entity.AccountType;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Created by JohnUkraine on 5/07/2018.
 */

public class MySqlAccountTypeDao implements AccountTypeDao {
    private final static String SELECT_ALL =
            "SELECT id AS type_id, name AS type_name " +
                    "FROM account_type ";

    private final static String INSERT =
            "INSERT INTO account_type (name) " +
                    "VALUES(?);";

    private final static String UPDATE =
            "UPDATE account_type SET name = ? ";

    private final static String DELETE =
            "DELETE FROM account_type ";

    private final static String WHERE_ID =
            "WHERE id = ? ";

    private final static String WHERE_NAME =
            "WHERE name = ? ";


    private final DefaultDaoImpl<AccountType> defaultDao;

    public MySqlAccountTypeDao(Connection connection) {
        this(connection, new AccountTypeDtoConverter());
    }

    public MySqlAccountTypeDao(Connection connection,
                               DtoConverter<AccountType> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlAccountTypeDao(DefaultDaoImpl<AccountType> defaultDao) {
        this.defaultDao = defaultDao;
    }

    @Override
    public Optional<AccountType> findOne(Integer id) {
        return defaultDao.findOne(SELECT_ALL + WHERE_ID, id);
    }

    @Override
    public List<AccountType> findAll() {
        return defaultDao.findAll(SELECT_ALL);
    }

    @Override
    public AccountType insert(AccountType type) {
        Objects.requireNonNull(type, "AccountType object must be not null");

        int id = (int) defaultDao.executeInsertWithGeneratedPrimaryKey(
                INSERT,
                type.getName()
        );

        type.setId(id);

        return type;
    }

    @Override
    public void update(AccountType type) {
        Objects.requireNonNull(type);

        defaultDao.executeUpdate(
                UPDATE + WHERE_ID,
                type.getName(),
                type.getId()
        );
    }

    @Override
    public void delete(Integer id) {
        defaultDao.executeUpdate(
                DELETE + WHERE_ID,
                id);
    }

    @Override
    public Optional<AccountType> findOneByName(String name) {
        return defaultDao.findOne(SELECT_ALL + WHERE_NAME, name);
    }

    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        AccountTypeDao mySqlAccountTypeDao;

        try {
            mySqlAccountTypeDao = new MySqlAccountTypeDao(dataSource.getConnection());
            ((MySqlAccountTypeDao) mySqlAccountTypeDao).
                    printAll(mySqlAccountTypeDao.findAll());
            System.out.println();

            System.out.println("Find one with id 8:");
            System.out.println(mySqlAccountTypeDao.findOne(8));

            System.out.println("Find one by name REGULAR:");
            System.out.println(mySqlAccountTypeDao.findOneByName("REGULAR"));

            System.out.println("Insert test:");
            AccountType accountType = mySqlAccountTypeDao.
                    insert(new AccountType(0, "TEST"));
            ((MySqlAccountTypeDao) mySqlAccountTypeDao).
                    printAll(mySqlAccountTypeDao.findAll());

            System.out.println("Update:");
            accountType.setName("TEST@222");
            mySqlAccountTypeDao.update(accountType);
            ((MySqlAccountTypeDao) mySqlAccountTypeDao).
                    printAll(mySqlAccountTypeDao.findAll());

            System.out.println("Delete:");
            mySqlAccountTypeDao.delete(accountType.getId());
            ((MySqlAccountTypeDao) mySqlAccountTypeDao).
                    printAll(mySqlAccountTypeDao.findAll());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void printAll(List<AccountType> list) {
        System.out.println("Find all:");
        for (AccountType type : list) {
            System.out.println(type);
        }
    }
}

