Feature: User Authentication
  As a Customer, I want to manage my account registration and identity.

  Background:
    Given the E.Power system is initialized

  Scenario: Creation of an account (Story #43)
    When I register a new account with Customer Identity "User123"
    Then a Customer Account should be created for "User123"
    And the initial Balance of "User123" should be 0.00

  Scenario: Deletion of an account (Story #44)
    Given a customer "User123" exists
    When I delete the account for "User123"
    Then the customer "User123" should no longer be in the system