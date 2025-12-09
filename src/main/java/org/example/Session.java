package org.example;

import java.time.LocalDateTime;
import java.time.Duration;

public class Session {

    private String sessionId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private double duration;   // in Minuten
    private double totalCost;
    private String stationId;
    private User user;

    public Session(String sessionId, String stationId, User user) {
        this.sessionId = sessionId;
        this.stationId = stationId;
        this.user = user;
    }

    public void startSession(String stationId) {
        this.stationId = stationId;
        this.startTime = LocalDateTime.now();
    }

    public void endSession() {
        this.endTime = LocalDateTime.now();
        this.duration = Duration.between(startTime, endTime).toMinutes();
    }

    public double calculateCost(double ratePerMinute) {
        this.totalCost = duration * ratePerMinute;
        return totalCost;
    }

    public String getSessionId() {
        return sessionId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public double getDuration() {
        return duration;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public String getStationId() {
        return stationId;
    }

    public String getUserId() {
        return this.user.getUserId();
    }
}



