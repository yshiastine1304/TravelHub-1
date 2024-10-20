package com.personal.development.travelhub.models;

public class User {
    private String fullName;
    private String email;
    private String contactNumber;
    private String interest;
    private String access;
    private String profilePictureLink;

    public User() {}

    public User(String fullName, String email, String contactNumber, String interest, String access, String profilePictureLink) {
        this.fullName = fullName;
        this.email = email;
        this.contactNumber = contactNumber;
        this.interest = interest;
        this.access = access;
        this.profilePictureLink = profilePictureLink;
    }

    public String getProfilePictureLink() {
        return profilePictureLink;
    }

    public void setProfilePictureLink(String profilePictureLink) {
        this.profilePictureLink = profilePictureLink;
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
