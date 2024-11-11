package com.personal.development.travelhub.models;

public class DestinationModels {
    private String destination_name;
    private String image_link_1;

    public DestinationModels() {
        // Empty constructor needed for Firestore deserialization
    }

    public DestinationModels(String destination_name, String image_link_1) {
        this.destination_name = destination_name;
        this.image_link_1 = image_link_1;
    }

    public String getDestination_name() {
        return destination_name;
    }

    public void setDestination_name(String destination_name) {
        this.destination_name = destination_name;
    }

    public String getImage_link_1() {
        return image_link_1;
    }

    public void setImage_link_1(String image_link_1) {
        this.image_link_1 = image_link_1;
    }
}
