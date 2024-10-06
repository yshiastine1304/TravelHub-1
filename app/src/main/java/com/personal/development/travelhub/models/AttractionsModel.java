package com.personal.development.travelhub.models;

public class AttractionsModel {
    private String imageUrl_1;
    private String Caption_1;
    private String documentUID;

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

    public String getDocumentUID() {
        return documentUID;
    }

    public void setDocumentUID(String documentUID) {
        this.documentUID = documentUID;
    }

    public AttractionsModel(String imageUrl_1, String caption_1, String documentUID) {
        this.imageUrl_1 = imageUrl_1;
        Caption_1 = caption_1;
        this.documentUID = documentUID;
    }
}
