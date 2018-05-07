package entity;


import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by JohnUkraine on 5/06/2018.
 */

abstract class Account {
    public final static BigDecimal DEFAULT_BALANCE = BigDecimal.ZERO;

    public final static String DEFAULT_TYPE = "REGULAR";
    private final static int DEFAULT_TYPE_ID = AccountType.REGULAR_TYPE_ID;

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

    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {
        private Account account;

        protected abstract T getThis();

        public T setAccountNumber(long accountNumber) {
            account.setAccountNumber(accountNumber);
            return getThis();
        }

        public T setAccountHolder(User accountHolder) {
            account.setAccountHolder(accountHolder);
            return getThis();
        }

        public T setAccountType(AccountType accountType) {
            account.setAccountType(accountType);
            return getThis();
        }

        public T setDefaultAccountType() {
            account.setAccountType(new AccountType(DEFAULT_TYPE_ID,
                    DEFAULT_TYPE));
            return getThis();
        }

        public T setBalance(BigDecimal balance) {
            account.setBalance(balance);
            return getThis();
        }

        public T setDefaultBalance() {
            account.setBalance(DEFAULT_BALANCE);
            return getThis();
        }

        public T setStatus(Status status) {
            account.setStatus(status);
            return getThis();
        }

        public T setDefaultStatus() {
            account.setStatus(new Status(DEFAULT_STATUS_ID,
                    DEFAULT_STATUS));
            return getThis();
        }

        public Account build() {
            return account;
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

    public void setAccountHolder(User user) {
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
                "holder=" + accountHolder +
                ", type=" + accountType.getName() +
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
