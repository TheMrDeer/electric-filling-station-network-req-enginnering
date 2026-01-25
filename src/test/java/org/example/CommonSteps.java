package org.example;

import io.cucumber.java.en.Given;

public class CommonSteps {

    @Given("the E.Power system is initialized")
    public void theEPowerSystemIsInitialized() {
        StationManager.getInstance().clearAll();
        UserManager.clearUsers();
        TestContext.currentCustomerId = null;
        System.out.println("System initialized/reset for test scenario.");
    }
}
