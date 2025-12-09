package org.example;

import java.util.ArrayList;
import java.util.List;

public class ChargingStationRepository {

    private static List<ChargingStation> chargingStations = new ArrayList<>();

    public void addStation(ChargingStation chargingStation) {
        chargingStations.add(chargingStation);
    }

    public void removeStation(ChargingStation chargingStation) {
        chargingStations.remove(chargingStation);
    }

    public void print() {
        for (ChargingStation cs : chargingStations) {
            cs.print();
        }
    }
}
