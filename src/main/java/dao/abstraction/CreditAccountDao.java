package dao.abstraction;

import entity.Account;
import entity.CreditAccount;
import entity.Status;
import entity.User;

import java.math.BigDecimal;
import java.util.List;

public interface CreditAccountDao extends GenericDao<CreditAccount, Long> {
    /**
     * Retrieves all credit accounts associated with certain user.
     *
     * @param user user to retrieve accounts related with him
     * @return list of retrieved accounts
     */
    List<CreditAccount> findByUser(User user);

    /**
     * Retrieves all credit accounts associated with certain account status.
     *
     * @param status status of account
     * @return list of retrieved accounts
     */
    List<CreditAccount> findByStatus(Status status);

    /**
     * Pay off loan of certain amount.
     *
     * @param account account to pay off a loan
     * @param amount amount of pay off
     */
    void increaseBalance(CreditAccount account, BigDecimal amount);

    /**
     * Take loan of certain amount.
     *
     * @param account account to take loan
     * @param amount amount of taking
     */
    void decreaseBalance(CreditAccount account, BigDecimal amount);

    /**
     * Updates certain credit account status.
     *
     * @param account account which status will be updated.
     * @param status new status of account to update
     */
    void updateAccountStatus(CreditAccount account, Status status);

}
