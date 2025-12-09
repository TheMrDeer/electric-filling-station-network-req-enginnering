package org.example;

public class ChargingStation {

    String stationID;
    StationState state;
    String locationId;

    public ChargingStation(String stationID, StationState state, String locationId) {
        this.stationID = stationID;
        this.state = state;
        this.locationId = locationId;
    }

    public void print() {
        System.out.printf("StationID: %s, State: %s, Location: %s", this.stationID, this.state, this.locationId);
    }

}
