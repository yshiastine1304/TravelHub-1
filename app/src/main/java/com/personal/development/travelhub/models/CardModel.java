package com.personal.development.travelhub.models;

public class CardModel {
    private String imageUrl;
    private String Caption;

    public CardModel(String imageUrl, String caption) {
        this.imageUrl = imageUrl;
        Caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }
}
