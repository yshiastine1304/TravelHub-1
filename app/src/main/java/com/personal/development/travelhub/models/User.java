package com.personal.development.travelhub.models;

public class User {
    private String fullName;
    private String email;
    private String contactNumber;
    private String interest;
    private String travelStyle;

    // No-argument constructor required for Firestore serialization
    public User() {}

    public User(String fullName, String email, String contactNumber, String interest, String travelStyle) {
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.interest = interest;
        this.travelStyle = travelStyle;
    }

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getTravelStyle() {
        return travelStyle;
    }

    public void setTravelStyle(String travelStyle) {
        this.travelStyle = travelStyle;
    }
}
