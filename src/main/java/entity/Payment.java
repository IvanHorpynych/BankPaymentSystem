package entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by JohnUkraine on 5/06/2018.
 */
@Entity
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  @Column
  private BigDecimal amount;
  @ManyToOne
  @JoinColumn(name = "account_from")
  private Account accountFrom;
  @ManyToOne
  @JoinColumn(name = "account_to")
  private Account accountTo;
  @Column(name = "operation_date")
  private Date date;
  @Column(name = "card_number_from")
  private long cardNumberFrom;

  public static class Builder {
    private final Payment payment;

    public Builder() {
      payment = new Payment();
    }

    public Builder addId(long id) {
      payment.setId(id);
      return this;
    }

    public Builder addAmount(BigDecimal amount) {
      payment.setAmount(amount);
      return this;
    }

    public Builder addAccountFrom(Account accountFrom) {
      payment.setAccountFrom(accountFrom);
      return this;
    }

    public Builder addAccountTo(Account accountTo) {
      payment.setAccountTo(accountTo);
      return this;
    }

    public Builder addDate(Date date) {
      payment.setDate(date);
      return this;
    }

    public Builder addCardNumberFrom(Long cardNumber) {
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
    return "Payment{" + "id=" + id + ", amount=" + amount + ", accountFrom=" + accountFrom
        + ", cardNumberFrom=" + cardNumberFrom + ", accountTo=" + accountTo + '}';
  }

}
