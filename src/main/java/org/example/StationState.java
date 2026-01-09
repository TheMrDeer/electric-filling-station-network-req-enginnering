package org.example;

import java.util.Arrays;

public enum StationState {
    inOperationFree("in operation free"),
    Occupied("occupied"),
    OutOfOrder("out of order");

    private final String label;

    StationState(String label) {
        this.label=label;
    }
    public static StationState fromLabel(String label) {
        return Arrays.stream(values())
                .filter(s -> s.label.equalsIgnoreCase(label))
                .findFirst()
                .orElseThrow();
    }
}
