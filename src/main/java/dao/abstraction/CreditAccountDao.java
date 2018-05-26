package dao.abstraction;

import entity.CreditAccount;

import java.math.BigDecimal;

public interface CreditAccountDao extends GenericAccountDao<CreditAccount> {

    /**
     * increase accrued interest of certain amount.
     *
     * @param account account to increase interest
     * @param amount amount of increasing
     */
    void increaseAccruedInterest(CreditAccount account, BigDecimal amount);

    /**
     * decrease accrued interest of certain amount.
     *
     * @param account account to decrease interest
     * @param amount amount of decreasing
     */
    void decreaseAccruedInterest(CreditAccount account, BigDecimal amount);

}
