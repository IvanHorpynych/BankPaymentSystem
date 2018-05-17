package entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class Payment {
    private long id;
    private BigDecimal amount;
    private Account accountFrom;
    private Account accountTo;
    private Date date;
    private long cardNumberFrom;

    public static class Builder {
        private final Payment payment;

        public Builder() {
            payment = new Payment();
        }

        public Builder setId(long id) {
            payment.setId(id);
            return this;
        }

        public Builder setAmount(BigDecimal amount) {
            payment.setAmount(amount);
            return this;
        }

        public Builder setAccountFrom(Account accountFrom) {
            payment.setAccountFrom(accountFrom);
            return this;
        }

        public Builder setAccountTo(Account accountTo) {
            payment.setAccountTo(accountTo);
            return this;
        }

        public Builder setDate(Date date) {
            payment.setDate(date);
            return this;
        }
        public Builder setCardNumberFrom(Long cardNumber) {
            payment.setCardNumberFrom(cardNumber);
            return this;
        }

        public Payment build() {
            return payment;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getCardNumberFrom() {
        return cardNumberFrom;
    }

    public void setCardNumberFrom(long cardNumberFrom) {
        this.cardNumberFrom = cardNumberFrom;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", amount=" + amount  +
                ", accountFrom=" + accountFrom  +
                ", cardNumberFrom=" + cardNumberFrom  +
                ", accountTo=" + accountTo  +
                '}';
    }
}
