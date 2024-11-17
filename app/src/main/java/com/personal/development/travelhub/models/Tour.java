package com.personal.development.travelhub.models;

public class Tour {
    private String tourName;
    private String destinationCounter;
    private String destinationName;
    private String startTime;

    public Tour() {
        // Empty constructor for Firestore serialization
    }

    public Tour(String tourName, String destinationCounter, String destinationName, String startTime) {
        this.tourName = tourName;
        this.destinationCounter = destinationCounter;
        this.destinationName = destinationName;
        this.startTime = startTime;
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

