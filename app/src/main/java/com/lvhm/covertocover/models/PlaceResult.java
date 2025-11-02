package com.lvhm.covertocover.models;

import com.google.gson.annotations.SerializedName;

public class PlaceResult {
    @SerializedName("name")
    private String name;

    @SerializedName("vicinity")
    private String vicinity;

    @SerializedName("geometry")
    private Geometry geometry;

    public String getName() {
        return name;
    }
    public String getVicinity() {
        return vicinity;
    }
    public Geometry getGeometry() {
        return geometry;
    }
}
