package com.personal.development.travelhub.models;

public class TourSaveModel {
    private String dateRange;
    private String tourName;
    private String image_link_1;
    private String documentId; // Add this field
    private String userId;     // Add this if needed
    private String tourID;

    public TourSaveModel(){}

    public TourSaveModel(String dateRange, String tourName, String image_link_1, String documentId, String userId, String tourID) {
        this.dateRange = dateRange;
        this.tourName = tourName;
        this.image_link_1 = image_link_1;
        this.documentId = documentId;
        this.userId = userId;
        this.tourID = tourID;
    }

    public String getTourID() {
        return tourID;
    }

    public void setTourID(String tourID) {
        this.tourID = tourID;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImage_link_1() {
        return image_link_1;
    }

    public void setImage_link_1(String image_link_1) {
        this.image_link_1 = image_link_1;
    }

    public String getDateRange() {
        return dateRange;
    }

    public void setDateRange(String dateRange) {
        this.dateRange = dateRange;
    }

    public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }
}
