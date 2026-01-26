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

    // Helper Methods
    private Location getOrCreateLocation(String locationName) {
        Location location = StationManager.getInstance().findLocationByName(locationName);
        if (location == null) {
            location = new Location("LOC-" + locationName.hashCode(), locationName, "Unknown Address", Status.Active);
            StationManager.getInstance().addLocation(location);
        }
        return location;
    }

    private void createAndAddStation(String stationId, String locationName, StationState state, ChargingStationType type) {
        StationManager.getInstance().removeStationById(stationId);
        Location location = getOrCreateLocation(locationName);
        ChargingStation station = new ChargingStation(
                stationId,
                state,
                location.getLocationId(),
                type,
                new Price()
        );
        StationManager.getInstance().addStation(station);
    }

    @Given("a location named {string} exists at {string} with a default price of {double}")
    public void setupLocationWithPrice(String locationName, String address, double priceVal) {
        Location loc = new Location("LOC-" + locationName.hashCode(), locationName, address, Status.Active);

        // Associate a price object so future stations inherit this policy.
        Price initialPrice = new Price(loc.getLocationId(), ChargingStationType.AC, priceVal, 0.0, LocalDateTime.MIN, null);

        // Register the location to enable lookup by name in subsequent steps.
        loc.addPrice(initialPrice);
        StationManager.getInstance().addLocation(loc);
    }

    @When("I add a charging station with ID {string} to {string}")
    public void iAddAChargingStationWithIDTo(String stationId, String locationName) {
        // Retrieve the location first to ensure the station is linked to a valid parent.
        Location location = StationManager.getInstance().findLocationByName(locationName);

        if (location == null) {
            throw new IllegalArgumentException("Location does not exist");
        }

        // Fetch the active pricing configuration from the location settings.
        Price activePrice = location.getPriceFor(ChargingStationType.AC, LocalDateTime.now());

        // Construct the full station object with dependencies to ensure a valid state.
        ChargingStation station = new ChargingStation(
                stationId,
                StationState.inOperationFree,
                location.getLocationId(),
                ChargingStationType.AC,
                activePrice 
        );
        StationManager.getInstance().addStation(station);
    }

    @Then("the station {string} should be associated with {string}")
    public void theStationShouldBeAssociatedWith(String stationId, String locationName) {
        // Resolve the location name to an ID to verify the relationship, as stations store the foreign key.
        Location location = StationManager.getInstance().findLocationByName(locationName);
        String expectedLocationId = (location != null) ? location.getLocationId() : locationName;
        assertEquals(expectedLocationId, StationManager.getInstance().getStationById(stationId).getLocationId());
    }

    @Given("a station {string} exists at {string}")
    public void aStationExistsAt(String stationId, String locationName) {
        createAndAddStation(stationId, locationName, StationState.inOperationFree, ChargingStationType.AC);
    }

    @When("I remove the charging station {string} from {string}")
    public void iRemoveTheChargingStationFrom(String stationId, String locationName) {
        // Use the unique ID for removal; the location name is for Gherkin readability only.
        StationManager.getInstance().removeStationById(stationId);
    }

    @Then("the station {string} should no longer exist at {string}")
    public void theStationShouldNoLongerExistAt(String stationId, String locationName) {
        // Confirm removal by ensuring the station cannot be retrieved from the manager.
        assertNull(StationManager.getInstance().getStationById(stationId));
    }
    @When("I set the charging type of {string} to {string}")
    public void iSetTheChargingTypeOfTo(String stationId, String type) {
        // Simulate a configuration change to verify dynamic property updates.
        StationManager.getInstance().getStationById(stationId).setType(ChargingStationType.valueOf(type));
    }

    @Then("the station {string} should be identified as a {string} station")
    public void theStationShouldBeIdentifiedAsAStation(String stationId, String type) {
        // Verify that the modification was correctly persisted.
        assertEquals(ChargingStationType.valueOf(type),StationManager.getInstance().getStationById(stationId).getType());
    }

    @Given("a station {string} exists")
    public void aStationExists(String stationId) {
        // Associate with a default or fallback location to ensure the station is valid.
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
        // Simulate an operational state change to test status tracking.
        StationManager.getInstance().setStationState(stationId,StationState.fromLabel(state));
    }

    @Then("the station {string} status should be {string}")
    public void theStationStatusShouldBe(String stationId, String state) {
        // Check that the system reflects the new state for accurate reporting.
        assertEquals(StationManager.getInstance().getStationById(stationId).getState(),StationState.fromLabel(state));
    }

    @Given("the following stations exist at {string}:")
    public void theFollowingStationsExistAt(String locationName, DataTable table) {
        // Ensure the parent location exists to prevent foreign key issues.
        getOrCreateLocation(locationName);

        List<Map<String, String>> rows = table.asMaps(String.class, String.class);

        // Iterate through the data table to efficiently bulk-create stations for complex scenarios.
        for (Map<String, String> row : rows) {
            createAndAddStation(
                    row.get("ID"),
                    locationName,
                    StationState.fromLabel(row.get("State")),
                    ChargingStationType.valueOf(row.get("Type"))
            );
        }
    }

    private List<ChargingStation> chargingStationsByTypeAndState = new ArrayList<>();

    @When("I request a list of {string} stations that are {string}")
    public void iRequestAListOfStationsThatAre(String type, String state) {
        ChargingStationType requestedType = ChargingStationType.valueOf(type.trim().toUpperCase());

        StationState requestedState = StationState.fromLabel(state);

        // Filter the station list to isolate matches and verify search logic.
        chargingStationsByTypeAndState =
                StationManager.getInstance().getChargingStations().stream()
                        .filter(cs -> cs.getType() == requestedType)
                        .filter(cs -> cs.getState() == requestedState)
                        .toList();
    }

    @Then("I should receive only station {string}")
    public void iShouldReceiveOnlyStation(String stationId) {
        // Verify the filter was precise enough to return the single expected item.
        assertEquals(1, chargingStationsByTypeAndState.size(),
                "Expected exactly one matching station");

        assertEquals(stationId,
                chargingStationsByTypeAndState.get(0).getStationID());
    }

    @When("I attempt to add a charging station with ID {string} to {string}")
    public void iAttemptToAddAChargingStationWithIDTo(String stationId, String locationName) {
        try {
            // Wrap in try-catch to capture the expected failure for verification.
            iAddAChargingStationWithIDTo(stationId, locationName);
        } catch (IllegalArgumentException e) {
            lastError = e.getMessage();
        }
    }

    @Then("I should receive an error message {string}")
    public void iShouldReceiveAnErrorMessage(String expectedError) {
        // Assert that the system provided the correct feedback for the failure.
        assertEquals(expectedError, lastError);
    }
}
