package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "curr_annual_rate")
public class Rate {
  @Id
  long id;
  @Column(name = "annual_rate")
  double annualRate;
  @Column(name = "created_time")
  Date createdTime;

  public Rate(float annualRate, Date createdTime) {
    this.annualRate = annualRate;
    this.createdTime = createdTime;
  }

  public Rate() {}

  public double getAnnualRate() {
    return annualRate;
  }

  public void setAnnualRate(float annualRate) {
    this.annualRate = annualRate;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }
}
