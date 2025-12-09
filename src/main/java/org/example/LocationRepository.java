package org.example;

import java.util.ArrayList;
import java.util.List;

public class LocationRepository {

    private static List<Location> locations = new ArrayList<>();

    public static void addLocation(Location location) {
        locations.add(location);
    }

    public static void removeLocation(Location location) {
        locations.remove(location);
    }

    public static void print() {
        for (Location l : locations) {
            l.print();
        }
    }
}
