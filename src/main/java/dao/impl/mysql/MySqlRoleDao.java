
package dao.impl.mysql;

import dao.abstraction.RoleDao;
import dao.datasource.PooledConnection;
import dao.impl.mysql.converter.DtoConverter;
import dao.impl.mysql.converter.RoleDtoConverter;
import entity.Role;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Created by JohnUkraine on 5/07/2018.
 */

public class MySqlRoleDao implements RoleDao {
    private final static String SELECT_ALL =
            "SELECT id AS role_id, name AS role_name " +
                    "FROM role ";

    private final static String INSERT =
            "INSERT INTO role (name) " +
                    "VALUES(?);";

    private final static String UPDATE =
            "UPDATE role SET name = ? ";

    private final static String DELETE =
            "DELETE FROM role ";

    private final static String WHERE_ID =
            "WHERE id = ? ";

    private final static String WHERE_NAME =
            "WHERE name = ? ";


    private final DefaultDaoImpl<Role> defaultDao;

    public MySqlRoleDao(Connection connection) {
        this(connection, new RoleDtoConverter());
    }

    public MySqlRoleDao(Connection connection,
                        DtoConverter<Role> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlRoleDao(DefaultDaoImpl<Role> defaultDao) {
        this.defaultDao = defaultDao;
    }

    @Override
    public Optional<Role> findOne(Integer id) {
        return defaultDao.findOne(SELECT_ALL + WHERE_ID, id);
    }

    @Override
    public List<Role> findAll() {
        return defaultDao.findAll(SELECT_ALL);
    }

    @Override
    public Role insert(Role role) {
        Objects.requireNonNull(role, "Role object must be not null");

        int id = (int) defaultDao.executeInsertWithGeneratedPrimaryKey(
                INSERT,
                role.getName()
        );

        role.setId(id);

        return role;
    }

    @Override
    public void update(Role role) {
        Objects.requireNonNull(role);

        defaultDao.executeUpdate(
                UPDATE + WHERE_ID,
                role.getName(),
                role.getId()
        );
    }

    @Override
    public void delete(Integer id) {
        defaultDao.executeUpdate(
                DELETE + WHERE_ID,
                id);
    }

    @Override
    public Optional<Role> findOneByName(String name) {
        return defaultDao.findOne(SELECT_ALL + WHERE_NAME, name);
    }

    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();
        RoleDao mySqlRoleDao;

        try {
            mySqlRoleDao = new MySqlRoleDao(dataSource.getConnection());
            ((MySqlRoleDao) mySqlRoleDao).
                    printAll(mySqlRoleDao.findAll());
            System.out.println();

            System.out.println("Find one with id 1:");
            System.out.println(mySqlRoleDao.findOne(1));

            System.out.println("Find one by name MANAGER:");
            System.out.println(mySqlRoleDao.findOneByName("MANAGER"));

            System.out.println("Insert test:");
            Role accountType = mySqlRoleDao.
                    insert(new Role(0, "TEST"));
            ((MySqlRoleDao) mySqlRoleDao).
                    printAll(mySqlRoleDao.findAll());

            System.out.println("Update:");
            accountType.setName("TEST@222");
            mySqlRoleDao.update(accountType);
            ((MySqlRoleDao) mySqlRoleDao).
                    printAll(mySqlRoleDao.findAll());

            System.out.println("Delete:");
            mySqlRoleDao.delete(accountType.getId());
            ((MySqlRoleDao) mySqlRoleDao).
                    printAll(mySqlRoleDao.findAll());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void printAll(List<Role> list) {
        System.out.println("Find all:");
        for (Role type : list) {
            System.out.println(type);
        }
    }
}