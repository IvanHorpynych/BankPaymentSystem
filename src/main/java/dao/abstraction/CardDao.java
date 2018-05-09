package dao.abstraction;

import entity.*;

import java.util.List;

public interface CardDao extends GenericDao<Card, Long> {
    /**
     * Retrieves all cards associated with certain user.
     *
     * @param user user to retrieve cards
     * @return list of retrieved cards
     */
    List<Card> findByUser(User user);

    /**
     * Retrieves all cards associated with certain account.
     *
     * @param account account to retrieve cards
     * @return list of retrieved cards
     */
    List<Card> findByAccount(Account account);

    /**
     * Retrieves all cards associated with certain user and account type.
     *
     * @param user user to retrieve cards
     * @param accountType account type to retrieve cards
     * @return list of retrieved cards
     */
    List<Card> findByUserAndType(User user, AccountType accountType);

    /**
     * Retrieves all cards associated with certain user and account status.
     *
     * @param user user to retrieve cards
     * @param status account status to retrieve cards
     * @return list of retrieved cards
     */
    List<Card> findByUserAndStatus(User user, Status status);

    /**
     * Retrieves all cards associated with certain user and account type and status.
     *
     * @param user user to retrieve cards
     * @param accountType account type to retrieve cards
     * @param status account status to retrieve cards
     * @return list of retrieved cards
     */
    List<Card> findByUserAndTypeAndStatus(User user, AccountType accountType, Status status);

}
