package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

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
        
        ChargingStation station = StationManager.getChargingStations().stream()
                .filter(s -> s.getLocationId().equals(location.getLocationId()) && s.getType() == type)
                .findFirst()
                .orElse(null);

        assertNotNull(station, "No station of type " + typeStr + " found in " + location.getName());
        assertEquals(expectedPrice, station.getPrice().getRatePerMinute(), 0.001);
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
}
