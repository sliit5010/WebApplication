package com.photo.model;

public class PhotographerDTO {
    private String id;
    private String name;
    private String contact;
    private int exp;
    private double price;
    private String type;
    private String specialty; // maps to weddingPackageType or eventType

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public int getExp() { return exp; }
    public void setExp(int exp) { this.exp = exp; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
}
