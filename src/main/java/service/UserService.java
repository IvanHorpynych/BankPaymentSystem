package service;

import dao.abstraction.UserDao;
import dao.factory.DaoFactory;
import dao.config.HibernateUtil;
import dao.util.hashing.PasswordStorage;
import entity.User;
import org.hibernate.Session;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer. Implements operations of finding,
 * creating, deleting entities. Uses dao layer.
 *
 * @author JohnUkraine
 */
public class UserService {
  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private UserService() {}

  private static class Singleton {
    private final static UserService INSTANCE = new UserService();
  }

  public static UserService getInstance() {
    return Singleton.INSTANCE;
  }

  public Optional<User> findById(Long id) {
    try(Session session = HibernateUtil.getInstance()) {
      UserDao userDao = daoFactory.getUserDao();
      return userDao.findOne(id);
    }
  }

  public Optional<User> findByEmail(String email) {
    try(Session session = HibernateUtil.getInstance()) {
      UserDao userDao = daoFactory.getUserDao();
      return userDao.findOneByEmail(email);
    }
  }

  public List<User> findAllUsers() {
    try(Session session = HibernateUtil.getInstance()) {
      UserDao userDao = daoFactory.getUserDao();
      return userDao.findAll();
    }
  }

  public User createUser(User user) {
    try(Session session = HibernateUtil.getInstance()) {
      Objects.requireNonNull(user);

      if (user.getRole() == null) {
        user.setDefaultRole();
      }

      String hash = PasswordStorage.getSecurePassword(user.getPassword());
      user.setPassword(hash);

      UserDao userDao = daoFactory.getUserDao();
      return userDao.insert(user);
    }
  }

  public boolean isCredentialsValid(String email, String password) {
    try(Session session = HibernateUtil.getInstance()) {
      UserDao userDao = daoFactory.getUserDao();
      Optional<User> user = userDao.findOneByEmail(email);

      return user.filter(u -> PasswordStorage.checkSecurePassword(password, u.getPassword()))
              .isPresent();
    }
  }

  public boolean isUserExists(User user){
    try(Session session = HibernateUtil.getInstance()) {
      UserDao userDao = daoFactory.getUserDao();
      return userDao.exist(user.getId()) || userDao.existByEmail(user.getEmail());
    }
  }
}
