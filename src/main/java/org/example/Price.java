package org.example;

import java.util.Date;

public class Price {

    private String priceId;
    private double ratePerMinute;
    private ChargingStation chargingStation;
    private Date validFrom;
    private Date validTo;

    public Price(String priceId, double ratePerMinute, ChargingStation chargingStation, Date validFrom, Date validTo) {
        this.priceId = priceId;
        setPrice(ratePerMinute);
        this.chargingStation = chargingStation;
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
