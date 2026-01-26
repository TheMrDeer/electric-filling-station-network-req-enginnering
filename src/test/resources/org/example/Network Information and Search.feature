Feature: Network Information and Search
  As a Driver, I want to search for locations and see their details, including prices and availability.

  Background:
    Given the E.Power system is initialized
    And a location named "Vienna Central" exists
    And the network has the following stations at "Vienna Central":
      | Station ID | Type | Status            |
      | CS-101     | AC   | in operation free |
      | CS-102     | DC   | occupied          |
    And "Vienna Central" has the following prices:
      | Type | Price |
      | AC   | 0.30  |
      | DC   | 0.50  |

  Scenario: Search for a specific Location (Story #LocationSearch)
    When I search for the Location "Vienna Central"
    Then I should receive the details for "Vienna Central"
    And the details should include the Price Configuration of 0.30 for "AC"
    And I should see the State of Charging Station "CS-101" as "in operation free"

    And the details should include the Price Configuration of 0.50 for "DC"

  Scenario: Search for a non-existent location (Edge Case)
    When I search for the Location "Atlantis"
    Then I should be informed that "Atlantis" does not exist
