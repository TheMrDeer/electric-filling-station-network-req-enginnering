package org.example;

import java.util.ArrayList;
import java.util.List;

public class StationManager {

    public static List<ChargingStation> getChargingStations() {
        return chargingStations;
    }

    private static List<ChargingStation> chargingStations = new ArrayList<>();
    private static List<Location> locations = new ArrayList<>();

    public static void addStation(ChargingStation chargingStation) {
        chargingStations.add(chargingStation);
    }

    public static void removeStation(ChargingStation chargingStation) {
        chargingStations.remove(chargingStation);
    }

    /*public void setPrice(ChargingStation station, Price price) {

    }*/

    public static void addLocation(Location location) {
        locations.add(location);
    }

    public static void removeLocation(Location location) {
        locations.remove(location);
    }
    public void printStations() {
        for (ChargingStation cs : chargingStations) {
            cs.print();
        }
    }

    public void printLocations() {
        for (Location l:locations) {
            System.out.println(l.getLocationInfo());
        }
    }
}
