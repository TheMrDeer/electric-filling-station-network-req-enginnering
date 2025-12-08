package org.example;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class ManageBalance {
    @Given("a customer {string} exists with a balance of {double}")
    public void aCustomerExistsWithABalanceOf(String arg0, int arg1, int arg2) {
    }

    @When("I check the available balance for {string}")
    public void iCheckTheAvailableBalanceFor(String arg0) {
    }

    @Then("I should see a balance of {double}")
    public void iShouldSeeABalanceOf(int arg0, int arg1) {
    }

    @When("I top up the balance by {double}")
    public void iTopUpTheBalanceBy(int arg0, int arg1) {
    }

    @Then("the payment process should be {string}")
    public void thePaymentProcessShouldBe(String arg0) {
    }

    @And("the new balance for {string} should be {double}")
    public void theNewBalanceForShouldBe(String arg0, int arg1, int arg2) {
    }
}
