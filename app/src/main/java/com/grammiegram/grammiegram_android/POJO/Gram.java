package com.grammiegram.grammiegram_android.POJO;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gram {

    @SerializedName("sender_first_name")
    @Expose
    private String senderFirstName;

    @SerializedName("sender_last_name")
    @Expose
    private String senderLastName;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("till")
    @Expose
    private String till;

    @SerializedName("year")
    @Expose
    private Integer year;

    @SerializedName("month")
    @Expose
    private Integer month;

    @SerializedName("day")
    @Expose
    private Integer day;

    @SerializedName("hour")
    @Expose
    private Integer hour;

    @SerializedName("minute")
    @Expose
    private Integer minute;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderFirstName() {
        return senderFirstName;
    }

    public void setSenderFirstName(String senderFirstName) {
        this.senderFirstName = senderFirstName;
    }

    public String getSenderLastName() {
        return senderLastName;
    }

    public void setSenderLastName(String senderLastName) {
        this.senderLastName = senderLastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTill() {
        return till;
    }

    public void setTill(String till) {
        this.till = till;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        try {
            Gram gram = (Gram) obj;
            //Grams are the same if they have the exact same
            return  this.getId().equals(gram.getId());
        } catch(ClassCastException objNotGram) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        StringBuilder builder = new StringBuilder(this.year);
        builder.append(this.month).append(this.day).append(this.hour).append(this.minute);
        return Integer.parseInt(builder.toString());
    }

}
