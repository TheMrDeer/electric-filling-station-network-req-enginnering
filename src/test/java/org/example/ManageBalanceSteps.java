package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ManageBalanceSteps {

    private Customer customer;
    private double balance;

    @Given("a customer {string} exists with a balance of {double}")
    public void aCustomerExistsWithABalanceOf(String customerId, double amount) {
        customer = new Customer(customerId, "", "", "");
        UserManager.getCustomerById(customerId).rechargeAccount(amount);
    }

    @When("I check the available balance for {string}")
    public void iCheckTheAvailableBalanceFor(String customerId) {
        balance = UserManager.getCustomerById(customerId).checkBalance();
    }

    @Then("I should see a balance of {double}")
    public void iShouldSeeABalanceOf(double amount) {
        assertEquals(amount, balance);
    }

    @Given("a customer {string} exists with a balance of {double}")
    public void _aCustomerExistsWithABalanceOf(String customerId, double amount) {
        customer = new Customer(customerId,"","","");
        UserManager.getCustomerById(customerId).rechargeAccount(amount);
    }

    @When("I top up the balance by {double}")
    public void iTopUpTheBalanceBy(double amount) {
        UserManager.getCustomerById("User124").rechargeAccount(amount);
    }

    @Then("the new balance for {string} should be {double}")
    public void theNewBalanceForShouldBe(String customerId, double balance) {
        assertEquals(balance,customer.checkBalance());
    }
}
