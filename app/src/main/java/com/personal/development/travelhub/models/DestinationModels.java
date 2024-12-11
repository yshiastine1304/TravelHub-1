package com.personal.development.travelhub.models;

public class DestinationModels {
    private String destination_name;
    private String location;
    private String highlight;
    private String bus_fare;
    private String entrance_fee;
    private String what_to_expect;
    private String other_details;
    private String image_link_1;
    private String recommend_interest;
    private String time;
    public DestinationModels(){
    // FOR FIRESTORE
    }

    public DestinationModels(String destination_name, String location, String highlight, String bus_fare, String entrance_fee, String what_to_expect, String other_details, String image_link_1, String recommend_interest, String time) {
        this.destination_name = destination_name;
        this.location = location;
        this.highlight = highlight;
        this.bus_fare = bus_fare;
        this.entrance_fee = entrance_fee;
        this.what_to_expect = what_to_expect;
        this.other_details = other_details;
        this.image_link_1 = image_link_1;
        this.recommend_interest = recommend_interest;
        this.time = time;
    }

    public String getRecommend_interest() {
        return recommend_interest;
    }

    public void setRecommend_interest(String recommend_interest) {
        this.recommend_interest = recommend_interest;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public void setDestination_name(String destination_name) {
        this.destination_name = destination_name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getBus_fare() {
        return bus_fare;
    }

    public void setBus_fare(String bus_fare) {
        this.bus_fare = bus_fare;
    }

    public String getEntrance_fee() {
        return entrance_fee;
    }

    public void setEntrance_fee(String entrance_fee) {
        this.entrance_fee = entrance_fee;
    }

    public String getWhat_to_expect() {
        return what_to_expect;
    }

    public void setWhat_to_expect(String what_to_expect) {
        this.what_to_expect = what_to_expect;
    }

    public String getOther_details() {
        return other_details;
    }

    public void setOther_details(String other_details) {
        this.other_details = other_details;
    }

    public String getImage_link_1() {
        return image_link_1;
    }

    public void setImage_link_1(String image_link_1) {
        this.image_link_1 = image_link_1;
    }
}
