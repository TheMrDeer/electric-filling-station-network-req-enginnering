package org.example;

import io.cucumber.java.en.Given;

public class CommonSteps {

    @Given("the E.Power system is initialized")
    public void theEPowerSystemIsInitialized() {
        System.out.println("System initialized/reset for test scenario.");
    }
}
