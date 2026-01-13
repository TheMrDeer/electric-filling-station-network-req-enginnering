package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ManageChargingStationSteps {
    @When("I add a charging station with ID {string} to {string}")
    public void iAddAChargingStationWithIDTo(String stationId, String locationName) {
        StationManager.addStation(new ChargingStation(stationId,StationState.inOperationFree,locationName,ChargingStationType.AC,new Price()));
    }

    @Then("the station {string} should be associated with {string}")
    public void theStationShouldBeAssociatedWith(String stationId, String locationName) {
        assertEquals(locationName,StationManager.getStationById(stationId).getLocationId());
    }

    @Given("a station {string} exists at {string}")
    public void aStationExistsAt(String stationId, String locationName) {
        StationManager.removeStationById(stationId);
        StationManager.addStation(new ChargingStation(stationId,StationState.inOperationFree,locationName,ChargingStationType.AC,new Price()));
    }

    @When("I remove the charging station {string} from {string}")
    public void iRemoveTheChargingStationFrom(String stationId, String locationName) {
        StationManager.removeStationById(stationId);
    }

    @Then("the station {string} should no longer exist at {string}")
    public void theStationShouldNoLongerExistAt(String stationId, String locationName) {
        assertNull(StationManager.getStationById(stationId));
    }
    @When("I set the charging type of {string} to {string}")
    public void iSetTheChargingTypeOfTo(String stationId, String type) {
        StationManager.getStationById(stationId).setType(ChargingStationType.valueOf(type));
    }

    @Then("the station {string} should be identified as a {string} station")
    public void theStationShouldBeIdentifiedAsAStation(String stationId, String type) {
        assertEquals(ChargingStationType.valueOf(type),StationManager.getStationById(stationId).getType());
    }

    @Given("a station {string} exists")
    public void aStationExists(String stationId) {
        StationManager.removeStationById(stationId);
        StationManager.addStation(new ChargingStation(
                stationId,
                StationState.inOperationFree,
                "LOC-1",
                ChargingStationType.AC,
                new Price()
        ));
    }

    @When("I set the state of {string} to {string}")
    public void iSetTheStateOfTo(String stationId, String state) {
        StationManager.setStationState(stationId,StationState.fromLabel(state));
    }

    @Then("the station {string} status should be {string}")
    public void theStationStatusShouldBe(String stationId, String state) {
        assertEquals(StationManager.getStationById(stationId).getState(),StationState.fromLabel(state));
    }

    @Given("the following stations exist at {string}:")
    public void theFollowingStationsExistAt(String locationName, DataTable table) {
        StationManager.clearAll();
        Location location = new Location("LOC-1", locationName, "", Status.Active);
        location.addLocation();

        List<Map<String, String>> rows = table.asMaps(String.class, String.class);


        for (Map<String, String> row : rows) {
            ChargingStation station = new ChargingStation(
                    row.get("ID"),
                    StationState.fromLabel(row.get("State")),
                    location.getLocationId(),
                    ChargingStationType.valueOf(row.get("Type")),
                    new Price()
            );
            station.addChargingStation();;
        }
    }

    private List<ChargingStation> chargingStationsByTypeAndState = new ArrayList<>();

    @When("I request a list of {string} stations that are {string}")
    public void iRequestAListOfStationsThatAre(String type, String state) {
        ChargingStationType requestedType = ChargingStationType.valueOf(type.trim().toUpperCase());

        StationState requestedState = StationState.fromLabel(state);

        chargingStationsByTypeAndState =
                StationManager.getChargingStations().stream()
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
}
