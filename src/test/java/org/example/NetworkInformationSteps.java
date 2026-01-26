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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NetworkInformationSteps {

    private List<Location> searchResults;
    private Location locationDetails;

    @When("I request the list of all Locations in the Charging Network")
    public void iRequestTheListOfAllLocationsInTheChargingNetwork() {
        // Retrieving all locations from the singleton manager to verify network visibility.
        searchResults = StationManager.getInstance().getLocations();
    }

    @Then("I should see {string} in the results")
    public void iShouldSeeInTheResults(String locationName) {
        // Using a stream to check if any location in the results matches the expected name.
        boolean found = searchResults.stream().anyMatch(l -> l.getName().equals(locationName));
        assertTrue(found, "Location " + locationName + " not found in results");
        System.out.println("Location found in results: " + locationName);
    }

    @And("I should see the Price Configuration for {string} with {double} for {string} and {double} for {string}")
    public void iShouldSeeThePriceConfigurationForWithForAndFor(String locationName, double price1, String type1, double price2, String type2) {
        Location location = StationManager.getInstance().findLocationByName(locationName);
        assertNotNull(location, "Location not found");

        // Delegating to a helper method to avoid code duplication for price checks.
        checkPriceForType(location, type1, price1);
        checkPriceForType(location, type2, price2);
    }

    private void checkPriceForType(Location location, String typeStr, double expectedPrice) {
        ChargingStationType type = ChargingStationType.valueOf(typeStr);
        // Fetching the price for the current time to ensure validity.
        Price price = location.getPriceFor(type, LocalDateTime.now());
        
        assertNotNull(price, "No price found for " + typeStr + " at " + location.getName());
        // Using a delta for double comparison to handle floating-point inaccuracies.
        assertEquals(expectedPrice, price.getPricePerMinute(), 0.001);
    }

    @And("I should see the State of Charging Station {string} as {string}")
    public void iShouldSeeTheStateOfChargingStationAs(String stationId, String stateStr) {
        ChargingStation station = StationManager.getInstance().getStationById(stationId);
        assertNotNull(station, "Station " + stationId + " not found");
        // Converting the string label to the enum type for comparison.
        assertEquals(StationState.fromLabel(stateStr), station.getState());
    }

    @When("I search for the Location {string}")
    public void iSearchForTheLocation(String locationName) {
        // Simulating a search action by name.
        locationDetails = StationManager.getInstance().findLocationByName(locationName);
    }

    @Then("I should receive the details for {string}")
    public void iShouldReceiveTheDetailsFor(String locationName) {
        assertNotNull(locationDetails, "Location details not found");
        assertEquals(locationName, locationDetails.getName());
    }

    @And("the details should include the Price Configuration of {double} for {string}")
    public void theDetailsShouldIncludeThePriceConfigurationOfFor(double price, String typeStr) {
        // Reusing the helper method to verify price details in the search result context.
        checkPriceForType(locationDetails, typeStr, price);
    }

    @Given("the network has the following stations at {string}:")
    public void theNetworkHasTheFollowingStationsAt(String locationName, DataTable table) {
        // Ensuring the location exists before adding stations to it.
        Location location = StationManager.getInstance().findLocationByName(locationName);
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
            
            // Creating and adding stations based on the data table to set up the test environment.
            ChargingStation station = new ChargingStation(id, state, location.getLocationId(), type, dummyPrice);
            station.addChargingStation();
        }
    }

    @Then("I should be informed that {string} does not exist")
    public void iShouldBeInformedThatDoesNotExist(String locationName) {
        // Verifying that the search result is null for a non-existent location.
        assertNull(locationDetails, "Expected location to be null, but found: " + locationDetails);
    }
}
