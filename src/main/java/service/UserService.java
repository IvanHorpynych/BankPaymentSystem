package service;

import dao.abstraction.UserDao;
import dao.factory.DaoFactory;
import dao.util.hashing.PasswordStorage;
import entity.User;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Intermediate layer between command layer and dao layer. Implements operations of finding,
 * creating, deleting entities. Uses dao layer.
 *
 * @author JohnUkraine
 */
public class UserService implements TransactionServiceInvoker {
  private final DaoFactory daoFactory = DaoFactory.getInstance();

  private UserService() {}

  private static class Singleton {
    private final static UserService INSTANCE = new UserService();
  }

  public static UserService getInstance() {
    return Singleton.INSTANCE;
  }

  public Optional<User> findById(Long id) {
    return transactionOperation(() -> daoFactory.getUserDao().findOne(id));
  }

  public Optional<User> findByEmail(String email) {
    return transactionOperation(() -> daoFactory.getUserDao().findOneByEmail(email));
  }

  public List<User> findAllUsers() {
    return transactionOperation(() -> daoFactory.getUserDao().findAll());
  }

  public User createUser(User user) {
    Objects.requireNonNull(user);

    if (user.getRole() == null) {
      user.setDefaultRole();
    }

    String hash = PasswordStorage.getSecurePassword(user.getPassword());
    user.setPassword(hash);

    return transactionOperation(() -> daoFactory.getUserDao().insert(user));

  }

  public boolean isCredentialsValid(String email, String password) {

    Optional<User> user = transactionOperation(() -> daoFactory.getUserDao().findOneByEmail(email));

    return user.filter(u -> PasswordStorage.checkSecurePassword(password, u.getPassword()))
        .isPresent();
  }

  public boolean isUserExists(User user) {
    UserDao userDao = daoFactory.getUserDao();
    return userDao.exist(user.getId()) || userDao.existByEmail(user.getEmail());
  }
}
