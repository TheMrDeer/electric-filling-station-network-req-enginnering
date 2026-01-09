package org.example;

public class ChargingStation {

    public String getStationID() {
        return stationID;
    }

    private String stationID;

    public StationState getState() {
        return state;
    }

    public void setState(StationState state) {
        this.state = state;
    }

    private StationState state;
    private String locationId;

    public ChargingStationType getType() {
        return type;
    }

    ChargingStationType type;

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    private Price price;


    public ChargingStation(String stationID, StationState state, String locationId, ChargingStationType type, Price price) {
        this.stationID = stationID;
        this.state = state;
        this.locationId = locationId;
        this.type = type;
        this.price = price;
    }


    public void addChargingStation(){
        StationManager.addStation(this);
    }

    public void print() {
        System.out.printf("StationID: %s, State: %s, Location: %s", this.stationID, this.state, this.locationId);
    }

}
