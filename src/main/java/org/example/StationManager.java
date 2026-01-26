package org.example;

import java.util.ArrayList;
import java.util.List;

public class StationManager {

    private static StationManager instance;

    private List<ChargingStation> chargingStations;
    private List<Location> locations;

    private StationManager() {
        chargingStations = new ArrayList<>();
        locations = new ArrayList<>();
    }

    public static synchronized StationManager getInstance() {
        if (instance == null) {
            instance = new StationManager();
        }
        return instance;
    }

    public List<ChargingStation> getChargingStations() {
        return chargingStations;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void addStation(ChargingStation chargingStation) {
        if (getStationById(chargingStation.getStationID()) != null) {
            throw new IllegalArgumentException("Station ID already exists");
        }
        boolean locationExists = locations.stream()
                .anyMatch(l -> l.getLocationId().equals(chargingStation.getLocationId()));

        if (!locationExists) {
            throw new IllegalArgumentException("Location does not exist");
        }
        chargingStations.add(chargingStation);
    }

    public void removeStation(ChargingStation chargingStation) {
        chargingStations.remove(chargingStation);
    }

    public void removeStationById(String stationId) {
        chargingStations.removeIf(s -> s.getStationID().equals(stationId));
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public void removeLocation(Location location) {
        locations.remove(location);
    }

    public void clearAll() {
        chargingStations.clear();
        locations.clear();
    }

    public void printStations() {
        for (ChargingStation cs : chargingStations) {
            cs.print();
        }
    }

    public ChargingStation getStationById(String stationId) {
        return chargingStations.stream()
                .filter(s -> s.getStationID().equals(stationId))
                .findFirst()
                .orElse(null);
    }

    public Location findLocationByName(String name){
        return locations.stream()
                .filter(l -> l.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public void setStationState(String stationId, StationState newState) {
        ChargingStation station = getStationById(stationId);
        if (station != null) {
            station.setState(newState);
        }
    }

    public void printLocations() {
        for (Location l : locations) {
            System.out.println(l.getLocationInfo());
        }
    }
}
