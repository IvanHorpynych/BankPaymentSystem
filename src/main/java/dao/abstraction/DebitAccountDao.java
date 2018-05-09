package dao.abstraction;

import entity.DebitAccount;
import entity.Status;
import entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface DebitAccountDao extends GenericDao<DebitAccount, Long> {
    /**
     * Retrieves all debit accounts associated with certain user.
     *
     * @param user user to retrieve accounts related with him
     * @return list of retrieved accounts
     */
    List<DebitAccount> findByUser(User user);

    /**
     * Retrieves all debit accounts associated with certain account status.
     *
     * @param status status of account
     * @return list of retrieved accounts
     */
    List<DebitAccount> findByStatus(Status status);

    /**
     * Increase debit account balance of certain amount.
     *
     * @param account account to increase
     * @param amount amount of increasing
     */
    void increaseBalance(DebitAccount account, BigDecimal amount);

    /**
     * Decrease debit account balance of certain amount.
     *
     * @param account account to decrease
     * @param amount amount of decreasing
     */
    void decreaseBalance(DebitAccount account, BigDecimal amount);

    /**
     * Updates certain debit account status.
     *
     * @param account account which status will be updated.
     * @param status new status of account to update
     */
    void updateAccountStatus(DebitAccount account, Status status);

}
