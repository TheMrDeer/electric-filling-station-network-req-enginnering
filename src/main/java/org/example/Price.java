package org.example;

import java.time.LocalDateTime;
import java.util.Date;

public class Price {

    private String locationId;
    private ChargingStationType type;
    private double pricePerMinute;
    private double pricePerKwh;
    private LocalDateTime validFrom;
    private LocalDateTime validTo;

    // New Constructor for Temporal Tariff
    public Price(String locationId, ChargingStationType type, double pricePerMinute, double pricePerKwh, LocalDateTime validFrom, LocalDateTime validTo) {
        this.locationId = locationId;
        this.type = type;
        this.pricePerMinute = pricePerMinute;
        this.pricePerKwh = pricePerKwh;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    // Compatibility Constructors
    public Price() {
    }

    public Price(double ratePerMinute) {
        setPrice(ratePerMinute);
    }

    public Price(double ratePerMinute, double ratePerKwh) {
        setPrice(ratePerMinute, ratePerKwh);
    }

    // Compatibility Methods
    public void setPrice(double ratePerMinute) {
        this.pricePerMinute = ratePerMinute;
        this.pricePerKwh = 0.0;
    }

    public void setPrice(double ratePerMinute, double ratePerKwh) {
        this.pricePerMinute = ratePerMinute;
        this.pricePerKwh = ratePerKwh;
    }

    public double getRatePerMinute() {
        return pricePerMinute;
    }

    public double getRatePerKwh() {
        return pricePerKwh;
    }

    // New Getters
    public String getLocationId() {
        return locationId;
    }

    public ChargingStationType getType() {
        return type;
    }

    public double getPricePerMinute() {
        return pricePerMinute;
    }

    public double getPricePerKwh() {
        return pricePerKwh;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }
}
