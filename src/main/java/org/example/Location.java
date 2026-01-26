package org.example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Location {

    private String locationId;
    private String name;
    private String address;
    private Status status;
    private List<ChargingStation> chargingStations = new ArrayList<>();
    private List<Price> priceHistory = new ArrayList<>();

    //create
    public Location(String locationId, String name, String address, Status status) {
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.status = status;
        addLocation();
    }

    public String getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public void addLocation() {
        StationManager.getInstance().addLocation(this);
    }

    public void deleteLocation() {
        StationManager.getInstance().removeLocation(this);
    }

    public void addChargingStationToLocation(ChargingStation chargingStation) {
        chargingStations.add(chargingStation);
    }

    public String getLocationInfo() {
        return String.format("LocationID: %s, Name: %s, Address: %s", this.locationId, this.name, this.address);
    }

    public void addPrice(Price newPrice) {
        // Optional: Close the previous price for that type
        for (Price p : priceHistory) {
            if (p.getType() == newPrice.getType() && p.getValidTo() == null) {
                if (newPrice.getValidFrom().isAfter(p.getValidFrom())) {
                    p.setValidTo(newPrice.getValidFrom());
                }
            }
        }
        priceHistory.add(newPrice);
    }

    public Price getPriceFor(ChargingStationType type, LocalDateTime time) {
        for (Price price : priceHistory) {
            if (price.getType() == type) {
                boolean isStarted = !time.isBefore(price.getValidFrom());
                boolean isNotEnded = price.getValidTo() == null || time.isBefore(price.getValidTo());
                
                if (isStarted && isNotEnded) {
                    return price;
                }
            }
        }
        return null; // Or throw exception if no price found
    }

    //update
    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setChargingStations(List<ChargingStation> chargingStations) {
        this.chargingStations = chargingStations;
    }

    public void setPriceHistory(List<Price> priceHistory) {
        this.priceHistory = priceHistory;
    }
}
