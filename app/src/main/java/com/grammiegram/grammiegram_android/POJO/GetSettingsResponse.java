package com.grammiegram.grammiegram_android.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;

public class GetSettingsResponse implements APIResponse {
    @SerializedName("audio_notifications")
    @Expose
    private String audioNotifications;

    @SerializedName("read_receipts")
    @Expose
    private boolean readReceipts;

    @SerializedName("interactive_gram")
    @Expose
    private boolean interactiveGram;

    @SerializedName("profanity_filter")
    @Expose
    private boolean profanityFilter;

    public String getAudioNotifications() {
        return audioNotifications;
    }

    public void setAudioNotifications(String audioNotifications) {
        this.audioNotifications = audioNotifications;
    }

    public boolean getReadReceipts() {
        return readReceipts;
    }

    public void setReadReceipts(boolean readReceipts) {
        this.readReceipts = readReceipts;
    }

    public boolean getInteractiveGram() {
        return interactiveGram;
    }

    public void setInteractiveGram(boolean interactiveGram) {
        this.interactiveGram = interactiveGram;
    }

    public boolean getProfanityFilter() {
        return profanityFilter;
    }

    public void setProfanityFilter(boolean profanityFilter) {
        this.profanityFilter = profanityFilter;
    }
}
