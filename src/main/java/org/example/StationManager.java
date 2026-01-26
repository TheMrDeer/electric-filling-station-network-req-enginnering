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

    // CRUD: Location (create)
    public Location createLocation(String locationId, String name, String address, Status status) {
        if (getLocationById(locationId) != null) {
            throw new IllegalArgumentException("Location ID already exists");
        }
        Location location = new Location(locationId, name, address, status);
        addLocation(location);
        return location;
    }

    // CRUD: Location (read)
    public Location getLocationById(String locationId) {
        return locations.stream()
                .filter(l -> l.getLocationId().equals(locationId))
                .findFirst()
                .orElse(null);
    }

    // CRUD: Location (update)
    public void updateLocationName(String locationId, String newName) {
        Location location = getLocationById(locationId);
        if (location != null) {
            location.setName(newName);
        }
    }

    public void updateLocationAddress(String locationId, String newAddress) {
        Location location = getLocationById(locationId);
        if (location != null) {
            location.setAddress(newAddress);
        }
    }

    public void updateLocationStatus(String locationId, Status newStatus) {
        Location location = getLocationById(locationId);
        if (location != null) {
            location.setStatus(newStatus);
        }
    }

    // CRUD: Location (delete)
    public void deleteLocationById(String locationId) {
        Location location = getLocationById(locationId);
        if (location != null) {
            removeLocation(location);
        }
    }

    // CRUD: ChargingStation (create)
    public ChargingStation createStation(ChargingStation chargingStation) {
        addStation(chargingStation);
        return chargingStation;
    }

    // CRUD: ChargingStation (read)
    public List<ChargingStation> listStations() {
        return chargingStations;
    }

    // CRUD: ChargingStation (update)
    public void updateStationState(String stationId, StationState newState) {
        ChargingStation station = getStationById(stationId);
        if (station != null) {
            station.setState(newState);
        }
    }

    public void updateStationType(String stationId, ChargingStationType newType) {
        ChargingStation station = getStationById(stationId);
        if (station != null) {
            station.setType(newType);
        }
    }

    // CRUD: ChargingStation (delete)
    public void deleteStationById(String stationId) {
        removeStationById(stationId);
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
        boolean hasStations = chargingStations.stream()
                .anyMatch(s -> s.getLocationId().equals(location.getLocationId()));
        if (hasStations) {
            throw new IllegalStateException("Cannot delete location with assigned stations");
        }
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
