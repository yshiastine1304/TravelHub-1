package com.personal.development.travelhub.models;

import com.google.firebase.firestore.PropertyName;

public class ActivityListModel {
    @PropertyName("activity")
    private String activity;
    @PropertyName("destination_counter")
    private String destinationCounter;
    @PropertyName("destination_name")
    private String destinationName;
    @PropertyName("start_time")
    private String startTime;

    // Empty constructor for Firestore
    public ActivityListModel() {
        // Firestore requires an empty constructor
    }

    // Constructor to initialize the class
    public ActivityListModel(String activity, String destinationCounter, String destinationName, String startTime) {
        this.activity = activity;
        this.destinationCounter = destinationCounter;
        this.destinationName = destinationName;
        this.startTime = startTime;
    }

    // Getter for activity
    public String getActivity() {
        return activity;
    }

    // Getter for destinationCounter
    public String getDestinationCounter() {
        return destinationCounter;
    }

    // Getter for destinationName
    public String getDestinationName() {
        return destinationName;
    }

    // Getter for startTime
    public String getStartTime() {
        return startTime;
    }

    // Setter for activity
    public void setActivity(String activity) {
        this.activity = activity;
    }

    // Setter for destinationCounter
    public void setDestinationCounter(String destinationCounter) {
        this.destinationCounter = destinationCounter;
    }

    // Setter for destinationName
    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    // Setter for startTime
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}

