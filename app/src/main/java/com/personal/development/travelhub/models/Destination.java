package com.personal.development.travelhub.models;

public class Destination {
    private String description;
    private String highlight;
    private String status;
    private String imageUrl;

    // No-argument constructor for Firestore deserialization
    public Destination() {}

    // Constructor with fields
    public Destination(String description, String highlight, String status, String imageUrl) {
        this.description = description;
        this.highlight = highlight;
        this.status = status;
        this.imageUrl = imageUrl;
    }

    // Getters and setters for each field
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

