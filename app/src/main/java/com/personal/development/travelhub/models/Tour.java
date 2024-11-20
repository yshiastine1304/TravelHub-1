package com.personal.development.travelhub.models;

public class Tour {
    private String tourName;
    private String destinationCounter;
    private String destinationName;
    private String startTime;
    private String image_link_1;

    public Tour() {
        // Empty constructor for Firestore serialization
    }

    public Tour(String tourName, String destinationCounter, String destinationName, String startTime, String image_link_1) {
        this.tourName = tourName;
        this.destinationCounter = destinationCounter;
        this.destinationName = destinationName;
        this.startTime = startTime;
        this.image_link_1 = image_link_1;
    }

    public String getImage_link_1() {
        return image_link_1;
    }

    public void setImage_link_1(String image_link_1) {
        this.image_link_1 = image_link_1;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public String getDestinationCounter() {
        return destinationCounter;
    }

    public void setDestinationCounter(String destinationCounter) {
        this.destinationCounter = destinationCounter;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}

