package org.example;

public class ChargingStation {


    private String stationID;
    private StationState state;
    private String locationId;
    ChargingStationType type;
    private Price price;


    public ChargingStation(String stationID, StationState state, String locationId, ChargingStationType type, Price price) {
        this.stationID = stationID;
        this.state = state;
        this.locationId = locationId;
        this.type = type;
        this.price = price;
    }

    public String getStationID() {
        return stationID;
    }

    public StationState getState() {
        return state;
    }

    public ChargingStationType getType() {
        return type;
    }

    public void setState(StationState state) {
        this.state = state;
    }

    public void setType(ChargingStationType type) {
        this.type = type;
    }

    public Price getPrice() {
        return price;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public void addChargingStation() {
        StationManager.getInstance().addStation(this);
    }

    public void print() {
        System.out.printf("StationID: %s, State: %s, Location: %s", this.stationID, this.state, this.locationId);
    }


}
