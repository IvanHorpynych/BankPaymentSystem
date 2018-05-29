package entity;

import java.util.Date;

public class Rate {

    long id;

    float annualRate;
    Date createdTime;

    public Rate(float annualRate, Date createdTime){
        this.annualRate = annualRate;
        this.createdTime = createdTime;
    }

    public float getAnnualRate() {
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
