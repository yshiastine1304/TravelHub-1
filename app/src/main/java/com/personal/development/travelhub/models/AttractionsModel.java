package com.personal.development.travelhub.models;

public class AttractionsModel {
    private String imageUrl_1;
    private String Caption_1;
    private String imageUrl_2;
    private String Caption_2;

    public String getImageUrl_1() {
        return imageUrl_1;
    }

    public void setImageUrl_1(String imageUrl_1) {
        this.imageUrl_1 = imageUrl_1;
    }

    public String getCaption_1() {
        return Caption_1;
    }

    public void setCaption_1(String caption_1) {
        Caption_1 = caption_1;
    }

    public String getImageUrl_2() {
        return imageUrl_2;
    }

    public void setImageUrl_2(String imageUrl_2) {
        this.imageUrl_2 = imageUrl_2;
    }

    public String getCaption_2() {
        return Caption_2;
    }

    public void setCaption_2(String caption_2) {
        Caption_2 = caption_2;
    }

    public AttractionsModel(String imageUrl_1, String caption_1, String imageUrl_2, String caption_2) {
        this.imageUrl_1 = imageUrl_1;
        Caption_1 = caption_1;
        this.imageUrl_2 = imageUrl_2;
        Caption_2 = caption_2;
    }


}
