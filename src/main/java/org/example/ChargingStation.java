package org.example;

public class ChargingStation {

    private String stationID;
    private StationState state;
    private String locationId;
    ChargingStationType type;


    public ChargingStation(String stationID, StationState state, String locationId, ChargingStationType type) {
        this.stationID = stationID;
        this.state = state;
        this.locationId = locationId;
        this.type = type;
    }

    public void print() {
        System.out.printf("StationID: %s, State: %s, Location: %s", this.stationID, this.state, this.locationId);
    }

}
