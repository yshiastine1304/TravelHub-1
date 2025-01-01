package com.personal.development.travelhub;

import com.google.gson.annotations.SerializedName;

public class NominatimResult {
    @SerializedName("display_name")
    private String displayName;

    @SerializedName("lat")
    private String latitude;

    @SerializedName("lon")
    private String longitude;

    public String getDisplayName() {
        return displayName;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }
}

