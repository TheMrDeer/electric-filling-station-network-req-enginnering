Feature: Manage Locations
  As the Owner (E.Power), I want to manage the infrastructure network.
# Network status evtl als eigenes Feature anlegen.
  Background:
    Given the E.Power system is initialized

  Scenario: Add new location to network (Story #8)
    When I add a new location named "Vienna Central"
    Then the location "Vienna Central" should be listed in the Charging Network

  Scenario: Delete location from network (Story #39)
    Given a location named "Old Station" exists in the network
    When I remove the location "Old Station"
    Then the location "Old Station" should no longer exist in the network