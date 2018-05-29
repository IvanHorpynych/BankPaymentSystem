package entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class DepositAccount extends Account{
    public final static String DEFAULT_TYPE = "DEPOSIT";
    private final static int DEFAULT_TYPE_ID = AccountType.TypeIdentifier.
            DEPOSIT_TYPE.getId();

    private BigDecimal minBalance;
    private float annualRate;
    private Date lastOperationDate;


    public DepositAccount() {
    }

    public static class Builder extends Account.AbstractBuilder<Builder, DepositAccount>{
        private final DepositAccount depositAccount;

        public Builder() {
            depositAccount = new DepositAccount();
        }

        public DepositAccount.Builder addAccountType(AccountType accountType) {
            depositAccount.setAccountType(accountType);
            return this;
        }

        public DepositAccount.Builder addDefaultAccountType() {
            depositAccount.setAccountType(new AccountType(DEFAULT_TYPE_ID,
                    DEFAULT_TYPE));
            return this;
        }

        public DepositAccount.Builder addMinBalance(BigDecimal minBalance) {
            depositAccount.setMinBalance(minBalance);
            return this;
        }

        public DepositAccount.Builder addDefaultMinBalance() {
            depositAccount.setMinBalance(DEFAULT_BALANCE);
            return this;
        }

        public DepositAccount.Builder addAnnualRate(float annualRate) {
            depositAccount.setAnnualRate(annualRate);
            return this;
        }

        public DepositAccount.Builder addLastOperationDate(Date date) {
            depositAccount.setLastOperationDate(date);
            return this;
        }


        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        protected DepositAccount getAccount() {
            return depositAccount;
        }

        @Override
        public DepositAccount build() {
            return depositAccount;
        }
    }

    public static DepositAccount.Builder newDepositBuilder() {
        return new DepositAccount.Builder();
    }


    public BigDecimal getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(BigDecimal minBalance) {
        this.minBalance = minBalance;
    }

    public float getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(float annualRate) {
        this.annualRate = annualRate;
    }

    public Date getLastOperationDate() {
        return lastOperationDate;
    }

    public void setLastOperationDate(Date lastOperationDate) {
        this.lastOperationDate = lastOperationDate;
    }

}
