package com.personal.development.travelhub.models;

public class WishlistModels {
    private String tripName;
    private String imageUrl;
    private String reviews;

    // No-argument constructor (required by Firestore)
    public WishlistModels() {
        // Default constructor
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getReviews() {
        return reviews;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public WishlistModels(String tripName, String imageUrl, String reviews) {
        this.tripName = tripName;
        this.imageUrl = imageUrl;
        this.reviews = reviews;
    }
}
