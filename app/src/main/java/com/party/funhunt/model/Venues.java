package com.party.funhunt.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.party.funhunt.model.VenueList;

public class Venues {

    @SerializedName("result")
    @Expose
    private List<VenueList> result = null;

    public List<VenueList> getResult() {
        return result;
    }

    public void setResult(List<VenueList> result) {
        this.result = result;
    }

}
