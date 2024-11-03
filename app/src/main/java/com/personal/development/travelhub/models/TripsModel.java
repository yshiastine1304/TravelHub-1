package com.personal.development.travelhub.models;

import java.util.ArrayList;
import java.util.List;

public class TripsModel {
    private String tripImgUrl;
    private String tripDescription;
    private String tripHighlight;
    private String tripStatus;
    private String tripCounts;
    private String tripDateFromAndTo;
    private List<Destination> destinations;  // Change to List<Destination>

    // No-argument constructor for Firestore deserialization
    public TripsModel() {}

    // Constructor with all fields
    public TripsModel(String tripImgUrl, String tripDescription, String tripHighlight, String tripStatus, String tripCounts, String tripDateFromAndTo, List<Destination> destinations) {
        this.tripImgUrl = tripImgUrl;
        this.tripDescription = tripDescription;
        this.tripHighlight = tripHighlight;
        this.tripStatus = tripStatus;
        this.tripCounts = tripCounts;
        this.tripDateFromAndTo = tripDateFromAndTo;
        this.destinations = destinations != null ? destinations : new ArrayList<>();
    }

    // Getter and setter for tripImgUrl
    public String getTripImgUrl() {
        return tripImgUrl;
    }

    public void setTripImgUrl(String tripImgUrl) {
        this.tripImgUrl = tripImgUrl;
    }

    // Getter and setter for tripDescription
    public String getTripDescription() {
        return tripDescription;
    }

    public void setTripDescription(String tripDescription) {
        this.tripDescription = tripDescription;
    }

    // Getter and setter for tripHighlight
    public String getTripHighlight() {
        return tripHighlight;
    }

    public void setTripHighlight(String tripHighlight) {
        this.tripHighlight = tripHighlight;
    }

    // Getter and setter for tripStatus
    public String getTripStatus() {
        return tripStatus;
    }

    public void setTripStatus(String tripStatus) {
        this.tripStatus = tripStatus;
    }

    // Getter and setter for tripCounts
    public String getTripCounts() {
        return tripCounts;
    }

    public void setTripCounts(String tripCounts) {
        this.tripCounts = tripCounts;
    }

    // Getter and setter for tripDateFromAndTo
    public String getTripDateFromAndTo() {
        return tripDateFromAndTo;
    }

    public void setTripDateFromAndTo(String tripDateFromAndTo) {
        this.tripDateFromAndTo = tripDateFromAndTo;
    }

    // Getter and setter for destinations list
    // Getter and setter for destinations
    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations != null ? destinations : new ArrayList<>();
    }
}

