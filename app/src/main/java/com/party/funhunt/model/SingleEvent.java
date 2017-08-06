package com.party.funhunt.model;

/**
 * Created by Ermike on 4/9/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleEvent {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("result")
    @Expose
    private SingleEventDetails result;

    public Boolean getSuccess() {
        return success;
    }

    public SingleEventDetails getResult() {
        return result;
    }

    public void setResult(SingleEventDetails result) {
        this.result = result;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

}
