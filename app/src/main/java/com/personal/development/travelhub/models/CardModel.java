package com.personal.development.travelhub.models;

public class CardModel {
    private String imageUrl;
    private String Caption;
    private String documentUID;

    public CardModel(String imageUrl, String caption, String documentUID) {
        this.imageUrl = imageUrl;
        Caption = caption;
        this.documentUID = documentUID;
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

    public String getDocumentUID() {
        return documentUID;
    }

    public void setDocumentUID(String documentUID) {
        this.documentUID = documentUID;
    }


}
