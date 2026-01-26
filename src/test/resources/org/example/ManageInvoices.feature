Feature: Manage Invoices
  As the Owner, I want to view all customer invoices to maintain an overview of charging sessions and revenues.

  Background:
    Given the E.Power system is initialized
    And the following invoices exist in the system:
      | InvoiceID | CustomerID | Location       | TotalAmount | Status |
      | INV-001   | User123    | Vienna Central | 15.00       | Paid   |
      | INV-002   | User456    | Graz North     | 22.50       | Paid   |

  Scenario: Get information of all invoices (Story #42)
    When I request a list of all invoices
    Then I should see 2 invoices in the list
    And the invoice "INV-001" should show a total amount of 15.00
    And the invoice "INV-002" should be associated with "User456"

  Scenario: Handle invoice request for non-existent customer (Edge Case)
    When I request an invoice for a non-existent customer "GhostUser"
    Then I should receive an invoice error message "Customer not found"
