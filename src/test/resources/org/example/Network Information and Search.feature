Feature: Network Information and Search
  As a Customer or Owner, I want to access the Charging Network to view the State of Charging Stations and Price Configuration for each Location.

  Background:
    Given the E.Power system is initialized
    And a location named "Vienna Central" exists
    And the following stations exist at "Vienna Central":
      | ID     | Type | State             |
      | CS-101 | AC   | in operation free |
      | CS-102 | DC   | occupied          |
    And "Vienna Central" has the following prices:
      | Type | Price |
      | AC   | 0.30  |
      | DC   | 0.50  |

  Scenario: View the current status of the Charging Network (Story #NetworkStatus)
    When I request the list of all Locations in the Charging Network
    Then I should see "Vienna Central" in the results
    And I should see the Price Configuration for "Vienna Central" with 0.30 for "AC" and 0.50 for "DC"
    And I should see the State of Charging Station "CS-101" as "in operation free"
    And I should see the State of Charging Station "CS-102" as "occupied"

  Scenario: Search for a specific Location (Story #LocationSearch)
    When I search for the Location "Vienna Central"
    Then I should receive the details for "Vienna Central"
    And the details should include the Price Configuration of 0.30 for "AC"
    And the details should include the Price Configuration of 0.50 for "DC"