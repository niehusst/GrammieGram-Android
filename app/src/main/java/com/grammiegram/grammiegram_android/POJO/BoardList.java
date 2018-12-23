package com.grammiegram.grammiegram_android.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BoardList {
    @SerializedName("boards")
    @Expose
    private List<Board> boards = null;

    public List<Board> getBoards() {return boards;}
    public void setBoards(List<Board> boards) {this.boards = boards;}
}
