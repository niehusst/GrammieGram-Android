package com.grammiegram.grammiegram_android.services;

import android.content.SharedPreferences;

import com.grammiegram.grammiegram_android.POJO.GetSettingsResponse;
import com.grammiegram.grammiegram_android.interfaces.APIResponse;
import com.grammiegram.grammiegram_android.interfaces.CallBack;

import okhttp3.ResponseBody;

public class SetPreferencesService implements CallBack {

    private SharedPreferences sharedPreferences;

    public SetPreferencesService(SharedPreferences preferences) {
        this.sharedPreferences = preferences;
    }

    /**
     * Place settings from api getSettings into shared preferences.
     *
     * @param response - GetSettingsResponse holding user preferences from api call to getSettings
     */
    @Override
    public void onSuccess(APIResponse response) {
        GetSettingsResponse settingsResponse = (GetSettingsResponse) response;
        //save audio notifications in shared prefs
        this.sharedPreferences.edit().putString("audio_notifications", settingsResponse.getAudioNotifications()).apply();
    }

    @Override
    public void onNetworkError(String error) {
        //do nothing, network errors will be handled on UI thread by retry button
    }

    @Override
    public void onServerError(int code, ResponseBody body) {
        //do nothing, server errors will be handled on UI thread by retry button
    }
}
