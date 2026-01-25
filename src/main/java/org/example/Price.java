package org.example;

import java.util.Date;

public class Price {

    private String priceId;
    private double ratePerMinute;
    private double ratePerKwh;
    private Date validFrom;
    private Date validTo;

    public Price() {
    }

    public Price(double ratePerMinute) {
        setPrice(ratePerMinute);
    }

    public Price(double ratePerMinute, double ratePerKwh) {
        setPrice(ratePerMinute, ratePerKwh);
    }

    public Price(String priceId, double ratePerMinute, Date validFrom, Date validTo) {
        this.priceId = priceId;
        setPrice(ratePerMinute);
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public Price(String priceId, double ratePerMinute, double ratePerKwh, Date validFrom, Date validTo) {
        this.priceId = priceId;
        setPrice(ratePerMinute, ratePerKwh);
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public void setPrice(double ratePerMinute) {
        this.ratePerMinute = ratePerMinute;
        this.ratePerKwh = 0.0;
    }

    public void setPrice(double ratePerMinute, double ratePerKwh) {
        this.ratePerMinute = ratePerMinute;
        this.ratePerKwh = ratePerKwh;
    }

    public double getRatePerMinute() {
        return ratePerMinute;
    }

    public double getRatePerKwh() {
        return ratePerKwh;
    }
}
