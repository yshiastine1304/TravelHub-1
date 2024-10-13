package com.personal.development.travelhub.models;

public class WishlistModels {
    private String tripName;
    private String imageUrl;
    private String reviews;
    private String attraction_uid;

    // No-argument constructor (required by Firebase)
    public WishlistModels() {
        // Empty constructor needed for Firestore deserialization
    }
    public WishlistModels(String tripName, String imageUrl, String reviews, String attraction_uid) {
        this.tripName = tripName;
        this.imageUrl = imageUrl;
        this.reviews = reviews;
        this.attraction_uid = attraction_uid;
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

    public String getAttraction_uid() {
        return attraction_uid;
    }

    public void setAttraction_uid(String attraction_uid) {
        this.attraction_uid = attraction_uid;
    }
}
