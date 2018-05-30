package dao.impl.mysql;

import dao.abstraction.UserDao;
import dao.datasource.PooledConnection;
import dao.impl.mysql.converter.DtoConverter;
import dao.impl.mysql.converter.UserDtoConverter;
import entity.Role;
import entity.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class MySqlUserDao implements UserDao {
    private final static String SELECT_ALL =
            "SELECT user.id AS user_id, user.role_id," +
                    "user.first_name," +
                    "user.last_name, user.email," +
                    "user.password,user.phone_number," +
                    "role.id AS role_id, role.name AS role_name " +
                    "FROM user JOIN role ON user.role_id = role.id ";

    private final static String WHERE_ID =
            "WHERE user.id = ? ";

    private final static String WHERE_EMAIL =
            "WHERE user.email = ? ";

    private final static String INSERT =
            "INSERT into user (role_id, first_name, last_name," +
                    "email, phone_number, password)" +
                    "VALUES(?, ?, ?, ?, ?, ?) ";

    private final static String UPDATE =
            "UPDATE user SET " +
                    "first_name = ?, " +
                    "last_name = ?, email = ?, " +
                    "phone_number = ?, password = ? ";

    private final static String DELETE =
            "DELETE FROM user ";


    private final DefaultDaoImpl<User> defaultDao;

    public MySqlUserDao(Connection connection) {
        this(connection, new UserDtoConverter());
    }

    public MySqlUserDao(Connection connection,
                        DtoConverter<User> converter) {
        this.defaultDao = new DefaultDaoImpl<>(connection, converter);
    }

    public MySqlUserDao(DefaultDaoImpl<User> defaultDao) {
        this.defaultDao = defaultDao;
    }

    @Override
    public Optional<User> findOne(Long id) {
        return defaultDao.findOne(SELECT_ALL + WHERE_ID, id);
    }

    @Override
    public Optional<User> findOneByEmail(String email) {
        return defaultDao.findOne(SELECT_ALL + WHERE_EMAIL, email);
    }

    @Override
    public List<User> findAll() {
        return defaultDao.findAll(SELECT_ALL);
    }

    @Override
    public User insert(User obj) {
        Objects.requireNonNull(obj);

        int id = (int) defaultDao.executeInsertWithGeneratedPrimaryKey(
                INSERT,
                obj.getRole().getId(),
                obj.getFirstName(),
                obj.getLastName(),
                obj.getEmail(),
                obj.getPhoneNumber(),
                obj.getPassword()
        );

        obj.setId(id);
        return obj;
    }

    @Override
    public void update(User obj) {
        Objects.requireNonNull(obj);

        defaultDao.executeUpdate(
                UPDATE + WHERE_ID,
                obj.getFirstName(),
                obj.getLastName(),
                obj.getEmail(),
                obj.getPassword(),
                obj.getPhoneNumber(),
                obj.getId()
        );
    }

    @Override
    public void delete(Long id) {
        defaultDao.executeUpdate(
                DELETE + WHERE_ID, id);
    }

    @Override
    public boolean existByEmail(String email) {
        return findOneByEmail(email).isPresent();
    }

    public static void main(String[] args) {
        DataSource dataSource = PooledConnection.getInstance();

        System.out.println("Find all:");
        try {
            MySqlUserDao mySqlUserDao = new MySqlUserDao(dataSource.getConnection());
            for (User user : mySqlUserDao.findAll()) {
                System.out.println(user);
            }
            int random = (int)(Math.random()*100);
            System.out.println("Find one:");
            System.out.println(mySqlUserDao.findOne(1L));
            System.out.println("find one dy email:");
            System.out.println(mySqlUserDao.findOneByEmail("test@test.com"));
            System.out.println("Insert:");
            User insertUser = mySqlUserDao.insert(User.newBuilder().addFirstName("first"+random).
                    addLastName("last"+random).
                    addEmail("test"+random+"@com").
                    addPassword("123").
                    addPhoneNumber("+123").
                    addRole(new Role(Role.RoleIdentifier.
                            USER_ROLE.getId(),"USER")).
                    build()
            );
            System.out.println(insertUser);
            System.out.println("If exist new user:");
            System.out.println(mySqlUserDao.existByEmail("test"+random+"@com"));
            System.out.println("Update:");
            User temp = mySqlUserDao.findOneByEmail("test"+random+"@com").get();
            temp.setFirstName("newFirst"+random);
            temp.setLastName("newLast"+random);
            mySqlUserDao.update(temp);
            System.out.println("Result:");
            for (User user : mySqlUserDao.findAll()) {
                System.out.println(user);
            }
            System.out.println("Delete:");
            mySqlUserDao.delete(insertUser.getId());
            for (User user : mySqlUserDao.findAll()) {
                System.out.println(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
