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
        StationManager.addLocation(this);
    }

    public void deleteLocation() {
        StationManager.removeLocation(this);
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
}
