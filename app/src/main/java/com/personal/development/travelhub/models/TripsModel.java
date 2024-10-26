package com.personal.development.travelhub.models;

import java.util.ArrayList;
import java.util.List;

public class TripsModel {
    private String tripImgUrl;
    private String tripDescription;
    private String tripHighlight;
    private String tripStatus;
    private String tripCounts;  // Could be an integer or derived from the list size in the Adapter, but keeping it for backward compatibility
    private String tripDateFromAndTo;
    private List<String> destinations;  // New list for multiple destinations

    // No-argument constructor
    public TripsModel() {
        // Required empty constructor for Firestore deserialization
    }

    // Constructor with all fields
    public TripsModel(String tripImgUrl, String tripDescription, String tripHighlight, String tripStatus, String tripCounts, String tripDateFromAndTo, List<String> destinations) {
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
    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations != null ? destinations : new ArrayList<>();
    }
}

