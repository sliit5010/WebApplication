package com.eventbooking.model;

public abstract class Photographer {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String specialization;

    public Photographer(String id, String name, String email, String phone, String specialization) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.specialization = specialization;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getSpecialization() { return specialization; }

    // Format for saving to photographers.txt
    public String toFileFormat() {
        return id + "," + name + "," + email + "," + phone + "," + specialization;
    }
}
