package com.personal.development.travelhub;

import java.io.Serializable;

public class Attraction implements Serializable {
    private String name;
    private String description;
    private String latitude;
    private String longitude;
    private int imageResourceId;
    private String category;
    private String primaryLanguage;
    private String seasons;
    private String entranceFee;
    private String busFare;

    public Attraction(String name, String description, String latitude, String longitude, int imageResourceId, String category,
                      String primaryLanguage, String seasons, String entranceFee, String busFare) {
        this.name = name;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageResourceId = imageResourceId;
        this.category = category;
        this.primaryLanguage = primaryLanguage;
        this.seasons = seasons;
        this.entranceFee = entranceFee;
        this.busFare = busFare;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    public String getCategory() {
        return category;
    }

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public String getSeasons() {
        return seasons;
    }

    public String getEntranceFee() {
        return entranceFee;
    }

    public String getBusFare() {
        return busFare;
    }

    public String calculateBusFare(double userLatitude, double userLongitude) {
        double attractionLat = Double.parseDouble(this.latitude);
        double attractionLon = Double.parseDouble(this.longitude);

        // Calculate distance using Haversine formula
        double R = 6371; // Earth's radius in kilometers
        double dLat = Math.toRadians(attractionLat - userLatitude);
        double dLon = Math.toRadians(attractionLon - userLongitude);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(userLatitude)) * Math.cos(Math.toRadians(attractionLat)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = R * c;

        // Calculate fare based on distance (example: ₱10 per km)
        double fare = Math.round(distance * 10);
        return "₱" + fare;
    }
}

