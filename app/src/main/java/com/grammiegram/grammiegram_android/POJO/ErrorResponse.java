package com.grammiegram.grammiegram_android.POJO;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorResponse implements APIResponse {
    @SerializedName("error")
    @Expose
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String convertToJson() {
        return new Gson().toJson(this);
    }
}
