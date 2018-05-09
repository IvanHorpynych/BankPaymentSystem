package entity;

/**
 * Created by JohnUkraine on 5/06/2018.
 */

import java.math.BigDecimal;
import java.util.Date;

public class CreditAccount extends Account{
    public final static String DEFAULT_TYPE = "CREDIT";
    private final static int DEFAULT_TYPE_ID = AccountType.CREDIT_TYPE_ID;

    private BigDecimal creditLimit;
    private float interestRate;
    private Date lastOperationDate;
    private BigDecimal accruedInterest;
    private Date validityDate;


    public CreditAccount() {
    }

    public static class Builder extends Account.AbstractBuilder<Builder, CreditAccount>{
        private final CreditAccount creditAccount;

        public Builder() {
            creditAccount = new CreditAccount();
        }

        public CreditAccount.Builder setAccountType(AccountType accountType) {
            creditAccount.setAccountType(accountType);
            return this;
        }

        public CreditAccount.Builder setDefaultAccountType() {
            creditAccount.setAccountType(new AccountType(DEFAULT_TYPE_ID,
                    DEFAULT_TYPE));
            return this;
        }

        public CreditAccount.Builder setCreditLimit(BigDecimal creditLimit) {
            creditAccount.setCreditLimit(creditLimit);
            return this;
        }

        public CreditAccount.Builder setInterestRate(float interestRate) {
            creditAccount.setInterestRate(interestRate);
            return this;
        }

        public CreditAccount.Builder setLastOperationDate(Date date) {
            creditAccount.setLastOperationDate(date);
            return this;
        }


        public CreditAccount.Builder setAccruedInterest(BigDecimal accruedInterest) {
            creditAccount.setAccruedInterest(accruedInterest);
            return this;
        }

        public CreditAccount.Builder setValidityDate(Date date) {
            creditAccount.setValidityDate(date);
            return this;
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        protected CreditAccount getAccount() {
            return creditAccount;
        }

        @Override
        public CreditAccount build() {
            return creditAccount;
        }
    }

    public static CreditAccount.Builder newBuilder() {
        return new CreditAccount.Builder();
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    public Date getLastOperationDate() {
        return lastOperationDate;
    }

    public void setLastOperationDate(Date lastOperationDate) {
        this.lastOperationDate = lastOperationDate;
    }

    public BigDecimal getAccruedInterest() {
        return accruedInterest;
    }

    public void setAccruedInterest(BigDecimal accruedInterest) {
        this.accruedInterest = accruedInterest;
    }

    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }

}
