package com.grammiegram.grammiegram_android.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * JSON response conversion to a plain old java object
 */
public class Board {
    @SerializedName("board_first_name")
    @Expose
    private String boardFirstName;

    @SerializedName("board_last_name")
    @Expose
    private String boardLastName;

    @SerializedName("board_display_name")
    @Expose
    private String boardDisplayName;

    public String getBoardFirstName() {
        return boardFirstName;
    }

    public void setBoardFirstName(String boardFirstName) {
        this.boardFirstName = boardFirstName;
    }

    public String getBoardLastName() {
        return boardLastName;
    }

    public void setBoardLastName(String boardLastName) {
        this.boardLastName = boardLastName;
    }

    public String getBoardDisplayName() {
        return this.boardDisplayName;
    }

    public void setBoardDisplayName(String boardDisplayName) {
        this.boardDisplayName = boardDisplayName;
    }
}
