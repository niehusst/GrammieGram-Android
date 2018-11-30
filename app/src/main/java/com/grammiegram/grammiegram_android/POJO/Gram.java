package com.grammiegram.grammiegram_android.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gram {

    @SerializedName("sender_first_name")
    @Expose
    private String senderFirstName;

    @SerializedName("message")
    @Expose
    private String message;

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
}
