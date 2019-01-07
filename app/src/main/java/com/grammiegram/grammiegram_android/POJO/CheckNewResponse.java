package com.grammiegram.grammiegram_android.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;

import java.util.List;

public class CheckNewResponse implements APIResponse {
    @SerializedName("needed")
    @Expose
    private Boolean needed;

    @SerializedName("new_grams")
    @Expose
    private List<Gram> newGrams = null;

    public Boolean getNeeded() {
        return needed;
    }

    public void setNeeded(Boolean needed) {
        this.needed = needed;
    }

    public List<Gram> getNewGrams() {
        return newGrams;
    }

    public void setNewGrams(List<Gram> newGrams) {
        this.newGrams = newGrams;
    }
}
