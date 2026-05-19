package com.weddingplanner.admin.model;

public class Photographer {
    private String id;
    private String fullName;
    private String contactNumber;
    private int experience;
    private double rate;
    private String specialization; // e.g., "Wedding Photographer", "Event Photographer"
    private String packageType;    // e.g., "Gold", "Silver", "Platinum"

    public Photographer() {}

    public Photographer(String id, String fullName, String contactNumber, int experience, double rate, String specialization, String packageType) {
        this.id = id;
        this.fullName = fullName;
        this.contactNumber = contactNumber;
        this.experience = experience;
        this.rate = rate;
        this.specialization = specialization;
        this.packageType = packageType;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public double getRate() { return rate; }
    public void setRate(double rate) { this.rate = rate; }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }

    public String getPackageType() { return packageType; }
    public void setPackageType(String packageType) { this.packageType = packageType; }
}
