package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ManageChargingStationSteps {

    private Exception lastException;
    private List<ChargingStation> lastSearchResults;

    @When("I add a charging station with ID {string} to {string}")
    public void iAddAChargingStationWithIDTo(String stationId, String locationName) {
        Location location = StationManager.getInstance().findLocationByName(locationName);
        // If location is null, we pass a dummy ID to trigger "Location does not exist" in Manager.
        String locationId = (location != null) ? location.getLocationId() : "NON_EXISTENT_LOC";

        ChargingStation station = new ChargingStation(
                stationId,
                StationState.inOperationFree,
                locationId,
                ChargingStationType.AC,
                new Price()
        );
        StationManager.getInstance().addStation(station);
    }

    @Then("the station {string} should be associated with {string}")
    public void theStationShouldBeAssociatedWith(String stationId, String locationName) {
        ChargingStation station = StationManager.getInstance().getStationById(stationId);
        Assertions.assertNotNull(station, "Station should exist in the manager");

        Location location = StationManager.getInstance().findLocationByName(locationName);
        Assertions.assertNotNull(location, "Location should exist");

        Assertions.assertEquals(location.getLocationId(), station.getLocationId(),
                "Station should be associated with the correct location ID");
    }

    @Given("a station {string} exists at {string}")
    public void aStationExistsAt(String stationId, String locationName) {
        // Ensure clean state for this station ID
        StationManager.getInstance().removeStationById(stationId);

        Location location = StationManager.getInstance().findLocationByName(locationName);
        if (location == null) {
            // Create location if it doesn't exist (helper for setup)
            location = new Location("LOC-" + locationName.hashCode(), locationName, "Address", Status.Active);
            StationManager.getInstance().addLocation(location);
        }

        ChargingStation station = new ChargingStation(
                stationId,
                StationState.inOperationFree,
                location.getLocationId(),
                ChargingStationType.AC,
                new Price()
        );
        StationManager.getInstance().addStation(station);
    }

    @When("I remove the charging station {string} from {string}")
    public void iRemoveTheChargingStationFrom(String stationId, String locationName) {
        StationManager.getInstance().removeStationById(stationId);
    }

    @Then("the station {string} should no longer exist at {string}")
    public void theStationShouldNoLongerExistAt(String stationId, String locationName) {
        ChargingStation station = StationManager.getInstance().getStationById(stationId);
        Assertions.assertNull(station, "Station should be removed from the manager");
    }

    @When("I set the charging type of {string} to {string}")
    public void iSetTheChargingTypeOfTo(String stationId, String type) {
        ChargingStation station = StationManager.getInstance().getStationById(stationId);
        Assertions.assertNotNull(station, "Station must exist to set type");
        station.setType(ChargingStationType.valueOf(type));
    }

    @Then("the station {string} should be identified as a {string} station")
    public void theStationShouldBeIdentifiedAsAStation(String stationId, String type) {
        ChargingStation station = StationManager.getInstance().getStationById(stationId);
        Assertions.assertNotNull(station, "Station must exist");
        Assertions.assertEquals(ChargingStationType.valueOf(type), station.getType());
    }

    @Given("a station {string} exists")
    public void aStationExists(String stationId) {
        String defaultLocName = "Vienna Central";
        aStationExistsAt(stationId, defaultLocName);
    }

    @When("I set the state of {string} to {string}")
    public void iSetTheStateOfTo(String stationId, String state) {
        StationManager.getInstance().setStationState(stationId, StationState.fromLabel(state));
    }

    @Then("the station {string} status should be {string}")
    public void theStationStatusShouldBe(String stationId, String state) {
        ChargingStation station = StationManager.getInstance().getStationById(stationId);
        Assertions.assertNotNull(station, "Station must exist");
        Assertions.assertEquals(StationState.fromLabel(state), station.getState());
    }

    @Given("the following stations exist at {string}:")
    public void theFollowingStationsExistAt(String locationName, DataTable table) {
        Location location = StationManager.getInstance().findLocationByName(locationName);
        if (location == null) {
            location = new Location("LOC-" + locationName.hashCode(), locationName, "Address", Status.Active);
            StationManager.getInstance().addLocation(location);
        }

        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String id = row.get("ID");
            StationManager.getInstance().removeStationById(id);

            ChargingStation station = new ChargingStation(
                    id,
                    StationState.fromLabel(row.get("State")),
                    location.getLocationId(),
                    ChargingStationType.valueOf(row.get("Type")),
                    new Price()
            );
            StationManager.getInstance().addStation(station);
        }
    }

    @When("I request a list of {string} stations that are {string}")
    public void iRequestAListOfStationsThatAre(String type, String state) {
        ChargingStationType requestedType = ChargingStationType.valueOf(type);
        StationState requestedState = StationState.fromLabel(state);

        lastSearchResults = StationManager.getInstance().getChargingStations().stream()
                .filter(cs -> cs.getType() == requestedType)
                .filter(cs -> cs.getState() == requestedState)
                .collect(Collectors.toList());
    }

    @Then("I should receive only station {string}")
    public void iShouldReceiveOnlyStation(String stationId) {
        Assertions.assertNotNull(lastSearchResults, "Search results should not be null");
        Assertions.assertEquals(1, lastSearchResults.size(), "Expected exactly one matching station");
        Assertions.assertEquals(stationId, lastSearchResults.get(0).getStationID());
    }

    @When("I attempt to add a charging station with ID {string} to {string}")
    public void iAttemptToAddAChargingStationWithIDTo(String stationId, String locationName) {
        try {
            iAddAChargingStationWithIDTo(stationId, locationName);
            lastException = null;
        } catch (Exception e) {
            lastException = e;
        }
    }

    @Then("I should receive an error message {string}")
    public void iShouldReceiveAnErrorMessage(String expectedError) {
        Assertions.assertNotNull(lastException, "Expected an exception but none was thrown");
        Assertions.assertEquals(expectedError, lastException.getMessage());
    }
}
