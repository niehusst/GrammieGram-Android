package com.grammiegram.grammiegram_android.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;

public class MarkNotNewResponse implements APIResponse {
    @SerializedName("received")
    @Expose
    private Boolean received;

    public Boolean getReceived() {
        return received;
    }

    public void setReceived(Boolean received) {
        this.received = received;
    }
}
