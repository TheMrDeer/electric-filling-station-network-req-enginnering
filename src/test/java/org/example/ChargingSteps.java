package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
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
    private Location location;
    private String lastError;

    @Before
    public void setUp() {
        // Clearing the StationManager to ensure a clean state for each test scenario.
        StationManager.getInstance().clearAll();
    }

    @And("a location {string} exists with the following stations:")
    public void aLocationExistsWithTheFollowingStations(String arg0, DataTable datatable) {

        // Creating a location to host the stations defined in the data table.
        location = new Location("LOC-1", arg0, "", Status.Active);
        location.addLocation();

        List<Map<String, String>> rows = datatable.asMaps(String.class, String.class);


        for (Map<String, String> row : rows) {
            double pricePerMin = Double.parseDouble(row.get("PricePerMin"));
            double pricePerKwh = row.containsKey("PricePerKwh") ? Double.parseDouble(row.get("PricePerKwh")) : 0.0;
            ChargingStationType type = ChargingStationType.valueOf(row.get("Type"));

            // Create a Price object that is valid from the past so it applies now
            // Setting up pricing configuration for the location to support cost calculations.
            Price price = new Price(location.getLocationId(), type, pricePerMin, pricePerKwh, LocalDateTime.MIN, null);
            location.addPrice(price);

            ChargingStation station = new ChargingStation(
                    row.get("StationID"),
                    StationState.fromLabel(row.get("State")),
                    location.getLocationId(),
                    type,
                    price // Keep passing price for backward compatibility if needed, though Session uses Location price
            );
            // Adding the station to the system and the local map for quick lookup in tests.
            station.addChargingStation();
            stationsById.put(station.getStationID(), station);
        }
    }

    @When("I search for available charging stations")
    public void iSearchForAvailableChargingStations() {
        // Refreshing the local map of stations from the central manager to reflect current state.
        stationsById.clear();
        for (ChargingStation cs : StationManager.getInstance().getChargingStations()) {
            stationsById.put(cs.getStationID(), cs);
        }
    }

    @Then("I should see {string} with type {string} and state {string}")
    public void iShouldSeeWithTypeAndState(String stationID, String type, String state) {
        // Verifying that a specific station exists with the expected type and state.
        ChargingStation chargingStation = stationsById.get(stationID);
        assertEquals(ChargingStationType.valueOf(type), chargingStation.getType());
        assertEquals(StationState.fromLabel(state), chargingStation.getState());
    }

    @And("I should see {string} with type {string} and price {double}")
    public void iShouldSeeWithTypeAndPrice(String stationID, String type, double price) {
        ChargingStation chargingStation = stationsById.get(stationID);
        assertEquals(ChargingStationType.valueOf(type), chargingStation.getType());
        // Check against the location's current price for consistency, or the station's price if that's what's being tested
        // Since we updated the station with the price, this should still work.
        // Verifying the price associated with the station matches the expectation.
        assertEquals(price, chargingStation.getPrice().getRatePerMinute());
    }

    @And("I should see {string} with state {string}")
    public void iShouldSeeWithState(String stationId, String state) {
        // Checking the state of a specific station.
        ChargingStation chargingStation = stationsById.get(stationId);
        assertEquals(StationState.fromLabel(state), chargingStation.getState());
    }


    @Given("I am at station {string}")
    public void iAmAtStation(String stationId) {
        // Setting the context for subsequent actions to a specific station.
        currentStationId = stationId;
    }

    @And("the station {string} is {string}")
    public void theStationIs(String stationId, String state) {
        // Manually setting the station state to simulate conditions like "Occupied".
        ChargingStation cs = stationsById.get(stationId);
        cs.setState(StationState.fromLabel(state));
    }

    @When("I start a charging session with energy type {string}")
    public void iStartAChargingSessionWithEnergyType(String type) {
        // Initiating a new session for the current customer at the current station.
        session = new Session("SESSION-1", currentStationId, getCurrentCustomer());
        session.startSession();
    }

    @Then("the session should start successfully")
    public void theSessionShouldStartSuccessfully() {
        // Verifying that the session object is in an active state.
        assertTrue(session.isSessionActive());
        System.out.println("Session started successfully.");
    }

    @And("the station {string} state should change to {string}")
    public void theStationStateShouldChangeTo(String stationId, String state) {
        // Verifying that starting a session updates the station's state (e.g., to "Occupied").
        assertEquals(StationState.fromLabel(state), StationManager.getInstance().getStationById(stationId).getState());
    }

    @Given("I have an active charging session at {string}")
    public void iHaveAnActiveChargingSessionAt(String stationId) {
        // Pre-creating an active session to test mid-session or end-session logic.
        session = new Session("SESSION-1", stationId, getCurrentCustomer());
        session.startSession();
    }


    @And("the session has been running for {int} minutes")
    public void theSessionHasBeenRunningForMinutes(int arg0) {
        // Manually setting duration to simulate the passage of time for cost calculation.
        session.setDuration(arg0);
    }

    @And("the session charged {double} kWh")
    public void theSessionChargedKWh(double kwh) {
        // Setting the energy consumed to calculate energy-based costs.
        session.setChargedEnergy(kwh);
    }

    @When("I end the charging session")
    public void iEndTheChargingSession() {
        // Terminating the session to trigger final cost calculation and state updates.
        session.endSession();
    }

    @Then("_the station {string} state should change to {string}")
    public void _theStationStateShouldChangeTo(String arg0, String arg1) {//underline before, because of redundant methods
        // Verifying the station returns to "Free" (or other state) after session end.
        assertEquals(StationState.fromLabel(arg1), StationManager.getInstance().getStationById(arg0).getState());
    }


    @And("the cost of {double} should be deducted from my balance")
    public void theCostOfShouldBeDeductedFromMyBalance(double arg0) {
        // Verifying the calculated session cost matches the expected value.
        assertEquals(arg0,session.getTotalCost());
    }

    @And("my new balance should be {double}")
    public void myNewBalanceShouldBe(double arg0) {
        // Verifying the customer's balance was updated correctly.
        assertEquals(arg0, getCurrentCustomer().checkBalance());
    }

    @When("I attempt to start a charging session")
    public void iAttemptToStartAChargingSession() {
        try {
            // Attempting to start a session where failure is expected (e.g., station occupied).
            session = new Session("SESSION-1", currentStationId, getCurrentCustomer());
            session.startSession();
        } catch (IllegalStateException e) {
            // Capturing the error message.
            lastError = e.getMessage();
        }
    }

    @Then("I should receive a session error message {string}")
    public void iShouldReceiveASessionErrorMessage(String expectedMessage) {
        // Asserting the error message.
        assertEquals(expectedMessage, lastError);
    }

    private Customer getCurrentCustomer() {
        // Helper to retrieve the current test customer, creating a default one if needed.
        if (c1 == null && TestContext.currentCustomerId != null) {
            c1 = UserManager.getCustomerById(TestContext.currentCustomerId);
        }
        return c1;
    }
}
