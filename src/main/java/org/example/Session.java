package org.example;

import java.time.LocalDateTime;
import java.time.Duration;

public class Session {

    private String sessionId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;   // in Minuten
    private double chargedEnergy; // in kWh
    private double totalCost;
    private String stationId;
    private boolean isSessionActive;
    private final Customer customer;

    public void setDuration(long minutes) {
        this.duration = minutes;
    }
    
    public void setChargedEnergy(double kwh) {
        this.chargedEnergy = kwh;
    }

    public double getChargedEnergy() {
        return chargedEnergy;
    }

    public boolean isSessionActive() {
        return isSessionActive;
    }

    public Session(String sessionId, String stationId, Customer customer) {
        this.sessionId = sessionId;
        this.stationId = stationId;
        this.customer = customer;
    }

    public void startSession() {
        this.startTime = LocalDateTime.now();
        isSessionActive = true;
        StationManager.setStationState(stationId, StationState.Occupied);
    }

    public void endSession() {
        this.endTime = this.startTime.plusMinutes(this.duration);
        isSessionActive = false;
        StationManager.setStationState(stationId,StationState.inOperationFree);
        this.customer.updateBalance(calculateCost());
    }

    public double calculateCost() {
        ChargingStation station = StationManager.getStationById(this.stationId);
        double ratePerMinute = station.getPrice().getRatePerMinute();
        double ratePerKwh = station.getPrice().getRatePerKwh();
        this.totalCost = (duration * ratePerMinute) + (chargedEnergy * ratePerKwh);
        return totalCost;
    }

    public String getSessionId() {
        return sessionId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getDuration() {
        return duration;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getStationId() {
        return stationId;
    }

    public String getCutomerId() {
        return this.customer.getUserId();
    }
}
