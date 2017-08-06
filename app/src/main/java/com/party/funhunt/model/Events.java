package com.party.funhunt.model;

/**
 * Created by Ermike on 3/28/2017.
 */

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Events {

    @SerializedName("result")
    @Expose
    private List<EventList> result = null;

    public List<EventList> getEvents() {
        return result;
    }

    public void setEvents(List<EventList> result) {
        this.result = result;
    }
}