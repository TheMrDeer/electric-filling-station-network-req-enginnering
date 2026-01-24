Feature: Manage Charging Stations
  As the Owner, I want to configure the technical facilities.

  Background:
    Given a location named "Vienna Central" exists

  Scenario: Add charging stations to location (Story #9)
    When I add a charging station with ID "CS-001" to "Vienna Central"
    Then the station "CS-001" should be associated with "Vienna Central"

  Scenario: Delete charging stations of location (Story #40)
    Given a station "CS-001" exists at "Vienna Central"
    When I remove the charging station "CS-001" from "Vienna Central"
    Then the station "CS-001" should no longer exist at "Vienna Central"

  Scenario: Set charging station type (Story #10)
    Given a station "CS-001" exists at "Vienna Central"
    When I set the charging type of "CS-001" to "DC"
    Then the station "CS-001" should be identified as a "DC" station

  Scenario: Set charging station state (Story #11)
    Given a station "CS-001" exists
    When I set the state of "CS-001" to "occupied"
    Then the station "CS-001" status should be "occupied"

  Scenario: Get information of charging stations by type and state (Story #41)
    Given the following stations exist at "Vienna Central":
      | ID     | Type | State             |
      | CS-100 | AC   | in operation free |
      | CS-101 | AC   | occupied          |
      | CS-102 | DC   | in operation free |
    When I request a list of "AC" stations that are "in operation free"
    Then I should receive only station "CS-100"

  Scenario: Prevent adding a charging station with a duplicate ID (Edge Case)
    Given a station "CS-001" exists at "Vienna Central"
    When I attempt to add a charging station with ID "CS-001" to "Vienna Central"
    Then I should receive an error message "Station ID already exists"

  Scenario: Prevent adding a charging station to a non-existent location (Edge Case)
    When I attempt to add a charging station with ID "CS-999" to "Unknown Location"
    Then I should receive an error message "Location does not exist"
