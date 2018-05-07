package entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class DebitAccount extends Account{
    public final static String DEFAULT_TYPE = "DEBIT";
    private final static int DEFAULT_TYPE_ID = AccountType.DEBIT_TYPE_ID;

    private BigDecimal minBalance;
    private int annualRate;
    private Date lastOperationDate;


    public DebitAccount() {
    }

    public static class Builder extends Account.AbstractBuilder<Builder>{
        private final DebitAccount debitAccount;

        public Builder() {
            debitAccount = new DebitAccount();
        }

        public DebitAccount.Builder setAccountType(AccountType accountType) {
            debitAccount.setAccountType(accountType);
            return this;
        }

        public DebitAccount.Builder setDefaultAccountType() {
            debitAccount.setAccountType(new AccountType(DEFAULT_TYPE_ID,
                    DEFAULT_TYPE));
            return this;
        }

        public DebitAccount.Builder setMinBalance(BigDecimal minBalance) {
            debitAccount.setMinBalance(minBalance);
            return this;
        }

        public DebitAccount.Builder setDefaultMinBalance() {
            debitAccount.setMinBalance(DEFAULT_BALANCE);
            return this;
        }

        public DebitAccount.Builder setAnnualRate(int annualRate) {
            debitAccount.setAnnualRate(annualRate);
            return this;
        }

        public DebitAccount.Builder setLastOperationDate(Date date) {
            debitAccount.setLastOperationDate(date);
            return this;
        }


        @Override
        protected Builder getThis() {
            return null;
        }
    }

    public static DebitAccount.Builder newBuilder() {
        return new DebitAccount.Builder();
    }


    public BigDecimal getMinBalance() {
        return minBalance;
    }

    public void setMinBalance(BigDecimal minBalance) {
        this.minBalance = minBalance;
    }

    public int getAnnualRate() {
        return annualRate;
    }

    public void setAnnualRate(int annualRate) {
        this.annualRate = annualRate;
    }

    public Date getLastOperationDate() {
        return lastOperationDate;
    }

    public void setLastOperationDate(Date lastOperationDate) {
        this.lastOperationDate = lastOperationDate;
    }

}
