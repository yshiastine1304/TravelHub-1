package com.personal.development.travelhub.models;

public class AddTourModel {

    private String tourName;
    private String description;
    private String location;
    private String inclusionDetails;
    private String destinationDetails;
    private String activityDetails;
    private String duration;
    private String price;
    private String minimumAge;
    private String pricePer;
    private String otherDetails;

    public AddTourModel(){}

    public AddTourModel(String tourName, String description, String location, String inclusionDetails, String destinationDetails, String activityDetails, String duration, String price, String minimumAge, String pricePer, String otherDetails) {
        this.tourName = tourName;
        this.description = description;
        this.location = location;
        this.inclusionDetails = inclusionDetails;
        this.destinationDetails = destinationDetails;
        this.activityDetails = activityDetails;
        this.duration = duration;
        this.price = price;
        this.minimumAge = minimumAge;
        this.pricePer = pricePer;
        this.otherDetails = otherDetails;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInclusionDetails() {
        return inclusionDetails;
    }

    public void setInclusionDetails(String inclusionDetails) {
        this.inclusionDetails = inclusionDetails;
    }

    public String getDestinationDetails() {
        return destinationDetails;
    }

    public void setDestinationDetails(String destinationDetails) {
        this.destinationDetails = destinationDetails;
    }

    public String getActivityDetails() {
        return activityDetails;
    }

    public void setActivityDetails(String activityDetails) {
        this.activityDetails = activityDetails;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMinimumAge() {
        return minimumAge;
    }

    public void setMinimumAge(String minimumAge) {
        this.minimumAge = minimumAge;
    }

    public String getPricePer() {
        return pricePer;
    }

    public void setPricePer(String pricePer) {
        this.pricePer = pricePer;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public void setOtherDetails(String otherDetails) {
        this.otherDetails = otherDetails;
    }
}
