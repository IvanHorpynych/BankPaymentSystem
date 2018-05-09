package dao.abstraction;

import entity.RegularAccount;
import entity.Status;
import entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface RegularAccountDao extends GenericDao<RegularAccount, Long> {
    /**
     * Retrieves all regular accounts associated with certain user.
     *
     * @param user user to retrieve accounts related with him
     * @return list of retrieved accounts
     */
    List<RegularAccount> findByUser(User user);

    /**
     * Retrieves all regular accounts associated with certain account status.
     *
     * @param status status of account
     * @return list of retrieved accounts
     */
    List<RegularAccount> findByStatus(Status status);

    /**
     * Increase regular account balance of certain amount.
     *
     * @param account account to increase
     * @param amount amount of increasing
     */
    void increaseBalance(RegularAccount account, BigDecimal amount);

    /**
     * Decrease regular account balance of certain amount.
     *
     * @param account account to decrease
     * @param amount amount of decreasing
     */
    void decreaseBalance(RegularAccount account, BigDecimal amount);

    /**
     * Updates certain account status.
     *
     * @param account account which status will be updated.
     * @param status new status of account to update
     */
    void updateAccountStatus(RegularAccount account, Status status);

}
