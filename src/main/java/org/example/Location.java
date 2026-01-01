package org.example;

import java.util.ArrayList;
import java.util.List;

public class Location {


    private String locationId;
    private String name;
    private String address;
    private Status status; //enum? besser in Ch.Station?
    List<ChargingStation> chargingStations = new ArrayList<>(); // fehlt im UML

    public Location(String locationId, String name, String address, Status status) {
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.status = status;
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
}
