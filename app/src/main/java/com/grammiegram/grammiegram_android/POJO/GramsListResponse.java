package com.grammiegram.grammiegram_android.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;

import java.util.List;

public class GramsListResponse implements APIResponse {
    @SerializedName("board_display_name")
    @Expose
    private String boardDisplayName;

    @SerializedName("grams")
    @Expose
    private List<Gram> grams;

    @SerializedName("board_first_name")
    @Expose
    private String boardFirstName;

    public String getBoardDisplayName() {
        return boardDisplayName;
    }

    public void setBoardDisplayName(String boardDisplayName) {
        this.boardDisplayName = boardDisplayName;
    }

    public List<Gram> getGrams() {
        return grams;
    }

    public void setGrams(List<Gram> grams) {
        this.grams = grams;
    }

    public String getBoardFirstName() {
        return boardFirstName;
    }

    public void setBoardFirstName(String boardFirstName) {
        this.boardFirstName = boardFirstName;
    }
}
