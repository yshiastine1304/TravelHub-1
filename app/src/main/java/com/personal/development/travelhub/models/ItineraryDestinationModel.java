package com.personal.development.travelhub.models;

import com.google.firebase.firestore.PropertyName;

public class ItineraryDestinationModel {
    @PropertyName("activity")
    private String activity;
    @PropertyName("day")
    private String day;
    @PropertyName("destination_counter")
    private String destinationCounter;
    @PropertyName("destination_name")
    private String destinationName;
    @PropertyName("start_time")
    private String startTime;

    // Firestore requires an empty constructor for deserialization
    public ItineraryDestinationModel() {}

    public ItineraryDestinationModel(String activity,String day, String destinationCounter, String destinationName, String startTime) {
        this.activity = activity;
        this.day = day;
        this.destinationCounter = destinationCounter;
        this.destinationName = destinationName;
        this.startTime = startTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getActivity() {
        return activity;
    }

    public String getDestinationCounter() {
        return destinationCounter;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String getStartTime() {
        return startTime;
    }
}

