package com.personal.development.travelhub.models;

public class TripsModel {
    private String tripImgUrl;
    private String tripReviews;
    private String tripDescription;

    // No-argument constructor
    public TripsModel() {
        // Required empty constructor for Firestore deserialization
    }
    public TripsModel(String tripImgUrl, String tripReviews, String tripDescription) {
        this.tripImgUrl = tripImgUrl;
        this.tripReviews = tripReviews;
        this.tripDescription = tripDescription;
    }

    public String getTripImgUrl() {
        return tripImgUrl;
    }

    public void setTripImgUrl(String tripImgUrl) {
        this.tripImgUrl = tripImgUrl;
    }

    public String getTripReviews() {
        return tripReviews;
    }

    public void setTripReviews(String tripReviews) {
        this.tripReviews = tripReviews;
    }

    public String getTripDescription() {
        return tripDescription;
    }

    public void setTripDescription(String tripDescription) {
        this.tripDescription = tripDescription;
    }
}
