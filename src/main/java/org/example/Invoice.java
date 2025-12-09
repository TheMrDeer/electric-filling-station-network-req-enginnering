package org.example;

import java.util.Date;

public class Invoice {

    private String invoiceId;
    private String sessionId;
    private double totalCost;
    private Date date;
    private String status;

    public Invoice(String invoiceId, String sessionId, double totalCost) {
        this.invoiceId = invoiceId;
        this.sessionId = sessionId;
        this.totalCost = totalCost;
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






