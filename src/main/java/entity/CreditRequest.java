package entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by JohnUkraine on 5/07/2018.
 */

public class CreditRequest {

    public final static BigDecimal DEFAULT_BALANCE = BigDecimal.ZERO;

    public final static String DEFAULT_STATUS = "PENDING";
    private final static int DEFAULT_STATUS_ID = Status.StatusIdentifier.
            PENDING_STATUS.getId();

    public final static Date DEFAULT_DATE = new Date();

    private long requestNumber;
    private User accountHolder;
    private float interestRate;
    private BigDecimal creditLimit;
    private Status status;
    private Date validityDate;


    public CreditRequest() {
    }


    public static class Builder {
        private CreditRequest creditRequest;

        public Builder() {
            creditRequest = new CreditRequest();
        }

        public Builder addRequestNumber(long requestNumber) {
            creditRequest.setRequestNumber(requestNumber);
            return this;
        }

        public Builder addAccountHolder(User accountHolder) {
            creditRequest.setAccountHolder(accountHolder);
            return this;
        }

        public Builder addInterestRate(float interestRate) {
            creditRequest.setInterestRate(interestRate);
            return this;
        }

        public Builder addCreditLimit(BigDecimal creditLimit) {
            creditRequest.setCreditLimit(creditLimit);
            return this;
        }

        public Builder addStatus(Status status) {
            creditRequest.setStatus(status);
            return this;
        }

        public Builder addDefaultStatus() {
            creditRequest.setStatus(new Status(DEFAULT_STATUS_ID,
                    DEFAULT_STATUS));
            return this;
        }

        public CreditRequest.Builder addValidityDate(Date date) {
            creditRequest.setValidityDate(date);
            return this;
        }

        public CreditRequest build() {
            return creditRequest;
        }

    }

    public static CreditRequest.Builder newBuilder() {
        return new CreditRequest.Builder();
    }

    public long getRequestNumber() {
        return requestNumber;
    }

    public void setRequestNumber(long requestNumber) {
        this.requestNumber = requestNumber;
    }

    public User getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(User accountHolder) {
        this.accountHolder = accountHolder;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getValidityDate() {
        return validityDate;
    }

    public void setValidityDate(Date validityDate) {
        this.validityDate = validityDate;
    }

    public boolean isPending(){
        return status.getId() == Status.StatusIdentifier.PENDING_STATUS.getId();
    }

    @Override
    public String toString() {
        return "CreditRequest{" +
                "requestNumber=" + requestNumber +
                ", holder=" + accountHolder +
                ", creditLimit=" + creditLimit +
                ", interestRate=" + interestRate +
                ", status=" + status +
                ", validityDate=" + validityDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CreditRequest creditRequest = (CreditRequest) o;

        return requestNumber == creditRequest.requestNumber;
    }

    @Override
    public int hashCode() {
        return (int) (requestNumber ^ (requestNumber >>> 32));
    }

}
