package entity;


import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class Account {
    public final static BigDecimal DEFAULT_BALANCE = BigDecimal.ZERO;
    public final static long DEFAULT_NUMBER = 0L;

    public final static String DEFAULT_TYPE = "DEBIT";
    private final static int DEFAULT_TYPE_ID = AccountType.TypeIdentifier.
            DEBIT_TYPE.getId();

    public final static String DEFAULT_STATUS = "ACTIVE";
    private final static int DEFAULT_STATUS_ID = Status.StatusIdentifier.
            ACTIVE_STATUS.getId();

    public static final BigDecimal MAX_BALANCE = BigDecimal.valueOf(999999999.9999);

    private long accountNumber;
    private User accountHolder;
    private AccountType accountType;
    private BigDecimal balance;
    private Status status;


    public Account() {
    }


    public static abstract class AbstractBuilder<T extends AbstractBuilder<T,A>, A extends Account> {

        protected abstract T getThis();
        protected abstract A getAccount();
        public abstract Account build();

        public T addAccountNumber(long accountNumber) {
            getAccount().setAccountNumber(accountNumber);
            return getThis();
        }

        public T addAccountHolder(User accountHolder) {
            getAccount().setAccountHolder(accountHolder);
            return getThis();
        }

        public T addAccountType(AccountType accountType) {
            getAccount().setAccountType(accountType);
            return getThis();
        }

        public T addDefaultAccountType() {
            getAccount().setAccountType(new AccountType(DEFAULT_TYPE_ID,
                    DEFAULT_TYPE));
            return getThis();
        }

        public T addBalance(BigDecimal balance) {
            getAccount().setBalance(balance);
            return getThis();
        }

        public T addDefaultBalance() {
            getAccount().setBalance(DEFAULT_BALANCE);
            return getThis();
        }

        public T addStatus(Status status) {
            getAccount().setStatus(status);
            return getThis();
        }

        public T addDefaultStatus() {
            getAccount().setStatus(new Status(DEFAULT_STATUS_ID,
                    DEFAULT_STATUS));
            return getThis();
        }

    }

    public static class Builder extends Account.AbstractBuilder<Account.Builder, Account>{
        private final Account account;

        public Builder() {
            account = new Account();
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        protected Account getAccount() {
            return account;
        }

        @Override
        public Account build() {
            return account;
        }

    }

    public static final Account.Builder newBuilder() {
        return new Account.Builder();
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

    public boolean isActive(){
        return status.getId() == Status.StatusIdentifier.ACTIVE_STATUS.getId();
    }

    public boolean isBlocked(){
        return status.getId() == Status.StatusIdentifier.BLOCKED_STATUS.getId();
    }

    public boolean isClosed(){
        return status.getId() == Status.StatusIdentifier.CLOSED_STATUS.getId();
    }

    public boolean isNotClosed(){
        return status.getId() != Status.StatusIdentifier.CLOSED_STATUS.getId();
    }

    public boolean isATM(){
        return getAccountType().getId() == AccountType.TypeIdentifier.ATM_TYPE.getId();
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", user=" + accountHolder +
                ", type=" + accountType +
                ", status=" + status +
                ", balance=" + balance +
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
