package entity;

import java.util.Date;

/**
 * Created by JohnUkraine on 5/06/2018.
 */

public class Card {


    public enum CardType {
    VISA, MASTERCARD
}
    public final static long DEFAULT_NUMBER = 0L;

    private long cardNumber;
    private Account account;
    private int pin;
    private int cvv;
    private Date expireDate;
    private CardType type;


    public static class Builder{
        private final Card card;

        public Builder() {
            card = new Card();
        }

        public Builder setCardNumber(long cardNumber) {
            card.setCardNumber(cardNumber);
            return this;
        }

        public Builder setAccount(Account account) {
            card.setAccount(account);
            return this;
        }

        public Builder setPin(int pin) {
            card.setPin(pin);
            return this;
        }

        public Builder setCvv(int cvv) {
            card.setCvv(cvv);
            return this;
        }

        public Builder setExpireDate(Date date) {
            card.setExpireDate(date);
            return this;
        }

        public Builder setType(CardType type) {
            card.setType(type);
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
