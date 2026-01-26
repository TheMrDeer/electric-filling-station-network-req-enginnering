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
        // Clear StationManager for test isolation
        StationManager.getInstance().clearAll();
    }

    @And("a location {string} exists with the following stations:")
    public void aLocationExistsWithTheFollowingStations(String arg0, DataTable datatable) {

        // Create location for stations
        location = new Location("LOC-1", arg0, "", Status.Active);
        location.addLocation();

        List<Map<String, String>> rows = datatable.asMaps(String.class, String.class);


        for (Map<String, String> row : rows) {
            double pricePerMin = Double.parseDouble(row.get("PricePerMin"));
            double pricePerKwh = row.containsKey("PricePerKwh") ? Double.parseDouble(row.get("PricePerKwh")) : 0.0;
            ChargingStationType type = ChargingStationType.valueOf(row.get("Type"));

            // Create Price object valid from past
            // Setup pricing config
            Price price = new Price(location.getLocationId(), type, pricePerMin, pricePerKwh, LocalDateTime.MIN, null);
            location.addPrice(price);

            ChargingStation station = new ChargingStation(
                    row.get("StationID"),
                    StationState.fromLabel(row.get("State")),
                    location.getLocationId(),
                    type,
                    price // Keep passing price for backward compatibility if needed, though Session uses Location price
            );
            // Add station to system and local map
            station.addChargingStation();
            stationsById.put(station.getStationID(), station);
        }
    }

    @When("I search for available charging stations")
    public void iSearchForAvailableChargingStations() {
        // Refresh local map from central manager
        stationsById.clear();
        for (ChargingStation cs : StationManager.getInstance().getChargingStations()) {
            stationsById.put(cs.getStationID(), cs);
        }
    }

    @Then("I should see {string} with type {string} and state {string}")
    public void iShouldSeeWithTypeAndState(String stationID, String type, String state) {
        // Verify station exists with expected type and state
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
        // Verify station price matches expectation
        assertEquals(price, chargingStation.getPrice().getRatePerMinute());
    }

    @And("I should see {string} with state {string}")
    public void iShouldSeeWithState(String stationId, String state) {
        // Check station state
        ChargingStation chargingStation = stationsById.get(stationId);
        assertEquals(StationState.fromLabel(state), chargingStation.getState());
    }


    @Given("I am at station {string}")
    public void iAmAtStation(String stationId) {
        // Set context to specific station
        currentStationId = stationId;
    }

    @And("the station {string} is {string}")
    public void theStationIs(String stationId, String state) {
        // Manually set station state
        ChargingStation cs = stationsById.get(stationId);
        cs.setState(StationState.fromLabel(state));
    }

    @When("I start a charging session with energy type {string}")
    public void iStartAChargingSessionWithEnergyType(String type) {
        // Initiate new session
        session = new Session("SESSION-1", currentStationId, getCurrentCustomer());
        session.startSession();
    }

    @Then("the session should start successfully")
    public void theSessionShouldStartSuccessfully() {
        // Verify session is active
        assertTrue(session.isSessionActive());
        System.out.println("Session started successfully.");
    }

    @And("the station {string} state should change to {string}")
    public void theStationStateShouldChangeTo(String stationId, String state) {
        // Verify station state update on session start
        assertEquals(StationState.fromLabel(state), StationManager.getInstance().getStationById(stationId).getState());
    }

    @Given("I have an active charging session at {string}")
    public void iHaveAnActiveChargingSessionAt(String stationId) {
        // Pre-create active session
        session = new Session("SESSION-1", stationId, getCurrentCustomer());
        session.startSession();
    }


    @And("the session has been running for {int} minutes")
    public void theSessionHasBeenRunningForMinutes(int arg0) {
        // Manually set duration for cost calculation
        session.setDuration(arg0);
    }

    @And("the session charged {double} kWh")
    public void theSessionChargedKWh(double kwh) {
        // Set energy consumed
        session.setChargedEnergy(kwh);
    }

    @When("I end the charging session")
    public void iEndTheChargingSession() {
        // Terminate session
        session.endSession();
    }

    @Then("_the station {string} state should change to {string}")
    public void _theStationStateShouldChangeTo(String arg0, String arg1) {//underline before, because of redundant methods
        // Verify station returns to expected state
        assertEquals(StationState.fromLabel(arg1), StationManager.getInstance().getStationById(arg0).getState());
    }


    @And("the cost of {double} should be deducted from my balance")
    public void theCostOfShouldBeDeductedFromMyBalance(double arg0) {
        // Verify calculated session cost
        assertEquals(arg0,session.getTotalCost());
    }

    @And("my new balance should be {double}")
    public void myNewBalanceShouldBe(double arg0) {
        // Verify customer balance update
        assertEquals(arg0, getCurrentCustomer().checkBalance());
    }

    @When("I attempt to start a charging session")
    public void iAttemptToStartAChargingSession() {
        try {
            // Attempt session start expected to fail
            session = new Session("SESSION-1", currentStationId, getCurrentCustomer());
            session.startSession();
        } catch (IllegalStateException e) {
            // Capture error message
            lastError = e.getMessage();
        }
    }

    @Then("I should receive a session error message {string}")
    public void iShouldReceiveASessionErrorMessage(String expectedMessage) {
        // Assert error message
        assertEquals(expectedMessage, lastError);
    }

    private Customer getCurrentCustomer() {
        // Helper to retrieve or create test customer
        if (c1 == null && TestContext.currentCustomerId != null) {
            c1 = UserManager.getCustomerById(TestContext.currentCustomerId);
        }
        return c1;
    }
}
