package org.example;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;


import java.util.HashMap;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConfigureLocationPrices {

    private final Map<String, Location> locationsByName = new HashMap<>();

    private Map<String, Double> viewedPrices = new HashMap<>();

    private ChargingStationType parseType(String type) {
        return ChargingStationType.valueOf(type.trim().toUpperCase());
    }
    @Given("a location named {string} exists")
    public void aLocationNamedExists(String arg0) {
        locationsByName.clear();
        viewedPrices.clear();
        StationManager.clearAll();
        Location location = new Location("LOC-1", arg0, "", Status.Active);
        locationsByName.put(arg0, location);
    }

    @When("I set the price for {string} charging at {string} to {double} per minute")
    public void iSetThePriceForChargingAtToPerMinute(String type, String locationName, double ratePerMinute) {
        ChargingStation station = getOrCreateStation(locationName, type);
        station.setPrice(new Price(ratePerMinute));
    }

    @Then("the price configuration for {string} at {string} should be {double}")
    public void thePriceConfigurationForAtShouldBe(String type, String locationName, double ratePerMinute) {
        ChargingStation station = getOrCreateStation(locationName, type);
        assertEquals(ratePerMinute, station.getPrice().getRatePerMinute());
    }

    @Given("{string} has the following prices:")
    public void hasTheFollowingPrices(String locationName, DataTable table) {
        viewedPrices.clear();
        locationsByName.clear();
        StationManager.clearAll();
        ensureLocationExists(locationName);

        List<Map<String, String>> rows = table.asMaps(String.class, String.class);
        for (Map<String, String> row : rows) {
            String type = row.get("Type");
            double price = Double.parseDouble(row.get("Price"));
            ChargingStation station = getOrCreateStation(locationName, type);
            station.setPrice(new Price(price));
        }
    }

    @When("I view prices for {string}")
    public void iViewPricesFor(String locationName) {
        viewedPrices = new HashMap<>();
        Location location = ensureLocationExists(locationName);
        for (ChargingStation station : StationManager.getChargingStations()) {
            if (station.getLocationId().equals(location.getLocationId())) {
                viewedPrices.put(station.getType().name(), station.getPrice().getRatePerMinute());
            }
        }
    }

    @Then("I should see {double} for {string} and {double} for {string}")
    public void iShouldSeeForAndFor(double price1, String type1, double price2, String type2) {
        assertEquals(price1, viewedPrices.get(type1.trim().toUpperCase()));
        assertEquals(price2, viewedPrices.get(type2.trim().toUpperCase()));
    }

    private ChargingStation getOrCreateStation(String locationName, String type) {
        Location location = ensureLocationExists(locationName);
        ChargingStationType stationType = parseType(type);
        for (ChargingStation station : StationManager.getChargingStations()) {
            if (station.getLocationId().equals(location.getLocationId())
                    && station.getType() == stationType) {
                return station;
            }
        }
        ChargingStation station = new ChargingStation(
                location.getLocationId() + "-" + stationType.name(),
                StationState.inOperationFree,
                location.getLocationId(),
                stationType,
                new Price()
        );
        station.addChargingStation();
        return station;
    }

    private Location ensureLocationExists(String locationName) {
        return locationsByName.computeIfAbsent(locationName,
                name -> new Location("LOC-" + (locationsByName.size() + 1), name, "", Status.Active));
    }
}
