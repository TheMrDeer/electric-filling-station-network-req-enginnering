package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChargingSteps {

    private Map<String, ChargingStation> stationsById = new HashMap<>();
    private Customer c1;
    private String currentStationId;
    private Session session;

    @Given("the E.Power system is initialized")
    public void theEPowerSystemIsInitialized() {
    }

    @And("a location {string} exists with the following stations:")
    public void aLocationExistsWithTheFollowingStations(String arg0, DataTable datatable) {

        Location location = new Location("LOC-1", arg0, "", Status.Active);
        location.addLocation();

        List<Map<String, String>> rows = datatable.asMaps(String.class, String.class);


        for (Map<String, String> row : rows) {
            ChargingStation station = new ChargingStation(
                    row.get("StationID"),
                    StationState.fromLabel(row.get("State")),
                    location.getLocationId(),
                    ChargingStationType.valueOf(row.get("Type")),
                    new Price(Double.parseDouble(row.get("PricePerMin")))
            );
            station.addChargingStation();
            stationsById.put(station.getStationID(), station);
        }
    }

    @And("a customer {string} exists with a balance of {double}")
    public void aCustomerExistsWithABalanceOf(String arg0, double arg1) {
        c1 = new Customer("C1", arg0, "", "");
        c1.rechargeAccount(arg1);
    }


    @When("I search for available charging stations")
    public void iSearchForAvailableChargingStations() {
        stationsById.clear();
        for (ChargingStation cs : StationManager.getChargingStations()) {
            stationsById.put(cs.getStationID(), cs);
        }
    }

    @Then("I should see {string} with type {string} and state {string}")
    public void iShouldSeeWithTypeAndState(String stationID, String type, String state) {
        ChargingStation chargingStation = stationsById.get(stationID);
        assertEquals(ChargingStationType.valueOf(type), chargingStation.getType());
        assertEquals(StationState.fromLabel(state), chargingStation.getState());
    }

    @And("I should see {string} with type {string} and price {double}")
    public void iShouldSeeWithTypeAndPrice(String stationID, String type, double price) {
        ChargingStation chargingStation = stationsById.get(stationID);
        assertEquals(ChargingStationType.valueOf(type), chargingStation.getType());
        assertEquals(price, chargingStation.getPrice().getRatePerMinute());
    }

    @And("I should see {string} with state {string}")
    public void iShouldSeeWithState(String stationId, String state) {
        ChargingStation chargingStation = stationsById.get(stationId);
        assertEquals(StationState.fromLabel(state), chargingStation.getState());
    }


    @Given("I am at station {string}")
    public void iAmAtStation(String stationId) {
        currentStationId = stationId;
    }

    @And("the station {string} is {string}")
    public void theStationIs(String stationId, String state) {
        ChargingStation cs = stationsById.get(stationId);
        cs.setState(StationState.fromLabel(state));
    }

    @When("I start a charging session with energy type {string}")
    public void iStartAChargingSessionWithEnergyType(String type) {
        session = new Session("SESSION-1", currentStationId, c1);
        session.startSession();
    }

    @Then("the session should start successfully")
    public void theSessionShouldStartSuccessfully() {
        assertTrue(session.isSessionActive());
    }

    @And("the station {string} state should change to {string}")
    public void theStationStateShouldChangeTo(String stationId, String state) {
        assertEquals(StationState.fromLabel(state),StationManager.getStationById(stationId).getState());
    }

    @Given("I have an active charging session at {string}")
    public void iHaveAnActiveChargingSessionAt(String stationId) {
    }

    private int sessionDuration;
    @And("the session has been running for {int} minutes")
    public void theSessionHasBeenRunningForMinutes(int arg0) {
        sessionDuration = arg0;
    }

    @When("I end the charging session")
    public void iEndTheChargingSession() {

    }

    @And("the cost of {double} should be deducted from my balance")
    public void theCostOfShouldBeDeductedFromMyBalance(double arg0) {
    }

    @And("my new balance should be {double}")
    public void myNewBalanceShouldBe(double arg0) {
    }
}
