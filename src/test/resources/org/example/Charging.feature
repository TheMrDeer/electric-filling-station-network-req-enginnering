Feature: Charging
  As a Customer, I want to find charging stations, start a session, and end it successfully.

  Background:
    Given the E.Power system is initialized
    And a location "Vienna Central" exists with the following stations:
      | StationID | Type | State             | PricePerMin | PricePerKwh |
      | CS-101    | AC   | in operation free | 0.30        | 0.20        |
      | CS-102    | DC   | occupied          | 0.50        | 0.40        |
      | CS-103    | DC   | in operation free | 0.50        | 0.40        |
    And a customer "User123" exists with a balance of 50.00

  Scenario: Show overview of charging stations (Story #48)
    When I search for available charging stations
    Then I should see "CS-101" with type "AC" and state "in operation free"
    And I should see "CS-103" with type "DC" and price 0.50
    And I should see "CS-102" with state "occupied"

  Scenario: Start charging session (Story #49)
    Given I am at station "CS-103"
    And the station "CS-103" is "in operation free"
    When I start a charging session with energy type "DC"
    Then the session should start successfully
    And the station "CS-103" state should change to "occupied"

  Scenario: End charging session (Story #50)
    Given I have an active charging session at "CS-103"
    And the session has been running for 30 minutes
    And the session charged 20.0 kWh
    When I end the charging session
    Then _the station "CS-103" state should change to "in operation free"
    And the cost of 23.00 should be deducted from my balance
    And my new balance should be 27.00

  Scenario: Prevent starting a session at an occupied station (Edge Case)
    Given I am at station "CS-102"
    And the station "CS-102" is "occupied"
    When I attempt to start a charging session
    Then I should receive a session error message "Station is currently occupied"
