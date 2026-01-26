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
        // 1. Instantiate Location with the visible address
        Location loc = new Location("LOC-" + locationName.hashCode(), locationName, address, Status.Active);

        // 2. Instantiate Price with the visible amount
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
        // (This ensures we use the 0.30 defined in the feature file, not a hardcoded value)
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
        Location location = StationManager.getInstance().findLocationByName(locationName);
        String expectedLocationId = (location != null) ? location.getLocationId() : locationName;
        assertEquals(expectedLocationId, StationManager.getInstance().getStationById(stationId).getLocationId());
    }

    @Given("a station {string} exists at {string}")
    public void aStationExistsAt(String stationId, String locationName) {
        StationManager.getInstance().removeStationById(stationId);
        Location location = StationManager.getInstance().findLocationByName(locationName);
        String locationId = (location != null) ? location.getLocationId() : locationName;
        StationManager.getInstance().addStation(new ChargingStation(stationId,StationState.inOperationFree,locationId,ChargingStationType.AC,new Price()));
    }

    @When("I remove the charging station {string} from {string}")
    public void iRemoveTheChargingStationFrom(String stationId, String locationName) {
        StationManager.getInstance().removeStationById(stationId);
    }

    @Then("the station {string} should no longer exist at {string}")
    public void theStationShouldNoLongerExistAt(String stationId, String locationName) {
        assertNull(StationManager.getInstance().getStationById(stationId));
    }
    @When("I set the charging type of {string} to {string}")
    public void iSetTheChargingTypeOfTo(String stationId, String type) {
        StationManager.getInstance().getStationById(stationId).setType(ChargingStationType.valueOf(type));
    }

    @Then("the station {string} should be identified as a {string} station")
    public void theStationShouldBeIdentifiedAsAStation(String stationId, String type) {
        assertEquals(ChargingStationType.valueOf(type),StationManager.getInstance().getStationById(stationId).getType());
    }

    @Given("a station {string} exists")
    public void aStationExists(String stationId) {
        StationManager.getInstance().removeStationById(stationId);
        // We need a valid location for this to work with the new validation.
        // Assuming "LOC-1" exists from Background or we create a dummy one.
        // But this step is used in "Set charging station state" scenario which has "Background: Given a location named "Vienna Central" exists".
        // So we can use "Vienna Central".
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
        StationManager.getInstance().setStationState(stationId,StationState.fromLabel(state));
    }

    @Then("the station {string} status should be {string}")
    public void theStationStatusShouldBe(String stationId, String state) {
        assertEquals(StationManager.getInstance().getStationById(stationId).getState(),StationState.fromLabel(state));
    }

    @Given("the following stations exist at {string}:")
    public void theFollowingStationsExistAt(String locationName, DataTable table) {
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
            station.addChargingStation();
        }
    }

    private List<ChargingStation> chargingStationsByTypeAndState = new ArrayList<>();

    @When("I request a list of {string} stations that are {string}")
    public void iRequestAListOfStationsThatAre(String type, String state) {
        ChargingStationType requestedType = ChargingStationType.valueOf(type.trim().toUpperCase());

        StationState requestedState = StationState.fromLabel(state);

        chargingStationsByTypeAndState =
                StationManager.getInstance().getChargingStations().stream()
                        .filter(cs -> cs.getType() == requestedType)
                        .filter(cs -> cs.getState() == requestedState)
                        .toList();
    }

    @Then("I should receive only station {string}")
    public void iShouldReceiveOnlyStation(String stationId) {
        assertEquals(1, chargingStationsByTypeAndState.size(),
                "Expected exactly one matching station");

        assertEquals(stationId,
                chargingStationsByTypeAndState.get(0).getStationID());
    }

    @When("I attempt to add a charging station with ID {string} to {string}")
    public void iAttemptToAddAChargingStationWithIDTo(String stationId, String locationName) {
        try {
            iAddAChargingStationWithIDTo(stationId, locationName);
        } catch (IllegalArgumentException e) {
            lastError = e.getMessage();
        }
    }

    @Then("I should receive an error message {string}")
    public void iShouldReceiveAnErrorMessage(String expectedError) {
        assertEquals(expectedError, lastError);
    }
}
