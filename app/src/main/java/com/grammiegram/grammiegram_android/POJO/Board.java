package com.grammiegram.grammiegram_android.POJO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * JSON response conversion to a plain old java object
 */
public class Board implements Parcelable {
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

    /****** Parcel methods to allow Board objects to be passed by intent ******/

    /**
     * plain constructor for retrofit
     */
    public Board() {}

    /**
     * parcel constructor to rebuild parcelled board
     */
    public Board(Parcel parcel) {
        this.boardFirstName = parcel.readString();
        this.boardLastName = parcel.readString();
        this.boardDisplayName = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Write contents of board object to dest parcel
     * @param dest - parcel to write fields to
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.boardFirstName);
        dest.writeString(this.boardLastName);
        dest.writeString(this.boardDisplayName);
    }

    /**
     * Parceled Board creator
     */
    public static final Parcelable.Creator<Board> CREATOR = new Parcelable.Creator<Board>() {

        @Override
        public Board createFromParcel(Parcel parcel) {
            return new Board(parcel);
        }

        @Override
        public Board[] newArray(int size) {
            return new Board[size];
        }
    };
}
