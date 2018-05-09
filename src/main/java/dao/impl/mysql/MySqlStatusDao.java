/*
package dao.impl.mysql;

import dao.abstraction.RoleDao;
import dao.abstraction.StatusDao;
import dao.impl.mysql.converter.DtoConverter;
import dao.impl.mysql.converter.RoleDtoConverter;
import entity.Role;

import java.sql.Connection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

*/
/**
 * Created by JohnUkraine on 5/07/2018.
 *//*

public class MySqlStatusDao implements StatusDao {
    private final static String SELECT_ALL =
            "SELECT * FROM status ";

    private final static String INSERT =
            "INSERT INTO status (name) " +
                    "VALUES(?);";

    private final static String UPDATE =
            "UPDATE status SET name = ? ";

    private final static String DELETE =
            "DELETE FROM status " ;

    private final static String WHERE_ID =
            "WHERE id = ? ";

    private final static String WHERE_NAME =
            "WHERE name LIKE ? ";


    private final DefaultDaoImpl<Role> defaultDao;

    public MySqlStatusDao(Connection connection) {
        this(connection, new RoleDtoConverter());
    }

    public MySqlStatusDao(Connection connection,
                          DtoConverter<Role> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlStatusDao(DefaultDaoImpl<Role> defaultDao) {
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
    public Role insert(Role obj) {
        Objects.requireNonNull(obj,"Status object must be not null");

        int id = (int)defaultDao.executeInsertWithGeneratedPrimaryKey(
                INSERT,
                obj.getName()
        );

        obj.setId(id);

        return obj;
    }

    @Override
    public void update(Role obj) {
        Objects.requireNonNull(obj);

        defaultDao.executeUpdate(
                UPDATE + WHERE_ID,
                obj.getName(),
                obj.getId()
        );
    }

    @Override
    public void delete(Integer id) {
        defaultDao.executeUpdate(
                DELETE + WHERE_ID,
                id);
    }
}
*/
