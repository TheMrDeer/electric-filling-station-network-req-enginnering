package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NetworkInformationSteps {

    private List<Location> searchResults;
    private Location locationDetails;

    @When("I request the list of all Locations in the Charging Network")
    public void iRequestTheListOfAllLocationsInTheChargingNetwork() {
        searchResults = StationManager.getLocations();
    }

    @Then("I should see {string} in the results")
    public void iShouldSeeInTheResults(String locationName) {
        boolean found = searchResults.stream().anyMatch(l -> l.getName().equals(locationName));
        assertTrue(found, "Location " + locationName + " not found in results");
        System.out.println("Location found in results: " + locationName);
    }

    @And("I should see the Price Configuration for {string} with {double} for {string} and {double} for {string}")
    public void iShouldSeeThePriceConfigurationForWithForAndFor(String locationName, double price1, String type1, double price2, String type2) {
        Location location = StationManager.findLocationByName(locationName);
        assertNotNull(location, "Location not found");

        checkPriceForType(location, type1, price1);
        checkPriceForType(location, type2, price2);
    }

    private void checkPriceForType(Location location, String typeStr, double expectedPrice) {
        ChargingStationType type = ChargingStationType.valueOf(typeStr);
        Price price = location.getPriceFor(type, LocalDateTime.now());
        
        assertNotNull(price, "No price found for " + typeStr + " at " + location.getName());
        assertEquals(expectedPrice, price.getPricePerMinute(), 0.001);
    }

    @And("I should see the State of Charging Station {string} as {string}")
    public void iShouldSeeTheStateOfChargingStationAs(String stationId, String stateStr) {
        ChargingStation station = StationManager.getStationById(stationId);
        assertNotNull(station, "Station " + stationId + " not found");
        assertEquals(StationState.fromLabel(stateStr), station.getState());
    }

    @When("I search for the Location {string}")
    public void iSearchForTheLocation(String locationName) {
        locationDetails = StationManager.findLocationByName(locationName);
    }

    @Then("I should receive the details for {string}")
    public void iShouldReceiveTheDetailsFor(String locationName) {
        assertNotNull(locationDetails, "Location details not found");
        assertEquals(locationName, locationDetails.getName());
    }

    @And("the details should include the Price Configuration of {double} for {string}")
    public void theDetailsShouldIncludeThePriceConfigurationOfFor(double price, String typeStr) {
        checkPriceForType(locationDetails, typeStr, price);
    }

    @Given("the network has the following stations at {string}:")
    public void theNetworkHasTheFollowingStationsAt(String locationName, DataTable table) {
        Location location = StationManager.findLocationByName(locationName);
        if (location == null) {
            location = new Location("LOC-" + locationName.hashCode(), locationName, "Address", Status.Active);
        }

        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String id = row.get("Station ID");
            String typeStr = row.get("Type");
            String statusStr = row.get("Status");
            
            ChargingStationType type = ChargingStationType.valueOf(typeStr);
            StationState state = StationState.fromLabel(statusStr);
            
            Price dummyPrice = new Price(); 
            
            ChargingStation station = new ChargingStation(id, state, location.getLocationId(), type, dummyPrice);
            station.addChargingStation();
        }
    }
}
