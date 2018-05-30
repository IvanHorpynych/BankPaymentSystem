package dao.abstraction;

import entity.User;

import java.util.Optional;

public interface UserDao extends GenericDao<User, Long>{

    /**
     * Retrieve user from database identified by email.
     * @param email identifier of user
     * @return optional, which contains retrieved object or null
     */
    Optional<User> findOneByEmail(String email);

    /**
     * Check if user exists in database.
     *
     * @param email user's identifier
     * @return {@code true} if exists else {@code false}
     */
    boolean existByEmail(String email);
}
