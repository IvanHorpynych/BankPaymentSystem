package dao.hibernate.impl;

import dao.abstraction.UserDao;
import dao.datasource.PooledConnection;
import dao.hibernate.HibernateUtil;
import entity.Role;
import entity.User;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by JohnUkraine on 5/07/2018.
 */
public class HibernateUserDao implements UserDao {
   /* private final static String SELECT_ALL =
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
            "DELETE FROM user ";*/


    @Override
    public Optional<User> findOne(Long id) {

        try (Session session = HibernateUtil.getInstance()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root);
            query.where(criteriaBuilder.equal(root.get("id"), id));
            return Optional.ofNullable(session.createQuery(query).getSingleResult());
        }
    }

    @Override
    public Optional<User> findOneByEmail(String email) {
        try (Session session = HibernateUtil.getInstance()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root);
            query.where(criteriaBuilder.equal(root.get("email"), email));
            return Optional.ofNullable(session.createQuery(query).getSingleResult());
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getInstance()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
            Root<User> root = query.from(User.class);
            query.select(root);
            return session.createQuery(query).getResultList();
        }
    }

    @Override
    public User insert(User obj) {
        try (Session session = HibernateUtil.getInstance()) {
            session.save(obj);
            return obj;
        }
    }

    @Override
    public void update(User obj) {
        try (Session session = HibernateUtil.getInstance()) {
            Transaction transaction = session.beginTransaction();
            session.update(obj);
            transaction.commit();
        }
    }

    @Override
    public void delete(Long id) {
        try (Session session = HibernateUtil.getInstance()) {
            Transaction transaction = session.beginTransaction();
            User user = findOne(id).get();
            session.delete(user);
            transaction.commit();
        }
    }

    @Override
    public boolean existByEmail(String email) {
        return findOneByEmail(email).isPresent();
    }

    public static void main(String[] args) {

        System.out.println("Find all:");
        HibernateUserDao mySqlUserDao = new HibernateUserDao();
        for (User user : mySqlUserDao.findAll()) {
            System.out.println(user);
        }
        int random = (int) (Math.random() * 100);
        System.out.println("Find one:");
        System.out.println(mySqlUserDao.findOne(1L));
        System.out.println("find one dy email:");
        System.out.println(mySqlUserDao.findOneByEmail("test@test.com"));
        System.out.println("Insert:");
        User insertUser = mySqlUserDao.insert(User.newBuilder().addFirstName("first" + random).
                addLastName("last" + random).
                addEmail("test" + random + "@com").
                addPassword("123").
                addPhoneNumber("+123").
                addRole(new Role(Role.RoleIdentifier.
                        USER_ROLE.getId(), "USER")).
                build()
        );
        System.out.println(insertUser);
        System.out.println("If exist new user:");
        System.out.println(mySqlUserDao.existByEmail("test" + random + "@com"));
        System.out.println("Update:");
        User temp = mySqlUserDao.findOneByEmail("test" + random + "@com").get();
        temp.setFirstName("newFirst" + random);
        temp.setLastName("newLast" + random);
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
    }
}
