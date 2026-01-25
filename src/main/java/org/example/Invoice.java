package org.example;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Invoice {

    private String invoiceId;
    private Customer customer;
    private List<Session> sessions;
    private List<Double> topUps;
    private Date date;
    private String status;

    public Invoice(String invoiceId, Customer customer) {
        this.invoiceId = invoiceId;
        this.customer = customer;
        this.sessions = new ArrayList<>();
        this.topUps = new ArrayList<>();
        this.date = new Date();
        this.status = "CREATED";
    }

    public void addSession(Session session) {
        this.sessions.add(session);
    }

    public void addTopUp(double amount) {
        this.topUps.add(amount);
    }

    public String generateInvoice() {
        this.date = new Date();
        this.status = "GENERATED";
        return this.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Invoice ID: ").append(invoiceId).append("\n");
        sb.append("Customer: ").append(customer.getUserId()).append("\n");
        sb.append("Date: ").append(date).append("\n");
        sb.append("--------------------------------------------------------------------------------------------------------\n");
        // Updated columns as requested: Item # | Location | Station ID | Type | Duration | Energy (kWh) | Price
        sb.append(String.format("%-6s | %-20s | %-15s | %-5s | %-10s | %-12s | %-10s\n", 
                "Item #", "Location", "Station ID", "Type", "Duration", "Energy (kWh)", "Price"));
        sb.append("--------------------------------------------------------------------------------------------------------\n");
        
        // Sort sessions by start time
        sessions.sort(Comparator.comparing(Session::getStartTime));
        
        int count = 1;
        for (Session s : sessions) {
             sb.append(String.format("%-6d | %-20s | %-15s | %-5s | %-10d | %-12.2f | %-10.2f\n",
                     count++, 
                     s.getLocationName() != null ? s.getLocationName() : "Unknown",
                     s.getStationId(),
                     s.getStationType() != null ? s.getStationType() : "N/A",
                     (long)s.getDuration(),
                     s.getChargedEnergy(),
                     s.getTotalCost()));
        }

        sb.append("--------------------------------------------------------------------------------------------------------\n");
        
        if (!topUps.isEmpty()) {
            sb.append("Top-Ups:\n");
            for (Double amount : topUps) {
                sb.append(String.format(" + %.2f\n", amount));
            }
            sb.append("--------------------------------------------------------------------------------------------------------\n");
        }
        
        sb.append(String.format("Outstanding Balance: %.2f\n", customer.checkBalance()));
        
        return sb.toString();
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }
}
