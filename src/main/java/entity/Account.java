package entity;


import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by JohnUkraine on 5/06/2018.
 */

public abstract class Account {
    public final static BigDecimal DEFAULT_BALANCE = BigDecimal.ZERO;
    public final static long DEFAULT_NUMBER = 0L;

    public final static String DEFAULT_TYPE = "DEBIT";
    private final static int DEFAULT_TYPE_ID = AccountType.DEBIT_TYPE_ID;

    public final static String DEFAULT_STATUS = "ACTIVE";
    private final static int DEFAULT_STATUS_ID = Status.ACTIVE_STATUS_ID;

    public final static Date DEFAULT_DATE = new Date();

    private long accountNumber;
    private User accountHolder;
    private AccountType accountType;
    private BigDecimal balance;
    private Status status;


    public Account() {
    }

    /*public Account(long accountNumber, User accountHolder,
                    AccountType accountType) {
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.accountType = accountType;
    }*/

    public static abstract class AbstractBuilder<T extends AbstractBuilder<T,A>, A extends Account> {
        //private Account account;

        protected abstract T getThis();
        protected abstract A getAccount();
        public abstract Account build();

        public T setAccountNumber(long accountNumber) {
            getAccount().setAccountNumber(accountNumber);
            return getThis();
        }

        public T setAccountHolder(User accountHolder) {
            getAccount().setAccountHolder(accountHolder);
            return getThis();
        }

        public T setAccountType(AccountType accountType) {
            getAccount().setAccountType(accountType);
            return getThis();
        }

        public T setDefaultAccountType() {
            getAccount().setAccountType(new AccountType(DEFAULT_TYPE_ID,
                    DEFAULT_TYPE));
            return getThis();
        }

        public T setBalance(BigDecimal balance) {
            getAccount().setBalance(balance);
            return getThis();
        }

        public T setDefaultBalance() {
            getAccount().setBalance(DEFAULT_BALANCE);
            return getThis();
        }

        public T setStatus(Status status) {
            getAccount().setStatus(status);
            return getThis();
        }

        public T setDefaultStatus() {
            getAccount().setStatus(new Status(DEFAULT_STATUS_ID,
                    DEFAULT_STATUS));
            return getThis();
        }

    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public User getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(User accountHolder) {
        this.accountHolder = accountHolder;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }


    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", user=" + accountHolder +
                ", type=" + accountType +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;

        return accountNumber == account.accountNumber;
    }

    @Override
    public int hashCode() {
        return (int) (accountNumber ^ (accountNumber >>> 32));
    }
}
