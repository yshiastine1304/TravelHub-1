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
    @PropertyName("image_link_1")
    private String imageLink1;

    // Firestore requires an empty constructor for deserialization
    public ItineraryDestinationModel() {}

    public ItineraryDestinationModel(String activity,String day, String destinationCounter, String destinationName, String startTime, String imageLink1) {
        this.activity = activity;
        this.day = day;
        this.destinationCounter = destinationCounter;
        this.destinationName = destinationName;
        this.startTime = startTime;
        this.imageLink1 = imageLink1;
    }

    public String getImageLink1() {
        return imageLink1;
    }

    public void setImageLink1(String imageLink1) {
        this.imageLink1 = imageLink1;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
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

