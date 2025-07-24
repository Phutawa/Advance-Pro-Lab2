package se233.chapter2.model;

import java.io.File;

public class CurrencyEntity {
    private Double rate;
    private String date;

    public CurrencyEntity(Double rate, String date) {
        this.rate = rate;
        this.date = date;
    }
    public Double getRate () {
        return rate;
    }
    public String getTimestamp() {
        return date;
    }
    @Override
    public String toString () {
        return String.format("%s %.4f" , date, rate);
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
