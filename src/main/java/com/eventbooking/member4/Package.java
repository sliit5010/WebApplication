package com.eventbooking.member4;

import java.io.Serializable;

// OOP: Encapsulation - Making fields private and providing getters/setters
public class Package implements Serializable {
    private String id;
    private String name;
    private String eventType;
    private String details;
    private double price;

    public Package() {}

    public Package(String id, String name, String eventType, String details, double price) {
        this.id = id;
        this.name = name;
        this.eventType = eventType;
        this.details = details;
        this.price = price;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    
    @Override
    public String toString() {
        return id + "|" + name + "|" + eventType + "|" + details + "|" + price;
    }
    
    public static Package fromString(String line) {
        String[] parts = line.split("\\|");
        if (parts.length == 5) {
            return new Package(parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4]));
        }
        return null;
    }
}
