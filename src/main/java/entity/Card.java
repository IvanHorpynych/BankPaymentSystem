package entity;

import java.util.Date;

/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class Card {


    public enum CardType {
    VISA, MASTERCARD
}

    public final static String DEFAULT_STATUS = "ACTIVE";
    private final static int DEFAULT_STATUS_ID = Status.StatusIdentifier.
            ACTIVE_STATUS.getId();

    private long cardNumber;
    private Account account;
    private int pin;
    private int cvv;
    private Date expireDate;
    private CardType type;
    private Status status;


    public static class Builder{
        private final Card card;

        public Builder() {
            card = new Card();
        }

        public Builder addCardNumber(long cardNumber) {
            card.setCardNumber(cardNumber);
            return this;
        }

        public Builder addAccount(Account account) {
            card.setAccount(account);
            return this;
        }

        public Builder addPin(int pin) {
            card.setPin(pin);
            return this;
        }

        public Builder addCvv(int cvv) {
            card.setCvv(cvv);
            return this;
        }

        public Builder addExpireDate(Date date) {
            card.setExpireDate(date);
            return this;
        }

        public Builder addType(CardType type) {
            card.setType(type);
            return this;
        }

        public Builder addStatus(Status status) {
            card.setStatus(status);
            return this;
        }

        public Builder addDefaultStatus() {
            card.setStatus(new Status(DEFAULT_STATUS_ID,
                    DEFAULT_STATUS));
            return this;
        }

        public Card build() {
            return card;
        }
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }


    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public CardType getType() {
        return type;
    }

    public void setType(CardType type) {
        this.type = type;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isActive(){
        return status.getId() == Status.StatusIdentifier.ACTIVE_STATUS.getId() &&
                new Date().compareTo(expireDate) < 0;
    }

    public boolean isBlocked(){
        return status.getId() == Status.StatusIdentifier.BLOCKED_STATUS.getId();
    }

    @Override
    public String toString() {
        return "Card{" +
                "cardNumber=" + cardNumber +
                ", accountId=" + getAccount() +
                ", pin=" + pin +
                ", cvv=" + cvv +
                ", expireDate=" + expireDate +
                ", type=" + type.toString() +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || this.getClass() != o.getClass()) {return false;}

        Card card = (Card) o;

        return this.cardNumber == card.cardNumber;
    }

    @Override
    public int hashCode() {
        return (int) (cardNumber ^ (cardNumber >>> 32));
    }


}
