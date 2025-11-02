package com.lvhm.covertocover.models;
import com.google.gson.annotations.SerializedName;
import java.util.List;
public class PlaceResponse {
    @SerializedName("status")
    public String status;
    public String getStatus(){
        return status;
    }
    @SerializedName("results")
    private List<PlaceResult> results;

    public List<PlaceResult> getResults() {
        return results;
    }


}

