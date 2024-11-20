package com.personal.development.travelhub.models;

public class TourSaveModel {
    private String dateRange;
    private String tourName;
    private String image_link_1;

    public TourSaveModel(){}
    public TourSaveModel(String dateRange, String tourName, String image_link_1) {
        this.dateRange = dateRange;
        this.tourName = tourName;
        this.image_link_1 = image_link_1;
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
