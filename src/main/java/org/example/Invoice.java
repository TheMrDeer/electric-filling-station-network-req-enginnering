package org.example;

import java.util.Date;

public class Invoice {

    private String invoiceId;
    private String sessionId;
    private double totalCost;
    private double loadedEnergy; // in kWh
    private Date date;
    private String status;

    public Invoice(String invoiceId, String sessionId, double totalCost) {
        this(invoiceId, sessionId, totalCost, 0.0);
    }

    public Invoice(String invoiceId, String sessionId, double totalCost, double loadedEnergy) {
        this.invoiceId = invoiceId;
        this.sessionId = sessionId;
        this.totalCost = totalCost;
        this.loadedEnergy = loadedEnergy;
        this.date = new Date();
        this.status = "CREATED";
    }

    public void generateInvoice() {
        this.date = new Date();
        this.status = "GENERATED";
    }

    public String viewInvoice() {
        return "Invoice ID: " + invoiceId + "\n"
                + "Session ID: " + sessionId + "\n"
                + "Total Cost: " + totalCost + "\n"
                + "Loaded Energy: " + loadedEnergy + " kWh\n"
                + "Date: " + date + "\n"
                + "Status: " + status;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public double getLoadedEnergy() {
        return loadedEnergy;
    }

    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    // Setter für Status (z. B. PAID, CANCELLED)
    public void setStatus(String status) {
        this.status = status;
    }
}
