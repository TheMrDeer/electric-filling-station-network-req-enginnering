package org.example;

import java.time.LocalDateTime;

public class Session {

    private String sessionId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private long duration;   // in Minutes
    private double chargedEnergy; // in kWh
    private double totalCost;
    private String stationId;
    private boolean isSessionActive;
    private final Customer customer;
    
    // Snapshot Data
    private String locationName;
    private ChargingStationType stationType;
    private double appliedPricePerMinute;
    private double appliedPricePerKwh;

    public Session(String sessionId, String stationId, Customer customer) {
        this.sessionId = sessionId;
        this.stationId = stationId;
        this.customer = customer;
    }

    public void startSession() {
        this.startTime = LocalDateTime.now();
        isSessionActive = true;
        StationManager.setStationState(stationId, StationState.Occupied);
        
        // Fetch Snapshot Data
        ChargingStation station = StationManager.getStationById(this.stationId);
        if (station != null) {
            this.stationType = station.getType();
            
            Location location = StationManager.getLocations().stream()
                    .filter(l -> l.getLocationId().equals(station.getLocationId()))
                    .findFirst()
                    .orElse(null);
            
            if (location != null) {
                this.locationName = location.getName();
                
                Price currentPrice = location.getPriceFor(station.getType(), this.startTime);
                if (currentPrice != null) {
                    this.appliedPricePerMinute = currentPrice.getPricePerMinute();
                    this.appliedPricePerKwh = currentPrice.getPricePerKwh();
                } else {
                    this.appliedPricePerMinute = 0.0;
                    this.appliedPricePerKwh = 0.0;
                }
            }
        }
    }

    public void endSession() {
        if (this.startTime != null) {
            this.endTime = this.startTime.plusMinutes(this.duration);
        } else {
            this.endTime = LocalDateTime.now(); // Fallback if startSession wasn't called properly
        }
        isSessionActive = false;
        StationManager.setStationState(stationId, StationState.inOperationFree);
        this.customer.updateBalance(calculateCost());
    }

    public double calculateCost() {
        this.totalCost = (duration * appliedPricePerMinute) + (chargedEnergy * appliedPricePerKwh);
        return totalCost;
    }

    public void setDuration(long minutes) {
        this.duration = minutes;
    }

    public void setChargedEnergy(double kwh) {
        this.chargedEnergy = kwh;
    }
    
    // For testing purposes to set past dates
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public double getChargedEnergy() {
        return chargedEnergy;
    }

    public boolean isSessionActive() {
        return isSessionActive;
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
    
    public String getLocationName() {
        return locationName;
    }
    
    public ChargingStationType getStationType() {
        return stationType;
    }
}
