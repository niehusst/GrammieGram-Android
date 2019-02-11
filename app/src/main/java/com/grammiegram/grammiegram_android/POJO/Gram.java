package com.grammiegram.grammiegram_android.POJO;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Gram implements Parcelable {

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
    private Integer id;

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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
            //prevent null pointer exception
            if(gram == null) return false;

            //Grams are the same if they have the exact same Id
            return  this.hashCode() == gram.hashCode(); //TODO: change back to ID when api is updated
        } catch(ClassCastException objNotGram) {
            return false;
        }
    }

    @Override
    public int hashCode() { //TODO: make this return id instead. when api update
        StringBuilder builder = new StringBuilder(this.year);
        builder.append(this.month).append(this.day).append(this.hour).append(this.minute);
        return Integer.parseInt(builder.toString());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(senderFirstName);
        parcel.writeString(senderLastName);
        parcel.writeString(message);
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(till);
        if (year == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(year);
        }
        if (month == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(month);
        }
        if (day == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(day);
        }
        if (hour == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(hour);
        }
        if (minute == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(minute);
        }
    }

    protected Gram(Parcel in) {
        senderFirstName = in.readString();
        senderLastName = in.readString();
        message = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        till = in.readString();
        if (in.readByte() == 0) {
            year = null;
        } else {
            year = in.readInt();
        }
        if (in.readByte() == 0) {
            month = null;
        } else {
            month = in.readInt();
        }
        if (in.readByte() == 0) {
            day = null;
        } else {
            day = in.readInt();
        }
        if (in.readByte() == 0) {
            hour = null;
        } else {
            hour = in.readInt();
        }
        if (in.readByte() == 0) {
            minute = null;
        } else {
            minute = in.readInt();
        }
    }

    public static final Creator<Gram> CREATOR = new Creator<Gram>() {
        @Override
        public Gram createFromParcel(Parcel in) {
            return new Gram(in);
        }

        @Override
        public Gram[] newArray(int size) {
            return new Gram[size];
        }
    };
}
