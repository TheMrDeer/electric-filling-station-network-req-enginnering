package org.example;

import java.util.ArrayList;
import java.util.List;

public class StationManager {

    public static List<ChargingStation> getChargingStations() {
        return chargingStations;
    }

    private static List<ChargingStation> chargingStations = new ArrayList<>();

    public static List<Location> getLocations() {
        return locations;
    }

    private static List<Location> locations = new ArrayList<>();

    public static void addStation(ChargingStation chargingStation) {
        chargingStations.add(chargingStation);
    }

    public static void removeStation(ChargingStation chargingStation) {
        chargingStations.remove(chargingStation);
    }

    public static void removeStationById(String stationId) {
        chargingStations.removeIf(s -> s.getStationID().equals(stationId));
    }


    /*public void setPrice(ChargingStation station, Price price) {

    }*/

    public static void addLocation(Location location) {
        locations.add(location);
    }

    public static void removeLocation(Location location) {
        locations.remove(location);
    }

    public static void clearAll() {
        chargingStations.clear();
        locations.clear();
    }

    public void printStations() {
        for (ChargingStation cs : chargingStations) {
            cs.print();
        }
    }

    public static ChargingStation getStationById(String stationId) {
        return chargingStations.stream()
                .filter(s -> s.getStationID().equals(stationId))
                .findFirst()
                .orElse(null);
    }

    public static Location findLocationByName(String name){
        return locations.stream()
                .filter(l -> l.getName().equals(name))
                .findFirst()
                .orElse(null);
    }



    public static void setStationState(String stationId, StationState newState) {
        getStationById(stationId).setState(newState);
    }

    public void printLocations() {
        for (Location l : locations) {
            System.out.println(l.getLocationInfo());
        }
    }
}
