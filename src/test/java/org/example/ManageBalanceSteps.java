package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManageBalanceSteps {

    private Customer customer;
    private double balance;
    private String lastError;

    @Given("a customer {string} exists with a balance of {double}")
    public void _aCustomerExistsWithABalanceOf(String customerId, double amount) {
        // Ensuring a customer exists with a specific balance. If they exist, reset them to ensure a clean state.
        customer = UserManager.getCustomerById(customerId);
        if (customer == null) {
            customer = new Customer(customerId, customerId, customerId + "@example.com", "password");
        } else {
            // Reset balance if user exists or recreate
            UserManager.removeUser(customer);
            customer = new Customer(customerId, customerId, customerId + "@example.com", "password");
        }
        UserManager.addUser(customer);
        if (amount > 0) {
            customer.rechargeAccount(amount);
        }
        // Setting the global test context to track the current user.
        TestContext.currentCustomerId = customerId;
    }

    @When("I check the available balance for {string}")
    public void iCheckTheAvailableBalanceFor(String customerId) {
        // Retrieving the balance for verification.
        balance = UserManager.getCustomerById(customerId).checkBalance();
    }

    @Then("I should see a balance of {double}")
    public void iShouldSeeABalanceOf(double amount) {
        // Verifying the retrieved balance matches the expectation.
        assertEquals(amount, balance);
        System.out.println("Balance checked: " + balance);
    }

    @When("I top up the balance by {double}")
    public void iTopUpTheBalanceBy(double amount) {
        // Adding funds to a hardcoded user (User124) as per specific test scenario requirements.
        UserManager.getCustomerById("User124").rechargeAccount(amount);
    }

    @Then("the new balance for {string} should be {double}")
    public void theNewBalanceForShouldBe(String customerId, double balance) {
        // Verifying the balance after a top-up operation.
        assertEquals(balance, UserManager.getCustomerById(customerId).checkBalance());
    }

    @When("I attempt to top up the balance by {double}")
    public void iAttemptToTopUpTheBalanceBy(double amount) {
        try {
            // Assuming the user is "User124" based on the scenario context or TestContext
            // The scenario says: Given a customer "User124"...
            // So we can use "User124" or TestContext.currentCustomerId
            // Attempting a top-up that might fail (e.g., negative amount) to test error handling.
            String id = TestContext.currentCustomerId != null ? TestContext.currentCustomerId : "User124";
            UserManager.getCustomerById(id).rechargeAccount(amount);
        } catch (IllegalArgumentException e) {
            // Capturing the exception message.
            lastError = e.getMessage();
        }
    }

    @Then("I should receive a balance error message {string}")
    public void iShouldReceiveABalanceErrorMessage(String expectedMessage) {
        // Asserting the error message.
        assertEquals(expectedMessage, lastError);
    }
}
