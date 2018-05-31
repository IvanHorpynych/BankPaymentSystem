package dao.abstraction;

import entity.Account;
import entity.Status;
import entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface GenericAccountDao<T extends Account> extends GenericDao<T, Long>{
    /**
     * Retrieves all accounts associated with certain user.
     *
     * @param user user to retrieve accounts related with him
     * @return list of retrieved accounts
     */
    List<T> findByUser(User user);

    /**
     * Retrieves all accounts associated with certain account status.
     *
     * @return list of retrieved accounts
     */
    List<T> findAllNotClosed();

    /**
     * increase balance of certain amount.
     *
     * @param account account to increase
     * @param amount amount of increasing
     */
    void increaseBalance(Account account, BigDecimal amount);

    /**
     * decrease balance of certain amount.
     *
     * @param account account to decrease
     * @param amount amount of decreasing
     */
    void decreaseBalance(Account account, BigDecimal amount);

    /**
     * Updates certain account status.
     *
     * @param account account which status will be updated.
     * @param statusId new status of account to update
     */
    void updateAccountStatus(T account, int statusId);

}
