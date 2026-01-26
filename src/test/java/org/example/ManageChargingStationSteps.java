package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ManageChargingStationSteps {

    private String lastError;

    @Given("a location named {string} exists at {string} with a default price of {double}")
    public void setupLocationWithPrice(String locationName, String address, double priceVal) {
        Location loc = new Location("LOC-" + locationName.hashCode(), locationName, address, Status.Active);

        // 2. Instantiate Price with the  amount
        Price initialPrice = new Price(loc.getLocationId(), ChargingStationType.AC, priceVal, 0.0, LocalDateTime.MIN, null);

        // 3. Store them so they can be retrieved later
        loc.addPrice(initialPrice);
        StationManager.getInstance().addLocation(loc);
    }

    @When("I add a charging station with ID {string} to {string}")
    public void iAddAChargingStationWithIDTo(String stationId, String locationName) {
        // 1. Retrieve the Location
        Location location = StationManager.getInstance().findLocationByName(locationName);

        if (location == null) {
            throw new IllegalArgumentException("Location does not exist");
        }

        // 2. Retrieve the Price we created in the Background

        Price activePrice = location.getPriceFor(ChargingStationType.AC, LocalDateTime.now());

        // 3. Create the Station using the retrieved objects

        ChargingStation station = new ChargingStation(
                stationId,
                StationState.inOperationFree,
                location.getLocationId(),
                ChargingStationType.AC,
                activePrice // <--- usage of retrieved price
        );
        StationManager.getInstance().addStation(station);
    }

    @Then("the station {string} should be associated with {string}")
    public void theStationShouldBeAssociatedWith(String stationId, String locationName) {
        // We need to check against the location ID, but the test passes the name.
        // However, since we now store the ID in the station, we should look up the location by name to get its ID for comparison.
        // Resolving the location name to an ID to verify the foreign key relationship in the station.
        Location location = StationManager.getInstance().findLocationByName(locationName);
        String expectedLocationId = (location != null) ? location.getLocationId() : locationName;
        assertEquals(expectedLocationId, StationManager.getInstance().getStationById(stationId).getLocationId());
    }

    @Given("a station {string} exists at {string}")
    public void aStationExistsAt(String stationId, String locationName) {
        // Ensuring a clean state by removing any existing station with the same ID before creating a new one.
        StationManager.getInstance().removeStationById(stationId);
        Location location = StationManager.getInstance().findLocationByName(locationName);
        String locationId = (location != null) ? location.getLocationId() : locationName;
        StationManager.getInstance().addStation(new ChargingStation(stationId,StationState.inOperationFree,locationId,ChargingStationType.AC,new Price()));
    }

    @When("I remove the charging station {string} from {string}")
    public void iRemoveTheChargingStationFrom(String stationId, String locationName) {
        // Removing the station by ID. The location name is passed for context but not strictly needed for removal by ID.
        StationManager.getInstance().removeStationById(stationId);
    }

    @Then("the station {string} should no longer exist at {string}")
    public void theStationShouldNoLongerExistAt(String stationId, String locationName) {
        // Verifying the station is gone from the manager.
        assertNull(StationManager.getInstance().getStationById(stationId));
    }
    @When("I set the charging type of {string} to {string}")
    public void iSetTheChargingTypeOfTo(String stationId, String type) {
        // Updating the station type to test modification logic.
        StationManager.getInstance().getStationById(stationId).setType(ChargingStationType.valueOf(type));
    }

    @Then("the station {string} should be identified as a {string} station")
    public void theStationShouldBeIdentifiedAsAStation(String stationId, String type) {
        // Verifying the type update was successful.
        assertEquals(ChargingStationType.valueOf(type),StationManager.getInstance().getStationById(stationId).getType());
    }

    @Given("a station {string} exists")
    public void aStationExists(String stationId) {


        // Finding a valid location to associate the station with, defaulting to "LOC-1" if not found.
        Location location = StationManager.getInstance().findLocationByName("Vienna Central");
        String locationId = (location != null) ? location.getLocationId() : "LOC-1";

        StationManager.getInstance().addStation(new ChargingStation(
                stationId,
                StationState.inOperationFree,
                locationId,
                ChargingStationType.AC,
                new Price()
        ));
    }

    @When("I set the state of {string} to {string}")
    public void iSetTheStateOfTo(String stationId, String state) {
        // Updating the station state (e.g., to "Occupied" or "Broken").
        StationManager.getInstance().setStationState(stationId,StationState.fromLabel(state));
    }

    @Then("the station {string} status should be {string}")
    public void theStationStatusShouldBe(String stationId, String state) {
        // Verifying the state update.
        assertEquals(StationManager.getInstance().getStationById(stationId).getState(),StationState.fromLabel(state));
    }

    @Given("the following stations exist at {string}:")
    public void theFollowingStationsExistAt(String locationName, DataTable table) {
        // Clearing previous state to ensure test isolation.
        StationManager.getInstance().clearAll();
        Location location = new Location("LOC-1", locationName, "", Status.Active);


        List<Map<String, String>> rows = table.asMaps(String.class, String.class);


        for (Map<String, String> row : rows) {
            ChargingStation station = new ChargingStation(
                    row.get("ID"),
                    StationState.fromLabel(row.get("State")),
                    location.getLocationId(),
                    ChargingStationType.valueOf(row.get("Type")),
                    new Price()
            );
            // station.addChargingStation(); // This calls StationManager.addStation(this)
            // Since we cleared all, there are no duplicates.
            // And location exists.
            // Adding multiple stations from the data table to the manager.
            station.addChargingStation();
        }
    }

    private List<ChargingStation> chargingStationsByTypeAndState = new ArrayList<>();

    @When("I request a list of {string} stations that are {string}")
    public void iRequestAListOfStationsThatAre(String type, String state) {
        ChargingStationType requestedType = ChargingStationType.valueOf(type.trim().toUpperCase());

        StationState requestedState = StationState.fromLabel(state);

        // Using Java Streams to filter stations by both type and state.
        chargingStationsByTypeAndState =
                StationManager.getInstance().getChargingStations().stream()
                        .filter(cs -> cs.getType() == requestedType)
                        .filter(cs -> cs.getState() == requestedState)
                        .toList();
    }

    @Then("I should receive only station {string}")
    public void iShouldReceiveOnlyStation(String stationId) {
        // Verifying that the filter returned exactly one result and it matches the expected ID.
        assertEquals(1, chargingStationsByTypeAndState.size(),
                "Expected exactly one matching station");

        assertEquals(stationId,
                chargingStationsByTypeAndState.get(0).getStationID());
    }

    @When("I attempt to add a charging station with ID {string} to {string}")
    public void iAttemptToAddAChargingStationWithIDTo(String stationId, String locationName) {
        try {
            // Wrapping the add action in a try-catch to test error scenarios (e.g., invalid location).
            iAddAChargingStationWithIDTo(stationId, locationName);
        } catch (IllegalArgumentException e) {
            lastError = e.getMessage();
        }
    }

    @Then("I should receive an error message {string}")
    public void iShouldReceiveAnErrorMessage(String expectedError) {
        // Asserting the caught error message.
        assertEquals(expectedError, lastError);
    }
}
