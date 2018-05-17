package entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class DepositAccount extends Account{
    public final static String DEFAULT_TYPE = "DEBIT";
    private final static int DEFAULT_TYPE_ID = AccountType.DEBIT_TYPE_ID;

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

        public DepositAccount.Builder setAccountType(AccountType accountType) {
            depositAccount.setAccountType(accountType);
            return this;
        }

        public DepositAccount.Builder setDefaultAccountType() {
            depositAccount.setAccountType(new AccountType(DEFAULT_TYPE_ID,
                    DEFAULT_TYPE));
            return this;
        }

        public DepositAccount.Builder setMinBalance(BigDecimal minBalance) {
            depositAccount.setMinBalance(minBalance);
            return this;
        }

        public DepositAccount.Builder setDefaultMinBalance() {
            depositAccount.setMinBalance(DEFAULT_BALANCE);
            return this;
        }

        public DepositAccount.Builder setAnnualRate(float annualRate) {
            depositAccount.setAnnualRate(annualRate);
            return this;
        }

        public DepositAccount.Builder setLastOperationDate(Date date) {
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

    public static DepositAccount.Builder newBuilder() {
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
