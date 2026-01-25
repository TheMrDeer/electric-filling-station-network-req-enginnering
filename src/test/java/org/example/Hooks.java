package org.example;

import io.cucumber.java.Before;

public class Hooks {

    @Before
    public void resetSystem() {
        StationManager.getInstance().clearAll();
        UserManager.clearUsers();
        System.out.println("System initialized/reset for test scenario.");
    }
}
