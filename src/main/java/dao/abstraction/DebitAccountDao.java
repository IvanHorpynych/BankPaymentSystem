package dao.abstraction;

import entity.DebitAccount;

import java.math.BigDecimal;

public interface DebitAccountDao extends AccountDao<DebitAccount> {

    /**
     * Updates certain debit account minimum balance.
     *
     * @param account account which status will be updated.
     * @param minBalance new minimum balance of account to update
     */
    void updateMinBalance(DebitAccount account, BigDecimal minBalance);

}
