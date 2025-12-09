package org.example;

import java.util.ArrayList;
import java.util.List;

public class Location {


    String locationId;
    String name;
    String address;
    List<ChargingStation> chargingStations = new ArrayList<>();
    //String status;

    public Location(String locationId, String name, String address) {
        this.locationId = locationId;
        this.name = name;
        this.address = address;
    }


    public void addLocation() {
        LocationRepository.addLocation(this);
    }

    public void removeLocation() {
        LocationRepository.removeLocation(this);
    }

    public void addChargingStationToLocation(ChargingStation chargingStation) {
        chargingStations.add(chargingStation);
    }

    public void print() {
        System.out.printf("LocationID: %s, Name: %s, Address: %s", this.locationId, this.name, this.address);
    }
}
