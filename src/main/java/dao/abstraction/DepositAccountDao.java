package dao.abstraction;

import entity.DepositAccount;

import java.math.BigDecimal;

public interface DepositAccountDao extends GenericAccountDao<DepositAccount> {

    /**
     * Updates certain debit account minimum balance.
     *
     * @param account account which status will be updated.
     * @param minBalance new minimum balance of account to update
     */
    void updateMinBalance(DepositAccount account, BigDecimal minBalance);

}
