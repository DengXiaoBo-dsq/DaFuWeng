package com.dsq.DaFuWeng.model;

import java.io.Serializable;
import java.util.Date;

public class LotteryActivity implements Serializable {
    private String id;
    private String name;
    private String description;
    private Date startTime;
    private Date endTime;
    private double price;
    private int maxParticipants;
    private int currentParticipants;
    private boolean isActive;
    private String winnerId;
    private String prize;

    public LotteryActivity() {
    }

    public LotteryActivity(String id, String name, String description, Date startTime, Date endTime, double price, int maxParticipants, int currentParticipants, boolean isActive, String winnerId, String prize) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.price = price;
        this.maxParticipants = maxParticipants;
        this.currentParticipants = currentParticipants;
        this.isActive = isActive;
        this.winnerId = winnerId;
        this.prize = prize;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public int getCurrentParticipants() {
        return currentParticipants;
    }

    public void setCurrentParticipants(int currentParticipants) {
        this.currentParticipants = currentParticipants;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getWinnerId() {
        return winnerId;
    }

    public void setWinnerId(String winnerId) {
        this.winnerId = winnerId;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }
}    