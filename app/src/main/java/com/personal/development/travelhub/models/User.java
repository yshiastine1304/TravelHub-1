package com.personal.development.travelhub.models;

public class User {
    private String fullName;
    private String email;
    private String contactNumber;
    private String interest;
    private String access;

    public User(String fullName, String email, String contactNumber, String interest, String access) {
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.interest = interest;
        this.access = access;
    }

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

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }
}
