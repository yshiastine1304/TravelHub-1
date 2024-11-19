package com.personal.development.travelhub.models;

public class TourSaveModel {
    private String dateRange;
    private String tourName;

    public TourSaveModel(){}
    public TourSaveModel(String dateRange, String tourName) {
        this.dateRange = dateRange;
        this.tourName = tourName;
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
