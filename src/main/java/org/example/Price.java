package org.example;

import java.util.Date;

public class Price {

    private String priceId;
    private double ratePerMinute;
    private Date validFrom;
    private Date validTo;


    public Price(double ratePerMinute) {
        setPrice(ratePerMinute);
    }

    public Price(String priceId, double ratePerMinute, Date validFrom, Date validTo) {
        this.priceId = priceId;
        setPrice(ratePerMinute);
        this.validFrom = validFrom;
        this.validTo = validTo;
    }
    public void setPrice(double ratePerMinute) {
        this.ratePerMinute = ratePerMinute;
    }
    public double getPrice() {
        return this.ratePerMinute;
    }

}
