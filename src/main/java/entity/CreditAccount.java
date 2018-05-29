package entity;

/**
 * Created by JohnUkraine on 5/06/2018.
 */

import java.math.BigDecimal;
import java.util.Date;

public class CreditAccount extends Account{
    public final static String DEFAULT_TYPE = "CREDIT";
    private final static int DEFAULT_TYPE_ID = AccountType.TypeIdentifier.
            CREDIT_TYPE.getId();

    private BigDecimal creditLimit;
    private float interestRate;
    private BigDecimal accruedInterest;
    private Date validityDate;


    public CreditAccount() {
    }

    public static class Builder extends Account.AbstractBuilder<CreditAccount.Builder, CreditAccount>{
        private final CreditAccount creditAccount;

        public Builder() {
            creditAccount = new CreditAccount();
        }

        public CreditAccount.Builder addAccountType(AccountType accountType) {
            creditAccount.setAccountType(accountType);
            return this;
        }

        public CreditAccount.Builder addDefaultAccountType() {
            creditAccount.setAccountType(new AccountType(DEFAULT_TYPE_ID,
                    DEFAULT_TYPE));
            return this;
        }

        public CreditAccount.Builder addCreditLimit(BigDecimal creditLimit) {
            creditAccount.setCreditLimit(creditLimit);
            return this;
        }

        public CreditAccount.Builder addInterestRate(float interestRate) {
            creditAccount.setInterestRate(interestRate);
            return this;
        }


        public CreditAccount.Builder addAccruedInterest(BigDecimal accruedInterest) {
            creditAccount.setAccruedInterest(accruedInterest);
            return this;
        }

        public CreditAccount.Builder addValidityDate(Date date) {
            creditAccount.setValidityDate(date);
            return this;
        }

        @Override
        protected CreditAccount.Builder getThis() {
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

    public static CreditAccount.Builder newCreditBuilder() {
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

    @Override
    public boolean isActive(){
        return (getStatus().getId() == Status.StatusIdentifier.ACTIVE_STATUS.getId() &&
                new Date().compareTo(validityDate) < 0);
    }

}
